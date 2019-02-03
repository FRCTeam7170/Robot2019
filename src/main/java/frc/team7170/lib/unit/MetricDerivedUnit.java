package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.IFundamentalUnitType;
import frc.team7170.lib.unit.unittypes.UnitType;

public class MetricDerivedUnit<R extends Enum<R> & IFundamentalUnitType<R>, T extends UnitType<R>>
        extends DerivedUnit<R, T> implements Metric {

    private final MetricPrefix prefix;

    protected MetricDerivedUnit(MetricPrefix prefix, T type, UnitSet<R> unitSet) {
        super(prefix.getFactor(), type, unitSet);
        this.prefix = prefix;
    }

    public MetricDerivedUnit(MetricPrefix prefix, MetricDerivedUnit<R, T> metricDerivedUnit) {
        this(prefix, metricDerivedUnit.getUnitType(), metricDerivedUnit.getUnitSet());
    }

    private MetricDerivedUnit(T type, Class<R> futClass) {
        super(type, futClass);
        this.prefix = MetricPrefix.BASE;
    }

    @Override
    public MetricPrefix getPrefix() {
        return prefix;
    }

    public static <R extends Enum<R> & IFundamentalUnitType<R>, T extends UnitType<R>>
    MetricDerivedUnit<R, T> newBaseMetricDerivedUnit(T type, Class<R> futClass) {
        return new MetricDerivedUnit<>(type, futClass);
    }

    public static <R extends Enum<R> & IFundamentalUnitType<R>, T extends UnitType<R>>
    MetricDerivedUnit<R, T> newBaseMetricDerivedUnit(T type, UnitSet<R> unitSet) {
        return new MetricDerivedUnit<>(MetricPrefix.BASE, type, unitSet);
    }
}
