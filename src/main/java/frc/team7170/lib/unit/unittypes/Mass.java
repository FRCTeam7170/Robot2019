package frc.team7170.lib.unit.unittypes;

public class Mass extends UnitType<FundamentalUnitType> {

    Mass() {
        super(FundamentalUnitType.class, new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.MASS, 1)
                .powerMap);
    }
}
