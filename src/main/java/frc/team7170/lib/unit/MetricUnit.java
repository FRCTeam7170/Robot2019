package frc.team7170.lib.unit;

public class MetricUnit<F extends Enum<F> & FundamentalUnitType> extends UnitImpl<F> implements Metric {

    private final MetricPrefix prefix;

    public MetricUnit(MetricPrefix prefix, UnitType<F> unitType) {
        super(prefix.getFactor(), unitType);
        this.prefix = prefix;
    }

    @Override
    public MetricPrefix getPrefix() {
        return prefix;
    }
}
