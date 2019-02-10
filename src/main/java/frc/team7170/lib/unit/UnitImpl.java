package frc.team7170.lib.unit;

public class UnitImpl<F extends Enum<F> & FundamentalUnitType> implements Unit<F> {

    private final double scale;
    private final UnitType<F> unitType;

    protected UnitImpl(double scale, UnitType<F> unitType) {
        this.scale = scale;
        this.unitType = unitType;
    }

    @Override
    public double getScale() {
        return scale;
    }

    @Override
    public UnitType<F> getUnitType() {
        return unitType;
    }

    @Override
    public Unit<F> multiply(double multiplier) {
        return new UnitImpl<>(multiplier * scale, unitType);
    }

    @Override
    public Unit<F> multiply(Unit<F> other) {
        return new UnitImpl<>(scale * other.getScale(), unitType.multiply(other.getUnitType()));
    }

    @Override
    public Unit<F> divide(Unit<F> other) {
        return new UnitImpl<>(scale / other.getScale(), unitType.divide(other.getUnitType()));
    }

    static <F extends Enum<F> & FundamentalUnitType> Unit<F> newBaseUnit(UnitType<F> unitType) {
        return new UnitImpl<>(1.0, unitType);
    }
}
