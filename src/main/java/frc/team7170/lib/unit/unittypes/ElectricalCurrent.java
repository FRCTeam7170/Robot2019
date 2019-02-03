package frc.team7170.lib.unit.unittypes;

public class ElectricalCurrent extends UnitType<FundamentalUnitType> {

    ElectricalCurrent() {
        super(FundamentalUnitType.class, new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.CURRENT, 1)
                .powerMap);
    }
}
