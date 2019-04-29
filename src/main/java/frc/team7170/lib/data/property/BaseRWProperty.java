package frc.team7170.lib.data.property;

import frc.team7170.lib.data.ValueType;

/**
 * The abstract base class used for the readable and writable properties returned in
 * {@link PropertyFactory PropertyFactory}.
 *
 * @author Robert Russell
 * @see RWProperty
 * @see PropertyFactory
 */
abstract class BaseRWProperty extends BaseRProperty implements RWProperty {

    /**
     * @param name the name of the property.
     * @param type the type of the property.
     * @param pollPeriodMs the poll period of the property in milliseconds. This is only for the readable "half" of the
     *                     property. (See {@link RProperty#getPollPeriodMs() RProperty} for a description of precisely
     *                     what this means.)
     * @throws NullPointerException if either of {@code name} or {@code type} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    BaseRWProperty(String name, ValueType type, int pollPeriodMs) {
        super(name, type, pollPeriodMs);
    }
}
