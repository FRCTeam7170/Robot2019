package frc.team7170.lib.unit.unittypes;

public class Power extends UnitType<FundamentalUnitType> {

    Power() {
        super(FundamentalUnitType.class, new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -3)
                .power(FundamentalUnitType.DISTANCE, 2)
                .power(FundamentalUnitType.MASS, 1)
                .powerMap);
    }
}
