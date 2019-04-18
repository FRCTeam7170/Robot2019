package frc.team7170.lib.data.property;

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
 *
 * @author Robert Russell
 * @see Property
 * @see WProperty
 * @see RWProperty
 * @see PropertyFactory
 */
public interface RProperty extends Property {

    /**
     * Get the value of this property if its type is {@linkplain PropertyType#BOOLEAN boolean}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain PropertyType#BOOLEAN boolean}.
     * @throws UnsupportedOperationException if the type of this property is not boolean.
     */
    default boolean getBoolean() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.BOOLEAN.name())
        );
    }

    /**
     * Get the value of this property if its type is {@linkplain PropertyType#DOUBLE double}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain PropertyType#DOUBLE double}.
     * @throws UnsupportedOperationException if the type of this property is not double.
     */
    default double getDouble() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.DOUBLE.name())
        );
    }

    /**
     * Get the value of this property if its type is {@linkplain PropertyType#STRING string}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain PropertyType#STRING string}.
     * @throws UnsupportedOperationException if the type of this property is not string.
     */
    default String getString() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.STRING.name())
        );
    }

    /**
     * Get the value of this property if its type is {@linkplain PropertyType#BOOLEAN_ARRAY boolean array}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain PropertyType#BOOLEAN_ARRAY boolean array}.
     * @throws UnsupportedOperationException if the type of this property is not boolean array.
     */
    default boolean[] getBooleanArray() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.BOOLEAN_ARRAY.name())
        );
    }

    /**
     * Get the value of this property if its type is {@linkplain PropertyType#DOUBLE_ARRAY double array}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain PropertyType#DOUBLE_ARRAY double array}.
     * @throws UnsupportedOperationException if the type of this property is not double array.
     */
    default double[] getDoubleArray() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.DOUBLE_ARRAY.name())
        );
    }

    /**
     * Get the value of this property if its type is {@linkplain PropertyType#STRING_ARRAY string array}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain PropertyType#STRING_ARRAY string array}.
     * @throws UnsupportedOperationException if the type of this property is not string array.
     */
    default String[] getStringArray() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.STRING_ARRAY.name())
        );
    }

    /**
     * Get the value of this property if its type is {@linkplain PropertyType#RAW raw}.
     *
     * @implSpec The default implementation throws {@link UnsupportedOperationException UnsupportedOperationException}
     * with a descriptive error message. Since properties may only have one data type associated with them, exactly
     * <em>one</em> of the {@code getX()} methods should be overridden to return the current value of the underlying
     * source of the readable property; the remaining {@code getX()} methods should be left to error.
     *
     * @return the value of this property if its type is {@linkplain PropertyType#RAW raw}.
     * @throws UnsupportedOperationException if the type of this property is not raw.
     */
    default byte[] getRaw() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.RAW.name())
        );
    }

    /**
     * Get the poll period of this readable property. If the poll period is greater than or equal to zero, it represents
     * the duration of inactivity between each poll of this readable property in milliseconds. If the poll period is
     * negative, it indicates that this readable property should not be periodically polled.
     * @return the poll period of this readable property.
     */
    int getPollPeriodMs();

    @Override
    default boolean isReadable() {
        return true;
    }
}
