package frc.team7170.lib.unit;

public interface Unit<F extends Enum<F> & FundamentalUnitType> {

    double getScale();

    UnitType<F> getUnitType();

    Unit<F> multiply(double multiplier);

    Unit<F> multiply(Unit<F> other);

    default Unit<F> divide(double divisor) {
        return multiply(1 / divisor);
    }

    Unit<F> divide(Unit<F> other);

    default boolean isCommensurateWith(Unit<F> other) {
        return getUnitType().isEquivalentUnitType(other.getUnitType());
    }
}
