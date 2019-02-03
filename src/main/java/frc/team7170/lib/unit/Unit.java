package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.*;

public class Unit<R extends Enum<R> & IFundamentalUnitType<R>, T extends UnitType<R>> {

    private final double factor;
    private final T type;
    private final BaseUnit<R, T> baseUnit;

    protected Unit(double factor, T type, BaseUnit<R, T> baseUnit) {
        this.factor = factor;
        this.type = type;
        this.baseUnit = baseUnit;
    }

    public Unit(double factor, BaseUnit<R, T> baseUnit) {
        this(factor, baseUnit.getUnitType(), baseUnit);
    }

    public Unit(double factor, Unit<R, T> relativeUnit) {
        this(factor * relativeUnit.getFactor(), relativeUnit.getBaseUnit());
    }

    public double getFactor() {
        return factor;
    }

    public T getUnitType() {
        return type;
    }

    public boolean isBaseUnit() {
        return baseUnit == null;
    }

    public BaseUnit<R, T> getBaseUnit() {
        return baseUnit;
    }
}
