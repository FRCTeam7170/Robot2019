package frc.team7170.lib.unit.unittypes;

import frc.team7170.lib.unit.BaseUnit;
import frc.team7170.lib.unit.Units;

public enum FundamentalUnitType implements IFundamentalUnitType<FundamentalUnitType> {
    TIME,
    DISTANCE,
    MASS,
    CURRENT;
    // TEMPERATURE
    // AMOUNT
    // LUMINOUS_INTENSITY

    @Override
    public BaseUnit<FundamentalUnitType, ? extends UnitType<FundamentalUnitType>> getBaseUnit() {
        // Must refer to the base unit indirectly to avoid a circular dependency.
        return Units.futToBaseUnit(this);
    }
}
