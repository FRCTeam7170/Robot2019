package frc.team7170.lib.unit.unittypes;

public class ElectricalInductance extends UnitType<FundamentalUnitType> {

    ElectricalInductance() {
        super(new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -2)
                .power(FundamentalUnitType.DISTANCE, 2)
                .power(FundamentalUnitType.CURRENT, -2)
                .power(FundamentalUnitType.MASS, 1));
    }
}
