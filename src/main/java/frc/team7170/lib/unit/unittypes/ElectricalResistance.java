package frc.team7170.lib.unit.unittypes;

public class ElectricalResistance extends UnitType<FundamentalUnitType> {

    ElectricalResistance() {
        super(new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -3)
                .power(FundamentalUnitType.DISTANCE, 2)
                .power(FundamentalUnitType.CURRENT, -2)
                .power(FundamentalUnitType.MASS, 1));
    }
}
