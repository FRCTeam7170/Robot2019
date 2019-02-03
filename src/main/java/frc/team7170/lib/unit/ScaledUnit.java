package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.*;

public class ScaledUnit<R extends Enum<R> & IFundamentalUnitType<R>, T extends UnitType<R>> implements Unit<R, T> {

    private final double factor;
    private final T type;

    protected ScaledUnit(double factor, T type) {
        this.factor = factor;
        this.type = type;
    }

    public ScaledUnit(double factor, Unit<R, T> relativeUnit) {
        this(factor * relativeUnit.getFactor(), relativeUnit.getUnitType());
    }

    @Override
    public double getFactor() {
        return factor;
    }

    @Override
    public T getUnitType() {
        return type;
    }
}
