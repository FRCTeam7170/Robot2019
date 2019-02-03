package frc.team7170.lib.unit.unittypes;

public class Velocity extends UnitType<FundamentalUnitType> {

    Velocity() {
        super(new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -1)
                .power(FundamentalUnitType.DISTANCE, 1));
    }
}
