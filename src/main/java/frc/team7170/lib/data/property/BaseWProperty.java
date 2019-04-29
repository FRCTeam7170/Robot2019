package frc.team7170.lib.data.property;

import frc.team7170.lib.data.ValueType;

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
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    BaseWProperty(String name, ValueType type) {
        super(name, type);
    }
}
