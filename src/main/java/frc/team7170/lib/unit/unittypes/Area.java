package frc.team7170.lib.unit.unittypes;

public class Area extends UnitType<FundamentalUnitType> {

    Area() {
        super(new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.DISTANCE, 2));
    }
}