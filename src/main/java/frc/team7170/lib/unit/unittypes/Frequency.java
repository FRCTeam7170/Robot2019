package frc.team7170.lib.unit.unittypes;

public class Frequency extends UnitType<FundamentalUnitType> {

    Frequency() {
        super(FundamentalUnitType.class, new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -1)
                .powerMap);
    }
}
