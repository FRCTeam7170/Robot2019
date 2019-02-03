package frc.team7170.lib.unit.unittypes;

public class Time extends UnitType<FundamentalUnitType> {

    Time() {
        super(FundamentalUnitType.class, new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, 1)
                .powerMap);
    }
}
