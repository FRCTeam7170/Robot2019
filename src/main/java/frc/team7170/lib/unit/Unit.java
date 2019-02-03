package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.IFundamentalUnitType;
import frc.team7170.lib.unit.unittypes.UnitType;

// TODO: make this an abstract class instead
public interface Unit<R extends Enum<R> & IFundamentalUnitType<R>, T extends UnitType<R>> {

    double getFactor();

    T getUnitType();
}
