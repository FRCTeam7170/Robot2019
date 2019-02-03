package frc.team7170.lib.unit.unittypes;

import frc.team7170.lib.unit.BaseUnit;

public interface IFundamentalUnitType<R extends Enum<R> & IFundamentalUnitType<R>> {

    BaseUnit<R, ? extends UnitType<R>> getBaseUnit();
}
