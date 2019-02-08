package frc.team7170.lib.unit2;

public class MetricUnit<F extends Enum<F> & FundamentalUnitType<F>> extends UnitImpl<F> implements Metric {

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
