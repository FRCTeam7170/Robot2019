package frc.team7170.lib.data.property;

public interface RProperty extends Property {

    default boolean getBoolean() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.BOOLEAN.name())
        );
    }

    default double getDouble() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.DOUBLE.name())
        );
    }

    default String getString() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.STRING.name())
        );
    }

    default boolean[] getBooleanArray() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.BOOLEAN_ARRAY.name())
        );
    }

    default double[] getDoubleArray() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.DOUBLE_ARRAY.name())
        );
    }

    default String[] getStringArray() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.STRING_ARRAY.name())
        );
    }

    default byte[] getRaw() {
        throw new UnsupportedOperationException(
                String.format("this property is of type %s, not %s", getType().name(), PropertyType.RAW.name())
        );
    }

    int getPollPeriodMs();

    @Override
    default boolean isReadable() {
        return true;
    }
}
