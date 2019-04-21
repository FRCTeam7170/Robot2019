package frc.team7170.lib.data.property;

import frc.team7170.lib.Named;
import frc.team7170.lib.data.ValueType;

/**
 * <p>
 * A readable, writable, or read- and writable property. Readable properties represent attributes of a client that are
 * periodically polled to be sent to some (potentially remote) service. Writable properties represent attributes of a
 * client that can be altered by some (potentially remote) service. An example of such a "service" might be a data
 * logging service which periodically polls registered clients to save data the clients generate to a file.
 * </p>
 * <p>
 * Every property object has a {@linkplain ValueType data type} associated with it. The supported data types are
 * {@code boolean}, {@code double}, {@code String}, the array variants of the preceding, and raw ({@code byte[]}).
 * </p>
 * <p>
 * Properties are {@link Named Named} to facilitate the common use case in which the attribute they represent needs a
 * string label to distinguish it from other properties.
 * </p>
 *
 * @author Robert Russell
 * @see RProperty
 * @see WProperty
 * @see RWProperty
 * @see PropertyFactory
 */
public interface Property extends Named {

    /**
     * Get the type of this property.
     *
     * @return the type of this property.
     */
    ValueType getType();

    /**
     * Get if this property is readable.
     *
     * @implSpec The default implementation returns {@code false}, but readable subtypes of {@code Property}
     * <em>must</em> override this method to return true. In most cases, it should be satisfactory to return the literal
     * {@code true} or {@code false}.
     *
     * @return if this property is readable.
     */
    default boolean isReadable() {
        return false;
    }

    /**
     * Get if this property is writable.
     *
     * @implSpec The default implementation returns {@code false}, but writable subtypes of {@code Property}
     * <em>must</em> override this method to return true. In most cases, it should be satisfactory to return the literal
     * {@code true} or {@code false}.
     *
     * @return if this property is writable.
     */
    default boolean isWritable() {
        return false;
    }
}
