package frc.team7170.lib.data;

import frc.team7170.lib.data.property.Property;

/**
 * The types supported by {@linkplain Property properties}.
 *
 * @author Robert Russell
 * @see Property
 */
public enum ValueType {

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
