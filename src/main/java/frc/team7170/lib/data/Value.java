package frc.team7170.lib.data;

import java.util.Arrays;

/**
 * <p>
 * A immutable value of one of the types in {@link ValueType ValueType}.
 * </p>
 * <p>
 * {@code Value}s support {@linkplain Value#equals(Object) equality testing}, {@linkplain Value#hashCode() hashing}, and
 * {@linkplain Value#toString() string conversion} based on their underlying value.
 * </p>
 * <p>
 * {@code Value}s are constructed using one of the static factory methods; there is one for each
 * {@link ValueType ValueType}.
 * </p>
 *
 * @apiNote This class's getters for the basic types have primitive return types to eliminate autoboxing.
 *
 * @implSpec All getters default to throwing {@link UnsupportedOperationException UnsupportedOperationException} with a
 * descriptive error message. Since {@code Value}s may only have one data type associated with them, exactly
 * <em>one</em> of the {@code getX()} methods should be overridden to return the current value; the remaining
 * {@code getX()} methods should be left to error.
 *
 * @author Robert Russell
 * @see ValueType
 */
// TODO: what if non-primitive Values are constructed with null?
public abstract class Value {

    private final ValueType type;

    /**
     * @apiNote Single private constructor to ensure instantiation must occur through the provided static factory
     * methods.
     *
     * @param type the type of this {@code Value}.
     */
    private Value(ValueType type) {
        this.type = type;
    }

    /**
     * Get the type of this {@code Value}.
     *
     * @return the type of this {@code Value}.
     */
    public final ValueType getType() {
        return type;
    }

    /**
     * Get the underlying value of this {@code Value} if its type is {@linkplain ValueType#BOOLEAN boolean}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since {@code Value}s may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value; the remaining
     * {@code getX()} methods should be left to error.
     *
     * @return the underlying value of this {@code Value} if its type is {@linkplain ValueType#BOOLEAN boolean}.
     * @throws UnsupportedOperationException if the type of this {@code Value} is not boolean.
     */
    public boolean getBoolean() {
        throw new UnsupportedOperationException(
                String.format("this value is of type %s, not %s", type.name(), ValueType.BOOLEAN.name())
        );
    }

    /**
     * Get the underlying value of this {@code Value} if its type is {@linkplain ValueType#DOUBLE double}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since {@code Value}s may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value; the remaining
     * {@code getX()} methods should be left to error.
     *
     * @return the underlying value of this {@code Value} if its type is {@linkplain ValueType#DOUBLE double}.
     * @throws UnsupportedOperationException if the type of this {@code Value} is not double.
     */
    public double getDouble() {
        throw new UnsupportedOperationException(
                String.format("this value is of type %s, not %s", type.name(), ValueType.DOUBLE.name())
        );
    }

    /**
     * Get the underlying value of this {@code Value} if its type is {@linkplain ValueType#STRING string}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since {@code Value}s may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value; the remaining
     * {@code getX()} methods should be left to error.
     *
     * @return the underlying value of this {@code Value} if its type is {@linkplain ValueType#STRING string}.
     * @throws UnsupportedOperationException if the type of this {@code Value} is not string.
     */
    public String getString() {
        throw new UnsupportedOperationException(
                String.format("this value is of type %s, not %s", type.name(), ValueType.STRING.name())
        );
    }

    /**
     * Get the underlying value of this {@code Value} if its type is {@linkplain ValueType#BOOLEAN_ARRAY boolean array}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since {@code Value}s may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value; the remaining
     * {@code getX()} methods should be left to error.
     *
     * @return the underlying value of this {@code Value} if its type is
     * {@linkplain ValueType#BOOLEAN_ARRAY boolean array}.
     * @throws UnsupportedOperationException if the type of this {@code Value} is not boolean array.
     */
    public boolean[] getBooleanArray() {
        throw new UnsupportedOperationException(
                String.format("this value is of type %s, not %s", type.name(), ValueType.BOOLEAN_ARRAY.name())
        );
    }

    /**
     * Get the underlying value of this {@code Value} if its type is {@linkplain ValueType#DOUBLE_ARRAY double array}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since {@code Value}s may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value; the remaining
     * {@code getX()} methods should be left to error.
     *
     * @return the underlying value of this {@code Value} if its type is
     * {@linkplain ValueType#DOUBLE_ARRAY double array}.
     * @throws UnsupportedOperationException if the type of this {@code Value} is not double array.
     */
    public double[] getDoubleArray() {
        throw new UnsupportedOperationException(
                String.format("this value is of type %s, not %s", type.name(), ValueType.DOUBLE_ARRAY.name())
        );
    }

    /**
     * Get the underlying value of this {@code Value} if its type is {@linkplain ValueType#STRING_ARRAY string array}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since {@code Value}s may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value; the remaining
     * {@code getX()} methods should be left to error.
     *
     * @return the underlying value of this {@code Value} if its type is
     * {@linkplain ValueType#STRING_ARRAY string array}.
     * @throws UnsupportedOperationException if the type of this {@code Value} is not string array.
     */
    public String[] getStringArray() {
        throw new UnsupportedOperationException(
                String.format("this value is of type %s, not %s", type.name(), ValueType.STRING_ARRAY.name())
        );
    }

