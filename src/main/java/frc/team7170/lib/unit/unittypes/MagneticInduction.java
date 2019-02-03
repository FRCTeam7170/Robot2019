package frc.team7170.lib.unit.unittypes;

public class MagneticInduction extends UnitType<FundamentalUnitType> {

    MagneticInduction() {
        super(FundamentalUnitType.class, new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -2)
                .power(FundamentalUnitType.DISTANCE, -1)
                .power(FundamentalUnitType.MASS, 1)
                .powerMap);
    }
}
