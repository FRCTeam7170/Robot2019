package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.IFundamentalUnitType;
import frc.team7170.lib.unit.unittypes.UnitType;

public class DerivedUnit<R extends Enum<R> & IFundamentalUnitType<R>, T extends UnitType<R>> extends Unit<R, T> {

    private UnitSet<R> unitSet;

    public DerivedUnit(T type, Class<R> futClass) {
        super(1.0, type, null);
        unitSet = new UnitSet.Builder<>(futClass).build();
    }

    public UnitSet<R> getUnitSet() {
        return unitSet;
    }
}
