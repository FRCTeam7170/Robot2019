package frc.team7170.lib.unit.unittypes;

public class Angle extends UnitType<FundamentalUnitType> {

    Angle() {
        super(FundamentalUnitType.class, new Config<>(FundamentalUnitType.class)
                .powerMap);
    }
}
