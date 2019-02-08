package frc.team7170.lib.unit2;

public interface FundamentalUnitType<F extends Enum<F> & FundamentalUnitType<F>> {

    Unit<F> getbaseUnit();
}
