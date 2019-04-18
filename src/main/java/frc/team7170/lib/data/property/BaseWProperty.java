package frc.team7170.lib.data.property;

/**
 * The abstract base class used for the writable properties returned in {@link PropertyFactory PropertyFactory}.
 *
 * @author Robert Russell
 * @see WProperty
 * @see PropertyFactory
 */
abstract class BaseWProperty extends BaseProperty implements WProperty {

    /**
     * @param name the name of the property.
     * @param type the type of the property.
     * @throws NullPointerException if either of {@code name} or {@code type} are {@code null}.
     */
    BaseWProperty(String name, PropertyType type) {
        super(name, type);
    }
}