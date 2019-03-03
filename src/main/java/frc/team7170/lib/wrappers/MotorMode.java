package frc.team7170.lib.wrappers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.team7170.lib.unit.Unit;
import frc.team7170.lib.unit.Units;
import frc.team7170.lib.unit.UniversalUnitType;

public enum MotorMode {
    PERCENT(ControlMode.PercentOutput, ControlType.kDutyCycle, null, Units.IDENTITY),
    VOLTAGE(null, ControlType.kVoltage, null, Units.VOLT),
    CURRENT(ControlMode.Current, null, null, Units.AMPERE),
    POSITION(ControlMode.Position, ControlType.kPosition, PIDSourceType.kDisplacement, Units.REVOLUTION),
    VELOCITY(ControlMode.Velocity, ControlType.kVelocity, PIDSourceType.kRate, Units.RPM);

    private final ControlMode eqControlMode;
    private final ControlType eqControlType;
    private final PIDSourceType eqPIDSourceType;
    private final Unit<UniversalUnitType> unit;

    MotorMode(ControlMode eqControlMode, ControlType eqControlType,
              PIDSourceType eqPIDSourceType, Unit<UniversalUnitType> unit) {
        this.eqControlMode = eqControlMode;
        this.eqControlType = eqControlType;
        this.eqPIDSourceType = eqPIDSourceType;
        this.unit = unit;
    }

    public ControlMode toTalonSRXControlMode() {
        requireNonNull(eqControlMode, "ControlMode");
        return eqControlMode;
    }

    public ControlType toSparkMaxControlType() {
        requireNonNull(eqControlType, "ControlType");
        return eqControlType;
    }

    public PIDSourceType toPIDSourceType() {
        requireNonNull(eqPIDSourceType, "PIDSourceType");
        return eqPIDSourceType;
    }

    private void requireNonNull(Object obj, String type) {
        if (obj == null) {
            throw new UnsupportedOperationException(
                    String.format("MotorMode '%s' has no equivalent %s", name(), type)
            );
        }
    }

    public double toStandardUnits(double value, Unit<UniversalUnitType> fromUnit) {
        return Units.convertAndCheck(value, fromUnit, unit);
    }

    public double fromStandardUnits(double value, Unit<UniversalUnitType> toUnit) {
        return Units.convertAndCheck(value, unit, toUnit);
    }

    public static MotorMode fromTalonSRXControlMode(ControlMode controlMode) {
        switch (controlMode) {
            case PercentOutput:
                return PERCENT;
            case Current:
                return CURRENT;
            case Position:
                return POSITION;
            case Velocity:
                return VELOCITY;
            default:
                throw new IllegalArgumentException("no MotorMode mapping for given TalonSRX ControlMode");
        }
    }

    public static MotorMode fromSparkMaxControlMode(ControlType controlType) {
        switch (controlType) {
            case kDutyCycle:
                return PERCENT;
            case kVoltage:
                return VOLTAGE;
            case kPosition:
                return POSITION;
            case kVelocity:
                return VELOCITY;
            default:
                throw new IllegalArgumentException("no MotorMode mapping for given Spark MAX ControlType");
        }
    }

    public static MotorMode fromPIDSourceType(PIDSourceType pidSourceType) {
        switch (pidSourceType) {
            case kDisplacement:
                return POSITION;
            case kRate:
                return VELOCITY;
            default:
                throw new IllegalArgumentException("no MotorMode mapping for given PIDSourceType");
        }
    }
}
