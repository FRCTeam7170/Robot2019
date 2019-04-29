package frc.team7170.lib.networktables;

import edu.wpi.first.networktables.*;
import frc.team7170.lib.Name;
import frc.team7170.lib.Named;
import frc.team7170.lib.ReflectUtil;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: make stuff optionally sendable
// TODO: allow passing functional interfaces for communicators in addition to annotated methods/fields
// TODO: POST-SEASON: Deprecate the entire annotation and use interface-based communicator registration to maintain type safety
public final class Communication implements Named {

    private static final Logger LOGGER = Logger.getLogger(Communication.class.getName());

    private static final String PATH_SEPARATOR = String.valueOf(NetworkTable.PATH_SEPARATOR);
    private static final String ENTRY_NAME_PREFIX_SEP = "_";

    // TODO: TEMP
    // private final Commander commander = new Commander(NetworkTableInstance.getDefault(), getNameObject());
    private final Map<String, Transmitter> transmitterMap = new HashMap<>();
    private final Map<String, Receiver> receiverMap = new HashMap<>();

    private Communication() {}

    private static final Communication INSTANCE = new Communication();

    public static Communication getInstance() {
        return INSTANCE;
    }

    public void registerStaticCommunicator(Class<?> cls, Name name, NetworkTable table) {
        findCommAnnotations(null, cls, name, table);
    }

    public void registerStaticCommunicator(Class<?> cls, NetworkTable table) {
        registerStaticCommunicator(cls, Name.UNNAMED, table);
    }

    public void registerCommunicator(Named obj, NetworkTable table) {
        findCommAnnotations(obj, obj.getClass(), obj.getNameObject(), table);
    }

    private void findCommAnnotations(Object obj, Class<?> cls, Name prefix, NetworkTable table) {
        ReflectUtil.getMethodAnnotationStream(cls, Transmit.class).forEach(pair -> {
            Method method = pair.getLeft();
            Transmit transmit = pair.getRight();

            // Sanity checks.
            if (method.getAnnotation(Receive.class) != null) {
                throw new IllegalCommunicatorException("a method cannot be a transmitter and a receiver");
            }
            doMethodAssertions(obj, method);
            ReflectUtil.assertParameterCount(method, 0);
            assertValidNTType(method.getReturnType());

            NetworkTableEntry entry = resolveEntry(table, prefix, transmit.value(), method);
            Runnable runnable = () -> invokeWithHandling(entry::setValue, method, obj);
            newTransmitter(runnable, transmit.pollRateMs(), entry);
        });

        ReflectUtil.getMethodAnnotationStream(cls, Receive.class).forEach(pair -> {
            Method method = pair.getLeft();
            Receive receive = pair.getRight();

            // Sanity checks.
            doMethodAssertions(obj, method);
            ReflectUtil.assertParameterCount(method, 1);
            ReflectUtil.assertReturnType(method, void.class);

            Consumer<EntryNotification> consumer;
            Consumer<Object> retConsumer = (x) -> {};  // Return type is void!
            Class<?> paramType = method.getParameterTypes()[0];
            if (paramType == EntryNotification.class) {
                consumer = notification -> invokeWithHandling(retConsumer, method, obj, notification);
            } else if (paramType == NetworkTableValue.class) {
                consumer = notification -> invokeWithHandling(retConsumer, method, obj, notification.value);
            } else if (isValidNTType(paramType)) {
                consumer = notification -> invokeWithHandling(retConsumer, method, obj, notification.value.getValue());
            } else {
                throw new IllegalCommunicatorException("invalid method parameter type; expected one of: " +
                        "'EntryNotification', 'NetworkTableValue', or any valid networktables type");
            }
            NetworkTableEntry entry = resolveEntry(table, prefix, receive.value(), method);
            newReceiver(consumer, receive.flags(), entry);
        });

        ReflectUtil.getFieldAnnotationStream(cls, CommField.class).forEach(pair -> {
            Field field = pair.getLeft();
            CommField commField = pair.getRight();

            // Sanity checks.
            doFieldAssertions(obj, field);

            NetworkTableEntry entry = resolveEntry(table, prefix, commField.value(), field);
            if (commField.transmit()) {
                Runnable runnable = () -> {
                    try {
                        tryMakeAccessible(field);
                        entry.setValue(field.get(obj));
                    } catch (IllegalAccessException e) {
                        LOGGER.log(Level.SEVERE, "Failed to access field.", e);
                    }
                };
                newTransmitter(runnable, commField.pollRateMs(), entry);
            }
            if (commField.receive()) {
                ReflectUtil.assertNonFinal(field);
                Consumer<EntryNotification> consumer = notification -> {
                    try {
                        tryMakeAccessible(field);
                        field.set(obj, notification.value.getValue());
                    } catch (IllegalAccessException e) {
                        LOGGER.log(Level.SEVERE, "Failed to access field.", e);
                    }
                };
                newReceiver(consumer, commField.flags(), entry);
            }
        });
    }

