package frc.team7170.lib.wrappers;

import frc.team7170.lib.CalcUtil;
import frc.team7170.lib.unit.Unit;
import frc.team7170.lib.unit.UniversalUnitType;

// TODO: should we really force inputs to be in metres & m/s?
public interface PIDFController {

    ProfileAccessor getProfileForSlot(int slot);

    default ProfileAccessor getCurrentProfile() {
        return getProfileForSlot(getCurrentSlot());
    }

    int getCurrentSlot();

    void setUnit(MotorMode motorMode, Unit<UniversalUnitType> unit);

    Unit<UniversalUnitType> getUnit(MotorMode motorMode);

    void setSetpoint(MotorMode motorMode, double setpoint, int slot);

    default void setSetpoint(MotorMode motorMode, double setpoint) {
        setSetpoint(motorMode, setpoint, 0);
    }

    double getSetpoint();

    MotorMode getSetpointMode();

    double getError();

    default boolean onTarget() {
        return CalcUtil.inThreshold(getError(), 0, getCurrentProfile().getTolerance());
    }
}
