package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.BaseUnitType;
import frc.team7170.lib.unit.unittypes.IFundamentalUnitType;

public class MetricUnit<R extends Enum<R> & IFundamentalUnitType<R>, T extends BaseUnitType<R>>
        extends ScaledUnit<R, T> implements Metric {

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
