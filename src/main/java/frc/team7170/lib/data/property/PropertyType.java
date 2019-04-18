package frc.team7170.lib.data.property;

/**
 * The types supported by {@linkplain Property properties}.
 *
 * @author Robert Russell
 * @see Property
 */
public enum PropertyType {

    BOOLEAN,
    DOUBLE,
    STRING,
    BOOLEAN_ARRAY,
    DOUBLE_ARRAY,
    STRING_ARRAY,
    /**
     * Corresponds to the Java {@code byte[]} type.
     */
    RAW
}
