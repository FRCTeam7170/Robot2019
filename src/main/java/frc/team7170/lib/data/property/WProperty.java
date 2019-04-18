package frc.team7170.lib.data.property;

public interface WProperty extends Property {

    default void setBoolean(boolean value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.BOOLEAN.name())
        );
    }

    default void setDouble(double value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.DOUBLE.name())
        );
    }

    default void setString(String value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.STRING.name())
        );
    }

    default void setBooleanArray(boolean[] value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.BOOLEAN_ARRAY.name())
        );
    }

    default void setDoubleArray(double[] value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.DOUBLE_ARRAY.name())
        );
    }

    default void setStringArray(String[] value) {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.STRING_ARRAY.name())
        );
    }

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
