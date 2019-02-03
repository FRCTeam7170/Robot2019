package frc.team7170.lib.unit.unittypes;

public class ElectricalCapacitance extends UnitType<FundamentalUnitType> {

    ElectricalCapacitance() {
        super(new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, 4)
                .power(FundamentalUnitType.DISTANCE, -2)
                .power(FundamentalUnitType.CURRENT, 2)
                .power(FundamentalUnitType.MASS, -1));
    }
}
