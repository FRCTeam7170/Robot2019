package frc.team7170.lib.unit2;

public enum UniversalUnitType implements FundamentalUnitType<UniversalUnitType> {
    TIME,
    DISTANCE,
    MASS,
    CURRENT,
    TEMPERATURE,
    AMOUNT,
    LUMINOUS_INTENSITY;

    @Override
    public Unit<UniversalUnitType> getbaseUnit() {
        return Units.futToBaseUnit(this);
    }
}
