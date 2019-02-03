package frc.team7170.lib.unit.unittypes;

public class Distance extends UnitType<FundamentalUnitType> {

    Distance() {
        super(FundamentalUnitType.class, new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.DISTANCE, 1)
                .powerMap);
    }
}
