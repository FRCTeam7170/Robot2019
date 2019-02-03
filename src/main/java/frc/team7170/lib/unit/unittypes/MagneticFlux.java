package frc.team7170.lib.unit.unittypes;

public class MagneticFlux extends UnitType<FundamentalUnitType> {

    MagneticFlux() {
        super(FundamentalUnitType.class, new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -2)
                .power(FundamentalUnitType.DISTANCE, 2)
                .power(FundamentalUnitType.CURRENT, -1)
                .power(FundamentalUnitType.MASS, 1)
                .powerMap);
    }
}
