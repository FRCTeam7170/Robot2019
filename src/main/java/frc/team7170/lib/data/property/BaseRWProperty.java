package frc.team7170.lib.data.property;

abstract class BaseRWProperty extends BaseRProperty implements RWProperty {

    BaseRWProperty(String name, PropertyType type, int pollPeriodMs) {
        super(name, type, pollPeriodMs);
    }
}
