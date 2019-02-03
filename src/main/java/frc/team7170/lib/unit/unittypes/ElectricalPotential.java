package frc.team7170.lib.unit.unittypes;

public class ElectricalPotential extends UnitType<FundamentalUnitType> {

    ElectricalPotential() {
        super(new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -3)
                .power(FundamentalUnitType.DISTANCE, 2)
                .power(FundamentalUnitType.CURRENT, -1)
                .power(FundamentalUnitType.MASS, 1));
    }
}