    private void newTransmitter(Runnable runnable, int pollRateMs, NetworkTableEntry entry) {
        Transmitter transmitter = new Transmitter(runnable, pollRateMs, entry);
        if (!transmitter.start()) {
            transmitter.invoke();
        }
        Transmitter old = transmitterMap.put(transmitter.getName(), transmitter);
        if (old != null) {
            LOGGER.warning(String.format("More than one transmitter with name '%s' registered; removing oldest one.", old.getName()));
            old.cancel();
        }
    }

    private void newReceiver(Consumer<EntryNotification> consumer, int flags, NetworkTableEntry entry) {
        Receiver receiver = new Receiver(consumer, flags, entry);
        receiver.start();
        Receiver old = receiverMap.put(receiver.getName(), receiver);
        if (old != null) {
            LOGGER.warning(String.format("More than one receiver with name '%s' registered; removing oldest one.", old.getName()));
            old.cancel();
        }
    }

    private static void doMethodAssertions(Object obj, Method method) {
        assertStaticness(obj, method);
        ReflectUtil.assertInvokable(method);
    }

    private static void doFieldAssertions(Object obj, Field field) {
        assertStaticness(obj, field);
        assertValidNTType(field.getType());
    }

    private static void tryMakeAccessible(AccessibleObject accessibleObject) {
        if (!accessibleObject.trySetAccessible()) {
            LOGGER.warning("Failed to make object accessible.");
        }
    }

    private static void assertStaticness(Object obj, Member member) {
        boolean isStatic = Modifier.isStatic(member.getModifiers());
        if (!((obj != null && !isStatic) || (obj == null && isStatic))) {
            throw new IllegalCommunicatorException("obj given for static communicator or no obj given for instance communicator");
        }
    }

    private static final ArrayList<Class<?>> PRIMITIVE_NUMBER_TYPES = new ArrayList<>(6);

    static {
        PRIMITIVE_NUMBER_TYPES.add(byte.class);
        PRIMITIVE_NUMBER_TYPES.add(short.class);
        PRIMITIVE_NUMBER_TYPES.add(int.class);
        PRIMITIVE_NUMBER_TYPES.add(long.class);
        PRIMITIVE_NUMBER_TYPES.add(float.class);
        PRIMITIVE_NUMBER_TYPES.add(double.class);
    }

    private static boolean isValidNTType(Class<?> cls) {
        if (cls.isArray()) {
            cls = cls.getComponentType();
        }

        if ((cls == boolean.class) || (cls == Boolean.class)) {
            return true;
        } else if (Number.class.isAssignableFrom(cls) || PRIMITIVE_NUMBER_TYPES.contains(cls)) {
            return true;
        } else if (cls == String.class) {
            return true;
        } else if (cls == NetworkTableValue.class) {
            return true;
        }

        return false;
    }

    private static void assertValidNTType(Class<?> cls) {
        if (!isValidNTType(cls)) {
            throw new IllegalCommunicatorException("(return/parameter) type is not one of valid networktables types");
        }
    }

    private static void invokeWithHandling(Consumer<Object> retConsumer, Method method, Object obj, Object... args) {
        tryMakeAccessible(method);
        try {
            retConsumer.accept(method.invoke(obj, (Object[]) args));
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Failed to access method.", e);
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.SEVERE, "Communicator callback threw an exception.", e);
        }
    }

    private static NetworkTableEntry resolveEntry(NetworkTable baseTable, Name prefix, String key, Member member) {
        String[] keyComponents;
        if (key.isEmpty()) {
            keyComponents = new String[] {member.getName()};
        } else {
            keyComponents = decomposeKey(key);
            for (String kc : keyComponents) {
                Name.requireValidName(kc);
            }
        }
        NetworkTable table = baseTable;
        for (int i = 0; i < keyComponents.length - 1; ++i) {
            table = table.getSubTable(keyComponents[i]);
        }
        String entryName = keyComponents[keyComponents.length - 1];
        if (!prefix.equals(Name.UNNAMED)) {
            entryName = prefix + ENTRY_NAME_PREFIX_SEP + entryName;
        }
        return table.getEntry(entryName);
    }

    private static String[] decomposeKey(String key) {
        if (key.startsWith(PATH_SEPARATOR)) {
            key = key.substring(1);
        }
        if (key.endsWith(PATH_SEPARATOR)) {
            key = key.substring(0, key.length() - 1);
        }
        return key.split(PATH_SEPARATOR);
    }

    @Override
    public String getName() {
        return "communication";
    }
}
