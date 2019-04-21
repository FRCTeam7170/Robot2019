package frc.team7170.lib.data.property;

import frc.team7170.lib.data.Value;
import frc.team7170.lib.data.ValueType;

/**
 * <p>
 * A writable {@link Property Property}. This interface provides primitive setters for each of the supported types,
 * which default to throwing {@link UnsupportedOperationException UnsupportedOperationException} with a descriptive
 * error message. Since properties may only have one data type associated with them, exactly <em>one</em> of the
 * {@code setX()} methods should be overridden to set the current value of the underlying attribute of the writable
 * property; the remaining {@code setX()} methods should be left to error.
 * </p>
 * TODO comment on how set methods should be called on events
 * TODO break first paragraph into implSpec and apiNote
 *
 * @author Robert Russell
 * @see Property
 * @see RProperty
 * @see RWProperty
 * @see PropertyFactory
 */
public interface WProperty extends Property {

    /**
     * Set the value of this property if its type is {@linkplain ValueType#BOOLEAN boolean}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code setX()} methods should be overridden to set the current value of the underlying
     * attribute of the writable property; the remaining {@code setX()} methods should be left to error.
     *
     **  @param value the value to set on the underlying attribute of the writable property.
     * @throws UnsupportedOperationException if the type of this property is not boolean.
     */
    default void setBoolean(boolean value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.BOOLEAN.name())
        );
    }

    /**
     * Set the value of this property if its type is {@linkplain ValueType#DOUBLE double}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code setX()} methods should be overridden to set the current value of the underlying
     * attribute of the writable property; the remaining {@code setX()} methods should be left to error.
     *
     * @param value the value to set on the underlying attribute of the writable property.
     * @throws UnsupportedOperationException if the type of this property is not double.
     */
    default void setDouble(double value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.DOUBLE.name())
        );
    }

    /**
     * Set the value of this property if its type is {@linkplain ValueType#STRING string}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code setX()} methods should be overridden to set the current value of the underlying
     * attribute of the writable property; the remaining {@code setX()} methods should be left to error.
     *
     * @param value the value to set on the underlying attribute of the writable property.
     * @throws UnsupportedOperationException if the type of this property is not string.
     */
    default void setString(String value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.STRING.name())
        );
    }

    /**
     * Set the value of this property if its type is {@linkplain ValueType#BOOLEAN_ARRAY boolean array}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code setX()} methods should be overridden to set the current value of the underlying
     * attribute of the writable property; the remaining {@code setX()} methods should be left to error.
     *
     * @param value the value to set on the underlying attribute of the writable property.
     * @throws UnsupportedOperationException if the type of this property is not boolean array.
     */
    default void setBooleanArray(boolean[] value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.BOOLEAN_ARRAY.name())
        );
    }

    /**
     * Set the value of this property if its type is {@linkplain ValueType#DOUBLE_ARRAY double array}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code setX()} methods should be overridden to set the current value of the underlying
     * attribute of the writable property; the remaining {@code setX()} methods should be left to error.
     *
     * @param value the value to set on the underlying attribute of the writable property.
     * @throws UnsupportedOperationException if the type of this property is not double array.
     */
    default void setDoubleArray(double[] value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.DOUBLE_ARRAY.name())
        );
    }

    /**
     * Set the value of this property if its type is {@linkplain ValueType#STRING_ARRAY string array}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code setX()} methods should be overridden to set the current value of the underlying
     * attribute of the writable property; the remaining {@code setX()} methods should be left to error.
     *
     * @param value the value to set on the underlying attribute of the writable property.
     * @throws UnsupportedOperationException if the type of this property is not string array.
     */
    default void setStringArray(String[] value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.STRING_ARRAY.name())
        );
    }

    /**
     * Set the value of this property if its type is {@linkplain ValueType#RAW raw}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code setX()} methods should be overridden to set the current value of the underlying
     * attribute of the writable property; the remaining {@code setX()} methods should be left to error.
     *
     * @param value the value to set on the underlying attribute of the writable property.
     * @throws UnsupportedOperationException if the type of this property is not raw.
     */
    default void setRaw(byte[] value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.RAW.name())
        );
    }

    /**
     * Set the value of this property via a {@link Value Value} object.
     *
     * @param value the {@code Value} to set on the underlying attribute of the writable property.
     * @throws IllegalArgumentException if the give {@code Value} does not have the same {@linkplain ValueType type} as
     * this property.
     */
    default void setValue(Value value) {
        if (!value.getType().equals(getType())) {
            throw new IllegalArgumentException(
                    String.format("cannot set property of type %s to value of type %s", getType(), value.getType())
            );
        }
        switch (getType()) {
            case BOOLEAN:
                setBoolean(value.getBoolean());
                break;
            case DOUBLE:
                setDouble(value.getDouble());
                break;
            case STRING:
                setString(value.getString());
                break;
            case BOOLEAN_ARRAY:
                setBooleanArray(value.getBooleanArray());
                break;
            case DOUBLE_ARRAY:
                setDoubleArray(value.getDoubleArray());
                break;
            case STRING_ARRAY:
                setStringArray(value.getStringArray());
                break;
            case RAW:
                setRaw(value.getRaw());
                break;
            default:
                throw new AssertionError();
        }
    }

    @Override
    default boolean isWritable() {
        return true;
    }
}
