package frc.team7170.lib.networktables;

import edu.wpi.first.networktables.*;
import frc.team7170.lib.command.TimedRunnableCommand;
import frc.team7170.lib.util.ReflectUtil;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Communication {

    private static final Logger LOGGER = Logger.getLogger(Communication.class.getName());

    private static final NetworkTableInstance INST = NetworkTableInstance.getDefault();
    private static final String ENTRY_NAME_PREFIX_SEP = "_";

    // Pass null for transmitPrefix for static/singleton
    public static void registerCommunicator(Class<?> cls, String transmitPrefix) {
        registerCommunicator(null, cls, transmitPrefix);
    }

    public static void registerCommunicator(Object obj, String transmitPrefix) {
        registerCommunicator(obj, obj.getClass(), transmitPrefix);
    }

    private static void registerCommunicator(Object obj, Class<?> cls, String prefix) {
        if (prefix != null) {
            assertValidName(prefix);
        }

        ReflectUtil.getMethodAnnotationStream(cls, Transmit.class).forEach(pair -> {
            Method method = pair.getLeft();
            Transmit transmit = pair.getRight();

            // Sanity checks.
            if (method.getAnnotation(Receives.class) != null) {
                throw new IllegalCommunicatorException("a method cannot be a transmitter and a receiver");
            }
            tryMakeAccessible(method);
            doMethodAssertions(obj, method);
            ReflectUtil.assertParameterCount(method, 0);
            assertValidNTType(method.getReturnType());

            NetworkTableEntry entry = INST.getEntry(resolveTransmitEntryName(prefix, transmit.value(), method));
            Runnable runnable = () -> invokeWithHandling(entry::setValue, method, obj);
            if (transmit.pollRateMs() != TransmitFrequency.STATIC) {
                // The scheduler stores an internal reference to this, so it shouldn't be garbage collected.
                new TimedRunnableCommand(runnable, transmit.pollRateMs(), true);
            }
            runnable.run();
        });

        ReflectUtil.getMethodAnnotationStream(cls, Receives.class).forEach(pair -> {
            Method method = pair.getLeft();
            Receives receives = pair.getRight();

            // Sanity checks.
            tryMakeAccessible(method);
            doMethodAssertions(obj, method);
            ReflectUtil.assertParameterCount(method, 1);
            ReflectUtil.assertReturnType(method, void.class);

            for (Receive receive : receives.value()) {
                Consumer<EntryNotification> consumer;
                Consumer<Object> retConsumer = (x) -> {};  // Return type is void!
                Class<?> paramType = method.getParameterTypes()[0];
                if (paramType == EntryNotification.class) {
                    consumer = notification -> invokeWithHandling(retConsumer, method, obj,
                            notification);
                } else if (paramType == NetworkTableValue.class) {
                    consumer = notification -> invokeWithHandling(retConsumer, method, obj,
                            notification.value);
                } else if (isValidNTType(paramType)) {
                    consumer = notification -> invokeWithHandling(retConsumer, method, obj,
                            notification.value.getValue());
                } else {
                    throw new IllegalCommunicatorException("invalid method return type; expected one of: " +
                            "'EntryNotification', 'NetworkTableValue', or any valid networktables type");
                }
                NetworkTableEntry entry = INST.getEntry(resolveTransmitEntryName(prefix, receive.value(), method));
                entry.addListener(consumer, receive.flags());
            }
        });

        ReflectUtil.getFieldAnnotationStream(cls, CommField.class).forEach(pair -> {
            Field field = pair.getLeft();
            CommField commField = pair.getRight();

            // Sanity checks.
            tryMakeAccessible(field);
            doFieldAssertions(obj, field);

            NetworkTableEntry entry = INST.getEntry(resolveTransmitEntryName(prefix, commField.value(), field));
            if (commField.transmit()) {
                Runnable runnable = () -> {
                    try {
                        entry.setValue(field.get(obj));
                    } catch (IllegalAccessException e) {
                        throw new AssertionError("method was confirmed to be accessible");
                    }
                };
                if (commField.pollRateMs() != TransmitFrequency.STATIC) {
                    // The scheduler stores an internal reference to this, so it shouldn't be garbage collected.
                    new TimedRunnableCommand(runnable, commField.pollRateMs(), true);
                }
                runnable.run();
            }
            if (commField.receive()) {
                Consumer<EntryNotification> consumer = notification -> {
                    try {
                        field.set(obj, notification.value.getValue());
                    } catch (IllegalAccessException e) {
                        throw new AssertionError("method was confirmed to be accessible");
                    }
                };
                entry.addListener(consumer, commField.flags());
            }
        });
    }

    private static void doMethodAssertions(Object obj, Method method) {
        assertStaticness(obj, method);
        assertAccessible(obj, method);
        ReflectUtil.assertInvokable(method);
    }

    private static void doFieldAssertions(Object obj, Field field) {
        assertStaticness(obj, field);
        assertAccessible(obj, field);
        assertValidNTType(field.getType());
    }

    private static void tryMakeAccessible(AccessibleObject accessibleObject) {
        try {
            if (!accessibleObject.trySetAccessible()) {
                LOGGER.warning("failed to make object accessible");
            }
        } catch (SecurityException e) {
            LOGGER.log(Level.WARNING, "security exception thrown while attempting to make object accessible", e);
        }
    }

    private static void assertStaticness(Object obj, Member member) {
        if (!((obj != null && !ReflectUtil.isStatic(member)) || (obj == null && ReflectUtil.isStatic(member)))) {
            throw new IllegalCommunicatorException("obj given for static communicator or no obj given for instance communicator");
        }
    }

    private static void assertAccessible(Object obj, AccessibleObject accessibleObject) {
        if (!accessibleObject.canAccess(obj)) {
            throw new IllegalCommunicatorException("member could not be accessed");
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
        }

        return false;
    }

    private static void assertValidNTType(Class<?> cls) {
        if (!isValidNTType(cls)) {
            throw new IllegalCommunicatorException("(return/parameter) type is not one of valid networktables types");
        }
    }

    private static void invokeWithHandling(Consumer<Object> retConsumer, Method method, Object obj, Object... args) {
        try {
            retConsumer.accept(method.invoke(obj, (Object[]) args));
        } catch (IllegalAccessException e) {
            throw new AssertionError("method was confirmed to be accessible");
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.SEVERE, "communicator callback threw an exception", e);
        }
    }

    private static String resolveTransmitEntryName(String prefix, String annoVal, Member member) {
        assertValidName(annoVal);
        String base = annoVal.isEmpty() ? member.getName() : annoVal;
        if (prefix == null) {
            return base;
        }
        return prefix + ENTRY_NAME_PREFIX_SEP + base;
    }

    private static void assertValidName(String name) {
        if (name.matches("[^a-zA-Z0-9_]")) {
            throw new IllegalCommunicatorException("invalid communicator name");
        }
    }
}
