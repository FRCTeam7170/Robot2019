package frc.team7170.lib.data.property;

import frc.team7170.lib.data.Value;
import frc.team7170.lib.data.ValueType;

/**
 * <p>
 * A readable {@link Property Property}. This interface provides primitive getters for each of the supported types,
 * which default to throwing {@link UnsupportedOperationException UnsupportedOperationException} with a descriptive
 * error message. Since properties may only have one data type associated with them, exactly <em>one</em> of the
 * {@code getX()} methods should be overridden to return the current value of the underlying source of the readable
 * property; the remaining {@code getX()} methods should be left to error.
 * </p>
 * <p>
 * In the case that this readable property represents dynamic data, {@link RProperty#getPollPeriodMs() getPollPeriodMs}
 * should return a positive integer representing the duration of inactivity between each poll of this readable property
 * in milliseconds. If this readable property otherwise represents static data or data that updates infrequently,
 * {@code getPollPeriodMs} can return a negative integer to indicate that the looper should not periodically poll this
 * property. TODO elaborate...
 * </p>
 * TODO @see the class which manages looping RProperties
 * TODO break first paragraph into implSpec and apiNote
 *
 * @author Robert Russell
 * @see Property
 * @see WProperty
 * @see RWProperty
 * @see PropertyFactory
 */
public interface RProperty extends Property {

    /**
     * Get the value of this property if its type is {@linkplain ValueType#BOOLEAN boolean}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain ValueType#BOOLEAN boolean}.
     * @throws UnsupportedOperationException if the type of this property is not boolean.
     */
    default boolean getBoolean() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.BOOLEAN.name())
        );
    }

    /**
     * Get the value of this property if its type is {@linkplain ValueType#DOUBLE double}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain ValueType#DOUBLE double}.
     * @throws UnsupportedOperationException if the type of this property is not double.
     */
    default double getDouble() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.DOUBLE.name())
        );
    }

    /**
     * Get the value of this property if its type is {@linkplain ValueType#STRING string}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain ValueType#STRING string}.
     * @throws UnsupportedOperationException if the type of this property is not string.
     */
    default String getString() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.STRING.name())
        );
    }

    /**
     * Get the value of this property if its type is {@linkplain ValueType#BOOLEAN_ARRAY boolean array}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain ValueType#BOOLEAN_ARRAY boolean array}.
     * @throws UnsupportedOperationException if the type of this property is not boolean array.
     */
    default boolean[] getBooleanArray() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.BOOLEAN_ARRAY.name())
        );
    }

    /**
     * Get the value of this property if its type is {@linkplain ValueType#DOUBLE_ARRAY double array}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain ValueType#DOUBLE_ARRAY double array}.
     * @throws UnsupportedOperationException if the type of this property is not double array.
     */
    default double[] getDoubleArray() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.DOUBLE_ARRAY.name())
        );
    }

    /**
     * Get the value of this property if its type is {@linkplain ValueType#STRING_ARRAY string array}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain ValueType#STRING_ARRAY string array}.
     * @throws UnsupportedOperationException if the type of this property is not string array.
     */
    default String[] getStringArray() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.STRING_ARRAY.name())
        );
    }

    /**
     * Get the value of this property if its type is {@linkplain ValueType#RAW raw}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain ValueType#RAW raw}.
     * @throws UnsupportedOperationException if the type of this property is not raw.
     */
    default byte[] getRaw() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), ValueType.RAW.name())
        );
    }

    /**
     * Get the value of this property as a {@link Value Value}.
     *
     * @return the value of this property as a {@code Value}.
     */
    default Value getValue() {
        switch (getType()) {
            case BOOLEAN:
                return Value.newBooleanValue(getBoolean());
            case DOUBLE:
                return Value.newDoubleValue(getDouble());
            case STRING:
                return Value.newStringValue(getString());
            case BOOLEAN_ARRAY:
                return Value.newBooleanArrayValue(getBooleanArray());
            case DOUBLE_ARRAY:
                return Value.newDoubleArrayValue(getDoubleArray());
            case STRING_ARRAY:
                return Value.newStringArrayValue(getStringArray());
            case RAW:
                return Value.newRawValue(getRaw());
            default:
                throw new AssertionError();
        }
    }

    /**
     * Get the poll period of this readable property. If the poll period is greater than or equal to zero, it represents
     * the duration of inactivity between each poll of this readable property in milliseconds. If the poll period is
     * negative, it indicates that this readable property should not be periodically polled.
     *
     * @return the poll period of this readable property.
     */
    int getPollPeriodMs();

    @Override
    default boolean isReadable() {
        return true;
    }
}
