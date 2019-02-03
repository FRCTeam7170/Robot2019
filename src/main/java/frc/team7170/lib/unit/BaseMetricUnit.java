package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.IFundamentalUnitType;
import frc.team7170.lib.unit.unittypes.UnitType;

public class BaseMetricUnit<R extends Enum<R> & IFundamentalUnitType<R>, T extends UnitType<R>> extends BaseUnit<R, T> implements IMetric {

    public BaseMetricUnit(T type) {
        super(type);
    }

    @Override
    public MetricPrefix getPrefix() {
        return MetricPrefix.BASE;
    }
}
