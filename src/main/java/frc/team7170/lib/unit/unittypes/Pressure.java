package frc.team7170.lib.unit.unittypes;

public class Pressure extends UnitType<FundamentalUnitType> {

    Pressure() {
        super(new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, -2)
                .power(FundamentalUnitType.DISTANCE, -1)
                .power(FundamentalUnitType.MASS, 1));
    }
}
