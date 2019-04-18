package frc.team7170.lib.data.property;

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
     */
    BaseRProperty(String name, PropertyType type, int pollPeriodMs) {
        super(name, type);
        this.pollPeriodMs = pollPeriodMs;
    }

    @Override
    public int getPollPeriodMs() {
        return pollPeriodMs;
    }
}