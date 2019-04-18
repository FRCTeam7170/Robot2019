package frc.team7170.lib.data.property;

/**
 * <p>
 * A writable {@link Property Property}. This interface provides primitive setters for each of the supported types,
 * which default to throwing {@link UnsupportedOperationException UnsupportedOperationException} with a descriptive
 * error message. Since properties may only have one data type associated with them, exactly <em>one</em> of the
 * {@code setX()} methods should be overridden to set the current value of the underlying attribute of the writable
 * property; the remaining {@code setX()} methods should be left to error.
 * </p>
 * TODO comment on how set methods should be called on events
 *
 * @author Robert Russell
 * @see Property
 * @see RProperty
 * @see RWProperty
 * @see PropertyFactory
 */
public interface WProperty extends Property {

    /**
     * Set the value of this property if its type is {@linkplain PropertyType#BOOLEAN boolean}.
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
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.BOOLEAN.name())
        );
    }

    /**
     * Set the value of this property if its type is {@linkplain PropertyType#DOUBLE double}.
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
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.DOUBLE.name())
        );
    }

    /**
     * Set the value of this property if its type is {@linkplain PropertyType#STRING string}.
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
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.STRING.name())
        );
    }

    /**
     * Set the value of this property if its type is {@linkplain PropertyType#BOOLEAN_ARRAY boolean array}.
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
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.BOOLEAN_ARRAY.name())
        );
    }

    /**
     * Set the value of this property if its type is {@linkplain PropertyType#DOUBLE_ARRAY double array}.
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
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.DOUBLE_ARRAY.name())
        );
    }

    /**
     * Set the value of this property if its type is {@linkplain PropertyType#STRING_ARRAY string array}.
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
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.STRING_ARRAY.name())
        );
    }

    /**
     * Set the value of this property if its type is {@linkplain PropertyType#RAW raw}.
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
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.RAW.name())
        );
    }

    @Override
    default boolean isWritable() {
        return true;
    }
}
