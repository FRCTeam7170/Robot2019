package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.BaseUnitType;
import frc.team7170.lib.unit.unittypes.IFundamentalUnitType;

public class BaseMetricUnit<R extends Enum<R> & IFundamentalUnitType<R>, T extends BaseUnitType<R>> extends BaseUnit<R, T> implements Metric {

    public BaseMetricUnit(T type) {
        super(type);
    }

    @Override
    public MetricPrefix getPrefix() {
        return MetricPrefix.BASE;
    }
}
