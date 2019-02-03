package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.IFundamentalUnitType;
import frc.team7170.lib.unit.unittypes.UnitType;

public class DerivedUnit<R extends Enum<R> & IFundamentalUnitType<R>, T extends UnitType<R>> implements Unit<R, T> {

    private final double factor;
    private final T type;
    private final UnitSet<R> unitSet;

    protected DerivedUnit(double factor, T type, UnitSet<R> unitSet) {
        this.factor = factor;
        this.type = type;
        this.unitSet = unitSet;
    }

    public DerivedUnit(T type, Class<R> futClass) {
        this(1.0, type, new UnitSet.Builder<>(futClass).build());
    }

    public DerivedUnit(T type, UnitSet<R> unitSet) {
        this(unitSet.calcFactorFor(type), type, unitSet);
    }

    @Override
    public double getFactor() {
        return factor;
    }

    @Override
    public T getUnitType() {
        return type;
    }

    public UnitSet<R> getUnitSet() {
        return unitSet;
    }
}
