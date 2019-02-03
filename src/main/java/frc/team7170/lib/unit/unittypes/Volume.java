package frc.team7170.lib.unit.unittypes;

public class Volume extends UnitType<FundamentalUnitType> {

    Volume() {
        super(FundamentalUnitType.class, new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.DISTANCE, 3)
                .powerMap);
    }
}
