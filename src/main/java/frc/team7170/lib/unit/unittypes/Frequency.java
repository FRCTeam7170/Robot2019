package frc.team7170.lib.unit.unittypes;

public class Frequency extends UnitType<FundamentalUnitType> {

    Frequency() {
        super(new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -1));
    }
}