    /**
     * Get the underlying value of this {@code Value} if its type is {@linkplain ValueType#RAW raw}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since {@code Value}s may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value; the remaining
     * {@code getX()} methods should be left to error.
     *
     * @return the underlying value of this {@code Value} if its type is {@linkplain ValueType#RAW raw}.
     * @throws UnsupportedOperationException if the type of this {@code Value} is not raw.
     */
    public byte[] getRaw() {
        throw new UnsupportedOperationException(
                String.format("this value is of type %s, not %s", type.name(), ValueType.RAW.name())
        );
    }

    /**
     * <p>
     * Return whether this {@code Value} and the given {@code Object}, {@code obj}, are equal.
     * </p>
     * <p>
     * If {@code obj instanceof Value}, then {@code obj} and this {@code Value} are equal if and only if they share the
     * same {@linkplain ValueType type} and their underlying values are equal. Otherwise, {@code obj} and this
     * {@code Value} are considered equal if and only if {@code obj} is equal to the underlying value of this
     * {@code Value}. If {@code obj == null}, this method always returns false.
     * </p>
     * <p>
     * A given non-{@code Value} {@code Object} and the underlying value for a {@code Value} are considered equal if
     * they are of the exact same Java type--or if {@code obj} is a boxed equivalent of this {@code Value}'s underlying
     * value--and they are equal according to that specific Java type's equality rules. Note that the aforementioned
     * "boxed equivalence" rule also applies for array types; for example, if this {@code Value} is of type
     * {@linkplain ValueType#RAW raw}, then {@code obj} (where {@code !(obj instanceof Value)}) must be of type
     * {@code byte[]} <em>or</em> {@code Byte[]} in order to be comparable to the underlying value of this
     * {@code Value}. If this {@code Value} is an array type and matches the type of the given {@code Object} as
     * described above, then this {@code Value} and the given {@code Object} are considered equal if all elements of the
     * arrays are equal. If this {@code Value} is an array type, and if {@code obj} is the corresponding boxed array
     * type and contains any {@code null} elements, the equality check fails.
     * </p>
     *
     * @param obj the {@code Object} to test for equality with.
     * @return whether or not this {@code Value} is equal to the given {@code Object}.
     */
    @Override
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Value) {
            Value other = (Value) obj;
            ValueType otherType = other.getType();
            if (!otherType.equals(type)) {
                return false;
            }
            switch (otherType) {
                // Note: the three scalar primitive cases perform autoboxing.
                case BOOLEAN:
                    return underlyingValueEqual(other.getBoolean());
                case DOUBLE:
                    return underlyingValueEqual(other.getDouble());
                case STRING:
                    return underlyingValueEqual(other.getString());
                case BOOLEAN_ARRAY:
                    return underlyingValueEqual(other.getBooleanArray());
                case DOUBLE_ARRAY:
                    return underlyingValueEqual(other.getDoubleArray());
                case STRING_ARRAY:
                    return underlyingValueEqual(other.getStringArray());
                case RAW:
                    return underlyingValueEqual(other.getRaw());
            }
        }
        return underlyingValueEqual(obj);
    }

    /**
     * Determine if the underlying value of this {@code Value} is equal to the given {@code Object} as described in
     * {@link Value#equals(Object) equals}.
     *
     * @param obj the {@code Object} to test for equality with.
     * @return whether or not the underlying value of this {@code Value} is equal to the given {@code Object}.
     */
    protected abstract boolean underlyingValueEqual(Object obj);

    /**
     * Return a hash code based on the underlying value of this {@code Value}.
     *
     * @return a hash code based on the underlying value of this {@code Value}.
     */
    @Override
    public abstract int hashCode();

    /**
     * Return a {@code String} representation of the underlying value of this {@code Value}.
     *
     * @return a {@code String} representation of the underlying value of this {@code Value}.
     */
    @Override
    public abstract String toString();

    /**
     * Construct a new {@code Value} of type {@linkplain ValueType#BOOLEAN boolean}.
     *
     * @param value the underlying value.
     * @return the newly-constructed {@code Value}.
     */
    public static Value newBooleanValue(boolean value) {
        return new Value(ValueType.BOOLEAN) {
            @Override
            public boolean getBoolean() {
                return value;
            }

            @Override
            protected boolean underlyingValueEqual(Object obj) {
                return (obj instanceof Boolean) && ((Boolean) obj) == value;
            }

            @Override
            public int hashCode() {
                return Boolean.hashCode(value);
            }

            @Override
            public String toString() {
                return Boolean.toString(value);
            }
        };
    }

    /**
     * Construct a new {@code Value} of type {@linkplain ValueType#DOUBLE double}.
     *
     * @param value the underlying value.
     * @return the newly-constructed {@code Value}.
     */
    public static Value newDoubleValue(double value) {
        return new Value(ValueType.DOUBLE) {
            @Override
            public double getDouble() {
                return value;
            }

            @Override
            protected boolean underlyingValueEqual(Object obj) {
                return (obj instanceof Double) && ((Double) obj) == value;
            }

            @Override
            public int hashCode() {
                return Double.hashCode(value);
            }

            @Override
            public String toString() {
                return Double.toString(value);
            }
        };
    }

    /**
     * Construct a new {@code Value} of type {@linkplain ValueType#STRING string}.
     *
     * @param value the underlying value.
     * @return the newly-constructed {@code Value}.
     */
    public static Value newStringValue(String value) {
        return new Value(ValueType.STRING) {
            @Override
            public String getString() {
                return value;
            }

            @Override
            protected boolean underlyingValueEqual(Object obj) {
                return (obj instanceof String) && value.equals(obj);
            }

            @Override
            public int hashCode() {
                return value.hashCode();
            }

            @Override
            public String toString() {
                return value;
            }
        };
    }

    /**
     * Construct a new {@code Value} of type {@linkplain ValueType#BOOLEAN_ARRAY boolean array}.
     *
     * @param value the underlying value.
     * @return the newly-constructed {@code Value}.
     */
    public static Value newBooleanArrayValue(boolean[] value) {
        return new Value(ValueType.BOOLEAN_ARRAY) {
            @Override
            public boolean[] getBooleanArray() {
                return value;
            }

            @Override
            @SuppressWarnings("Duplicates")
            protected boolean underlyingValueEqual(Object obj) {
                if (obj instanceof boolean[]) {
                    return Arrays.equals(value, (boolean[]) obj);
                } else if (obj instanceof Boolean[]) {
                    Boolean[] booleans = (Boolean[]) obj;
                    if (booleans.length != value.length) {
                        return false;
                    }
                    for (int i = 0; i < booleans.length; ++i) {
                        if ((booleans[i] == null) || (value[i] != booleans[i])) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }

            @Override
            public int hashCode() {
                return Arrays.hashCode(value);
            }

            @Override
            public String toString() {
                return Arrays.toString(value);
            }
        };
    }

    /**
     * Construct a new {@code Value} of type {@linkplain ValueType#DOUBLE_ARRAY double array}.
     *
     * @param value the underlying value.
     * @return the newly-constructed {@code Value}.
     */
    public static Value newDoubleArrayValue(double[] value) {
        return new Value(ValueType.DOUBLE_ARRAY) {
            @Override
            public double[] getDoubleArray() {
                return value;
            }

            @Override
            @SuppressWarnings("Duplicates")
            protected boolean underlyingValueEqual(Object obj) {
                if (obj instanceof double[]) {
                    return Arrays.equals(value, (double[]) obj);
                } else if (obj instanceof Double[]) {
                    Double[] doubles = (Double[]) obj;
                    if (doubles.length != value.length) {
                        return false;
                    }
                    for (int i = 0; i < doubles.length; ++i) {
                        if ((doubles[i] == null) || (value[i] != doubles[i])) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }

            @Override
            public int hashCode() {
                return Arrays.hashCode(value);
            }

            @Override
            public String toString() {
                return Arrays.toString(value);
            }
        };
    }

    /**
     * Construct a new {@code Value} of type {@linkplain ValueType#STRING_ARRAY string array}.
     *
     * @param value the underlying value.
     * @return the newly-constructed {@code Value}.
     */
    public static Value newStringArrayValue(String[] value) {
        return new Value(ValueType.STRING_ARRAY) {
            @Override
            public String[] getStringArray() {
                return value;
            }

            @Override
            protected boolean underlyingValueEqual(Object obj) {
                if (obj instanceof String[]) {
                    return Arrays.equals(value, (String[]) obj);
                }
                return false;
            }

            @Override
            public int hashCode() {
                return Arrays.hashCode(value);
            }

            @Override
            public String toString() {
                return Arrays.toString(value);
            }
        };
    }

    /**
     * Construct a new {@code Value} of type {@linkplain ValueType#RAW raw}.
     *
     * @param value the underlying value.
     * @return the newly-constructed {@code Value}.
     */
    public static Value newRawValue(byte[] value) {
        return new Value(ValueType.RAW) {
            @Override
            public byte[] getRaw() {
                return value;
            }

            @Override
            @SuppressWarnings("Duplicates")
            protected boolean underlyingValueEqual(Object obj) {
                if (obj instanceof byte[]) {
                    return Arrays.equals(value, (byte[]) obj);
                } else if (obj instanceof Byte[]) {
                    Byte[] bytes = (Byte[]) obj;
                    if (bytes.length != value.length) {
                        return false;
                    }
                    for (int i = 0; i < bytes.length; ++i) {
                        if ((bytes[i] == null) || (value[i] != bytes[i])) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }

            @Override
            public int hashCode() {
                return Arrays.hashCode(value);
            }

            @Override
            public String toString() {
                return Arrays.toString(value);
            }
        };
    }
}
