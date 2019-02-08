package frc.team7170.lib.unit.unittypes;

public class Energy extends UnitType<FundamentalUnitType> {

    Energy() {
        super(new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -2)
                .power(FundamentalUnitType.DISTANCE, 2)
                .power(FundamentalUnitType.MASS, 1));
    }
}