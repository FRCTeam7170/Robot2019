package frc.team7170.lib.unit;

public interface UnitType<F extends Enum<F> & FundamentalUnitType> {

    UnitType<F> multiply(UnitType<F> multiplier);

    UnitType<F> divide(UnitType<F> divisor);

    UnitType<F> reciprocal();

    int powerOf(F fut);

    boolean isEquivalentUnitType(UnitType<F> other);

    default void assertMatching(UnitType<F> other) {
        if (!isEquivalentUnitType(other)) {
            throw new IllegalArgumentException("units do not match");
        }
    }
}
