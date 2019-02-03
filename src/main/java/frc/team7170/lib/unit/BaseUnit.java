package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.BaseUnitType;
import frc.team7170.lib.unit.unittypes.IFundamentalUnitType;

public class BaseUnit<R extends Enum<R> & IFundamentalUnitType<R>, T extends BaseUnitType<R>> extends ScaledUnit<R, T> {

    public BaseUnit(T type) {
        super(1.0, type);
    }
}
