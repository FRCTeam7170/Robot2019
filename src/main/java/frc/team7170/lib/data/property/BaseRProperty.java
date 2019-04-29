package frc.team7170.lib.data.property;

import frc.team7170.lib.data.ValueType;

/**
 * The abstract base class used for the readable properties returned in {@link PropertyFactory PropertyFactory}.
 *
 * @author Robert Russell
 * @see RProperty
 * @see PropertyFactory
 */
abstract class BaseRProperty extends BaseProperty implements RProperty {

    private final int pollPeriodMs;

    /**
     * @param name the name of the property.
     * @param type the type of the property.
     * @param pollPeriodMs the poll period of the property in milliseconds. (See
     * {@link RProperty#getPollPeriodMs() RProperty} for a description of precisely what this means.)
     * @throws NullPointerException if either of {@code name} or {@code type} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    BaseRProperty(String name, ValueType type, int pollPeriodMs) {
        super(name, type);
        this.pollPeriodMs = pollPeriodMs;
    }

    @Override
    public int getPollPeriodMs() {
        return pollPeriodMs;
    }
}
