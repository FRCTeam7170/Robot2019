package frc.team7170.lib.unit.unittypes;

public class Momentum extends UnitType<FundamentalUnitType> {

    Momentum() {
        super(new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -1)
                .power(FundamentalUnitType.DISTANCE, 1)
                .power(FundamentalUnitType.MASS, 1));
    }
}
