package frc.team7170.lib.data.property;

import frc.team7170.lib.BooleanConsumer;
import frc.team7170.lib.data.ValueType;

import java.util.Objects;
import java.util.function.*;

/**
 * A collection of static methods for creating {@linkplain Property properties}. All the static factory methods
 * provided here use {@linkplain Supplier suppliers} and {@linkplain Consumer consumers} for the getters and setter of
 * readable and writeable properties, respectively. The primitive {@linkplain ValueType property types} use
 * non-generic supplier/consumer functional interfaces to avoid auto(un)boxing.
 *
 * @author Robert Russell
 * @see Property
 * @see RProperty
 * @see WProperty
 * @see RWProperty
 */
public final class PropertyFactory {

    // Enforce non-instantiability.
    private PropertyFactory() {}

    /**
     * Construct a new {@link RProperty RProperty} of type {@link ValueType#BOOLEAN boolean}.
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for this readable property.
     * @return the new readable property.
     * @throws NullPointerException if either of {@code name} or {@code getter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RProperty newBooleanRProperty(String name, int pollPeriodMs, BooleanSupplier getter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        return new BaseRProperty(name, ValueType.BOOLEAN, pollPeriodMs) {
            @Override
            public boolean getBoolean() {
                return getter.getAsBoolean();
            }
        };
    }

    /**
     * Construct a new {@link WProperty WProperty} of type {@link ValueType#BOOLEAN boolean}.
     *
     * @param name the name of the property.
     * @param setter the underlying value consumer for this writable property.
     * @return the new writable property.
     * @throws NullPointerException if either of {@code name} or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static WProperty newBooleanWProperty(String name, BooleanConsumer setter) {
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseWProperty(name, ValueType.BOOLEAN) {
            @Override
            public void setBoolean(boolean value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RWProperty RWProperty} of type {@link ValueType#BOOLEAN boolean}.
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for the readable component of this property.
     * @param setter the underlying value consumer for the writable component of this property.
     * @return the new readable and writeable property.
     * @throws NullPointerException if either of {@code name}, {@code getter}, or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RWProperty newBooleanRWProperty(String name, int pollPeriodMs,
                                                  BooleanSupplier getter, BooleanConsumer setter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseRWProperty(name, ValueType.BOOLEAN, pollPeriodMs) {
            @Override
            public boolean getBoolean() {
                return getter.getAsBoolean();
            }

            @Override
            public void setBoolean(boolean value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RProperty RProperty} of type {@link ValueType#DOUBLE double}.
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for this readable property.
     * @return the new readable property.
     * @throws NullPointerException if either of {@code name} or {@code getter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RProperty newDoubleRProperty(String name, int pollPeriodMs, DoubleSupplier getter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        return new BaseRProperty(name, ValueType.DOUBLE, pollPeriodMs) {
            @Override
            public double getDouble() {
                return getter.getAsDouble();
            }
        };
    }

    /**
     * Construct a new {@link WProperty WProperty} of type {@link ValueType#DOUBLE double}.
     *
     * @param name the name of the property.
     * @param setter the underlying value consumer for this writable property.
     * @return the new writable property.
     * @throws NullPointerException if either of {@code name} or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static WProperty newDoubleWProperty(String name, DoubleConsumer setter) {
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseWProperty(name, ValueType.DOUBLE) {
            @Override
            public void setDouble(double value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RWProperty RWProperty} of type {@link ValueType#DOUBLE double}.
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for the readable component of this property.
     * @param setter the underlying value consumer for the writable component of this property.
     * @return the new readable and writeable property.
     * @throws NullPointerException if either of {@code name}, {@code getter}, or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RWProperty newDoubleRWProperty(String name, int pollPeriodMs,
                                                 DoubleSupplier getter, DoubleConsumer setter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseRWProperty(name, ValueType.DOUBLE, pollPeriodMs) {
            @Override
            public double getDouble() {
                return getter.getAsDouble();
            }

            @Override
            public void setDouble(double value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RProperty RProperty} of type {@link ValueType#STRING String}.
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for this readable property.
     * @return the new readable property.
     * @throws NullPointerException if either of {@code name} or {@code getter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RProperty newStringRProperty(String name, int pollPeriodMs, Supplier<String> getter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        return new BaseRProperty(name, ValueType.STRING, pollPeriodMs) {
            @Override
            public String getString() {
                return getter.get();
            }
        };
    }

    /**
     * Construct a new {@link WProperty WProperty} of type {@link ValueType#STRING String}.
     *
     * @param name the name of the property.
     * @param setter the underlying value consumer for this writable property.
     * @return the new writable property.
     * @throws NullPointerException if either of {@code name} or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static WProperty newStringWProperty(String name, Consumer<String> setter) {
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseWProperty(name, ValueType.STRING) {
            @Override
            public void setString(String value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RWProperty RWProperty} of type {@link ValueType#STRING String}.
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for the readable component of this property.
     * @param setter the underlying value consumer for the writable component of this property.
     * @return the new readable and writeable property.
     * @throws NullPointerException if either of {@code name}, {@code getter}, or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RWProperty newStringRWProperty(String name, int pollPeriodMs,
                                                 Supplier<String> getter, Consumer<String> setter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseRWProperty(name, ValueType.STRING, pollPeriodMs) {
            @Override
            public String getString() {
                return getter.get();
            }

            @Override
            public void setString(String value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RProperty RProperty} of type {@link ValueType#BOOLEAN_ARRAY boolean[]}.
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for this readable property.
     * @return the new readable property.
     * @throws NullPointerException if either of {@code name} or {@code getter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RProperty newBooleanArrayRProperty(String name, int pollPeriodMs, Supplier<boolean[]> getter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        return new BaseRProperty(name, ValueType.BOOLEAN_ARRAY, pollPeriodMs) {
            @Override
            public boolean[] getBooleanArray() {
                return getter.get();
            }
        };
    }

    /**
     * Construct a new {@link WProperty WProperty} of type {@link ValueType#BOOLEAN_ARRAY boolean[]}.
     *
     * @param name the name of the property.
     * @param setter the underlying value consumer for this writable property.
     * @return the new writable property.
     * @throws NullPointerException if either of {@code name} or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static WProperty newBooleanArrayWProperty(String name, Consumer<boolean[]> setter) {
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseWProperty(name, ValueType.BOOLEAN_ARRAY) {
            @Override
            public void setBooleanArray(boolean[] value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RWProperty RWProperty} of type {@link ValueType#BOOLEAN_ARRAY boolean[]}.
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for the readable component of this property.
     * @param setter the underlying value consumer for the writable component of this property.
     * @return the new readable and writeable property.
     * @throws NullPointerException if either of {@code name}, {@code getter}, or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RWProperty newBooleanArrayRWProperty(String name, int pollPeriodMs,
                                                       Supplier<boolean[]> getter, Consumer<boolean[]> setter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseRWProperty(name, ValueType.BOOLEAN_ARRAY, pollPeriodMs) {
            @Override
            public boolean[] getBooleanArray() {
                return getter.get();
            }

            @Override
            public void setBooleanArray(boolean[] value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RProperty RProperty} of type {@link ValueType#DOUBLE_ARRAY double[]}.
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for this readable property.
     * @return the new readable property.
     * @throws NullPointerException if either of {@code name} or {@code getter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RProperty newDoubleArrayRProperty(String name, int pollPeriodMs, Supplier<double[]> getter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        return new BaseRProperty(name, ValueType.DOUBLE_ARRAY, pollPeriodMs) {
            @Override
            public double[] getDoubleArray() {
                return getter.get();
            }
        };
    }

    /**
     * Construct a new {@link WProperty WProperty} of type {@link ValueType#DOUBLE_ARRAY double[]}.
     *
     * @param name the name of the property.
     * @param setter the underlying value consumer for this writable property.
     * @return the new writable property.
     * @throws NullPointerException if either of {@code name} or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static WProperty newDoubleArrayWProperty(String name, Consumer<double[]> setter) {
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseWProperty(name, ValueType.DOUBLE_ARRAY) {
            @Override
            public void setDoubleArray(double[] value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RWProperty RWProperty} of type {@link ValueType#DOUBLE_ARRAY double[]}.
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for the readable component of this property.
     * @param setter the underlying value consumer for the writable component of this property.
     * @return the new readable and writeable property.
     * @throws NullPointerException if either of {@code name}, {@code getter}, or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RWProperty newDoubleArrayRWProperty(String name, int pollPeriodMs,
                                                      Supplier<double[]> getter, Consumer<double[]> setter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseRWProperty(name, ValueType.DOUBLE_ARRAY, pollPeriodMs) {
            @Override
            public double[] getDoubleArray() {
                return getter.get();
            }

            @Override
            public void setDoubleArray(double[] value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RProperty RProperty} of type {@link ValueType#STRING_ARRAY String[]}.
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for this readable property.
     * @return the new readable property.
     * @throws NullPointerException if either of {@code name} or {@code getter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RProperty newStringArrayRProperty(String name, int pollPeriodMs, Supplier<String[]> getter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        return new BaseRProperty(name, ValueType.STRING_ARRAY, pollPeriodMs) {
            @Override
            public String[] getStringArray() {
                return getter.get();
            }
        };
    }

    /**
     * Construct a new {@link WProperty WProperty} of type {@link ValueType#STRING_ARRAY String[]}.
     *
     * @param name the name of the property.
     * @param setter the underlying value consumer for this writable property.
     * @return the new writable property.
     * @throws NullPointerException if either of {@code name} or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static WProperty newStringArrayWProperty(String name, Consumer<String[]> setter) {
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseWProperty(name, ValueType.STRING_ARRAY) {
            @Override
            public void setStringArray(String[] value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RWProperty RWProperty} of type {@link ValueType#STRING_ARRAY String[]}.
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for the readable component of this property.
     * @param setter the underlying value consumer for the writable component of this property.
     * @return the new readable and writeable property.
     * @throws NullPointerException if either of {@code name}, {@code getter}, or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RWProperty newStringArrayRWProperty(String name, int pollPeriodMs,
                                                      Supplier<String[]> getter, Consumer<String[]> setter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseRWProperty(name, ValueType.STRING_ARRAY, pollPeriodMs) {
            @Override
            public String[] getStringArray() {
                return getter.get();
            }

            @Override
            public void setStringArray(String[] value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RProperty RProperty} of type {@linkplain ValueType#RAW raw} ({@code byte[]}).
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for this readable property.
     * @return the new readable property.
     * @throws NullPointerException if either of {@code name} or {@code getter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RProperty newRawRProperty(String name, int pollPeriodMs, Supplier<byte[]> getter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        return new BaseRProperty(name, ValueType.RAW, pollPeriodMs) {
            @Override
            public byte[] getRaw() {
                return getter.get();
            }
        };
    }

    /**
     * Construct a new {@link WProperty WProperty} of type {@linkplain ValueType#RAW raw} ({@code byte[]}).
     *
     * @param name the name of the property.
     * @param setter the underlying value consumer for this writable property.
     * @return the new writable property.
     * @throws NullPointerException if either of {@code name} or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static WProperty newRawWProperty(String name, Consumer<byte[]> setter) {
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseWProperty(name, ValueType.RAW) {
            @Override
            public void setRaw(byte[] value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Construct a new {@link RWProperty RWProperty} of type {@linkplain ValueType#RAW raw} ({@code byte[]}).
     *
     * @param name the name of the property.
     * @param pollPeriodMs the {@linkplain RProperty#getPollPeriodMs() poll period} of the property in milliseconds.
     * @param getter the underlying value supplier for the readable component of this property.
     * @param setter the underlying value consumer for the writable component of this property.
     * @return the new readable and writeable property.
     * @throws NullPointerException if either of {@code name}, {@code getter}, or {@code setter} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public static RWProperty newRawRWProperty(String name, int pollPeriodMs,
                                              Supplier<byte[]> getter, Consumer<byte[]> setter) {
        Objects.requireNonNull(getter, "readable property getter must be non-null");
        Objects.requireNonNull(setter, "writable property setter must be non-null");
        return new BaseRWProperty(name, ValueType.RAW, pollPeriodMs) {
            @Override
            public byte[] getRaw() {
                return getter.get();
            }

            @Override
            public void setRaw(byte[] value) {
                setter.accept(value);
            }
        };
    }
}
