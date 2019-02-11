package frc.team7170.lib.wrappers;

import frc.team7170.lib.unit.Unit;
import frc.team7170.lib.unit.Units;
import frc.team7170.lib.unit.UniversalUnitType;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractPIDFController implements PIDFController {

    private Unit<UniversalUnitType> elecPotentialUnit = Units.VOLT;
    private Unit<UniversalUnitType> currentUnit = Units.AMPERE;
    private Unit<UniversalUnitType> positionUnit = Units.REVOLUTION;
    private Unit<UniversalUnitType> velocityUnit = Units.RPM;

    private final List<ProfileAccessor> profileList = new ArrayList<>();
    private MotorMode motorMode = MotorMode.PERCENT;
    private double setpoint;
    private int slot;

    @Override
    public ProfileAccessor getProfileForSlot(int slot) {
        int nProfiles = profileList.size();
        if (slot >= nProfiles) {
            if (slot != nProfiles) {
                throw new IndexOutOfBoundsException();
            }
            ProfileAccessor pa = newProfileAccessor(slot);
            profileList.add(pa);
            return pa;
        }
        return profileList.get(slot);
    }

    @Override
    public void setSetpoint(MotorMode motorMode, double setpoint, int slot) {
        if (slot >= profileList.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (slot != this.slot) {
            loadProfileAccessor(profileList.get(slot));
        }
        this.motorMode = motorMode;
        this.setpoint = setpoint;
        this.slot = slot;
        setSetpointRaw(motorMode, motorMode.fromStandardUnits(setpoint, getUnit(motorMode)));
    }

    @Override
    public double getError() {
        MotorMode motorMode = getSetpointMode();
        return motorMode.toStandardUnits(getErrorRaw(), getUnit(motorMode));
    }

    @Override
    public int getCurrentSlot() {
        return slot;
    }

    @Override
    public double getSetpoint() {
        return setpoint;
    }

    @Override
    public MotorMode getSetpointMode() {
        return motorMode;
    }

    @Override
    public void setUnit(MotorMode motorMode, Unit<UniversalUnitType> unit) {
        switch (motorMode) {
            case VOLTAGE:
                elecPotentialUnit = unit;
            case CURRENT:
                currentUnit = unit;
            case POSITION:
                positionUnit = unit;
            case VELOCITY:
                velocityUnit = unit;
            default:
                throw new IllegalArgumentException("MotorMode must be one of: VOLTAGE, CURRENT, POSITION, VELOCITY");
        }
    }

    @Override
    public Unit<UniversalUnitType> getUnit(MotorMode motorMode) {
        switch (motorMode) {
            case VOLTAGE:
                return elecPotentialUnit;
            case CURRENT:
                return currentUnit;
            case POSITION:
                return positionUnit;
            case VELOCITY:
                return velocityUnit;
            default:
                return Units.IDENTITY;
        }
    }

    abstract ProfileAccessor newProfileAccessor(int slot);

    abstract void loadProfileAccessor(ProfileAccessor profileAccessor);

    abstract void setSetpointRaw(MotorMode motorMode, double setpoint);

    abstract double getErrorRaw();
}
