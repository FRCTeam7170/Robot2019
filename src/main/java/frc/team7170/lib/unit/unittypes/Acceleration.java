package frc.team7170.lib.unit.unittypes;

public class Acceleration extends UnitType<FundamentalUnitType> {

    Acceleration() {
        super(new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -2)
                .power(FundamentalUnitType.DISTANCE, 1));
    }
}
