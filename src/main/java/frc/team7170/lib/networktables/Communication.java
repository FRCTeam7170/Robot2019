package frc.team7170.lib.networktables;

import edu.wpi.first.networktables.*;
import frc.team7170.lib.command.TimedRunnableCommand;
import frc.team7170.lib.util.ReflectUtil;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.function.Consumer;


public class Communication {

    private static final NetworkTableInstance INST = NetworkTableInstance.getDefault();
    private static final String ENTRY_NAME_PREFIX_SEP = "_";

    // TODO: allow ignoring transmit prefix for singleton/static transmitters

    public static void registerCommunicator(Class<?> cls, String transmitPrefix) {
        registerCommunicator(null, cls, transmitPrefix);
    }

    public static void registerCommunicator(Object obj, String transmitPrefix) {
        registerCommunicator(obj, obj.getClass(), transmitPrefix);
    }

    private static void registerCommunicator(Object obj, Class<?> cls, String prefix) {
        ReflectUtil.getMethodAnnotationStream(cls, Transmit.class).forEach(pair -> {
            Method method = pair.getLeft();
            Transmit transmit = pair.getRight();

            tryMakeAccessible(method);
            doMethodAssertions(obj, method);
            NetworkTableEntry entry = INST.getEntry(resolveTransmitEntryName(prefix, transmit.value(), method));
            Runnable runnable = () -> invokeWithHandling(entry::setValue, method, obj);
            if (transmit.pollRateMs() != TransmitFrequency.STATIC) {
                // The scheduler stores an internal reference to this, so it shouldn't be garbage collected.
                new TimedRunnableCommand(runnable, transmit.pollRateMs(), true);
            }
            runnable.run();
        });

        ReflectUtil.getFieldAnnotationStream(cls, Transmit.class).forEach(pair -> {
            Field field = pair.getLeft();
            Transmit transmit = pair.getRight();

            tryMakeAccessible(field);
            doFieldAssertions(obj, field);
            NetworkTableEntry entry = INST.getEntry(resolveTransmitEntryName(prefix, transmit.value(), field));
        });
    }

    private static void doMethodAssertions(Object obj, Method method) {
        assertStaticness(obj, method);
        assertAccessible(obj, method);
        ReflectUtil.assertInvokable(method);
        ReflectUtil.assertParameterCount(method, 0);
        assertValidNTType(method.getReturnType());
    }

    private static void doFieldAssertions(Object obj, Field field) {
        assertStaticness(obj, field);
        assertAccessible(obj, field);
        assertValidNTType(field.getType());
    }

    private static void tryMakeAccessible(AccessibleObject accessibleObject) {
        try {
            if (!accessibleObject.trySetAccessible()) {
                System.out.println("Uh-oh");  // TODO: real logging
            }
        } catch (SecurityException e) {
            System.out.println("Uh-oh");  // TODO: real logging
        }
    }

    private static void assertStaticness(Object obj, Member member) {
        if (!((obj != null && !ReflectUtil.isStatic(member)) || (obj == null && ReflectUtil.isStatic(member)))) {
            throw new IllegalArgumentException("obj given for static communicator or no obj given for instance communicator");
        }
    }

    private static void assertAccessible(Object obj, AccessibleObject accessibleObject) {
        if (!accessibleObject.canAccess(obj)) {
            throw new IllegalArgumentException("member could not be accessed");
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

    private static void assertValidNTType(Class<?> cls) {
        if (cls.isArray()) {
            cls = cls.getComponentType();
        }

        if ((cls == boolean.class) || (cls == Boolean.class)) {
            return;
        } else if (Number.class.isAssignableFrom(cls) || PRIMITIVE_NUMBER_TYPES.contains(cls)) {
            return;
        } else if (cls == String.class) {
            return;
        }

        throw new IllegalArgumentException("method return type/member type is not one of valid networktables types");
    }

    private static void invokeWithHandling(Consumer<Object> consumer, Method method, Object obj) {
        try {
            consumer.accept(method.invoke(obj));
        } catch (IllegalAccessException e) {
            throw new AssertionError("method was confirmed to be accessible");
        } catch (InvocationTargetException e) {
            System.out.println("Uh-oh!");  // TODO: real logging
        }
    }

    private static String resolveTransmitEntryName(String prefix, String annoVal, Member member) {
        return prefix + ENTRY_NAME_PREFIX_SEP + (annoVal.isEmpty() ? member.getName() : annoVal);
    }
}
