package frc.team7170.lib.unit.unittypes;

public class ElectricalCharge extends UnitType<FundamentalUnitType> {

    ElectricalCharge() {
        super(FundamentalUnitType.class, new Config<>(FundamentalUnitType.class)
                .power(FundamentalUnitType.TIME, 1)
                .power(FundamentalUnitType.CURRENT, 1)
                .powerMap);
    }
}
