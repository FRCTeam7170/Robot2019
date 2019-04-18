package frc.team7170.lib.data.property;

abstract class BaseRProperty extends BaseProperty implements RWProperty {

    private final int pollPeriodMs;

    BaseRProperty(String name, PropertyType type, int pollPeriodMs) {
        super(name, type);
        this.pollPeriodMs = pollPeriodMs;
    }

    @Override
    public int getPollPeriodMs() {
        return pollPeriodMs;
    }
}
