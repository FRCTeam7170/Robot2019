package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.IFundamentalUnitType;
import frc.team7170.lib.unit.unittypes.UnitType;

public class BaseUnit<R extends Enum<R> & IFundamentalUnitType<R>, T extends UnitType<R>> extends Unit<R, T> {

    public BaseUnit(T type) {
        super(1.0, type, null);
    }
}
