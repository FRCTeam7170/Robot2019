package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.IFundamentalUnitType;
import frc.team7170.lib.unit.unittypes.UnitType;

public class MetricUnit<R extends Enum<R> & IFundamentalUnitType<R>, T extends UnitType<R>> extends Unit<R, T> implements IMetric {

    private final MetricPrefix prefix;

    public MetricUnit(MetricPrefix prefix, BaseMetricUnit<R, T> baseMetricUnit) {
        super(prefix.getFactor(), baseMetricUnit);
        this.prefix = prefix;
    }

    @Override
    public MetricPrefix getPrefix() {
        return prefix;
    }
}
