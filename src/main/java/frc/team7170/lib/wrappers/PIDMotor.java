package frc.team7170.lib.wrappers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.PIDSourceType;

public interface PIDMotor extends Motor, PIDFAccessor {

    enum MotorMode {
        PERCENT(ControlMode.PercentOutput, ControlType.kDutyCycle, null),
        VOLTAGE(null, ControlType.kVoltage, null),
        CURRENT(ControlMode.Current, null, null),
        POSITION(ControlMode.Position, ControlType.kPosition, PIDSourceType.kDisplacement),
        VELOCITY(ControlMode.Velocity, ControlType.kVelocity, PIDSourceType.kRate);

        private final ControlMode eqControlMode;
        private final ControlType eqControlType;
        private final PIDSourceType eqPIDSourceType;

        MotorMode(ControlMode eqControlMode, ControlType eqControlType, PIDSourceType eqPIDSourceType) {
            this.eqControlMode = eqControlMode;
            this.eqControlType = eqControlType;
            this.eqPIDSourceType = eqPIDSourceType;
        }

        public ControlMode toTalonSRXControlMode() {
            requireNonNull(eqControlMode, "ControlMode");
            return eqControlMode;
        }

        public ControlType toTalonSRXControlType() {
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

    void setSetpoint(MotorMode sourceType, double setpoint);

    default void setPositionSetpoint(double setpoint) {
        setSetpoint(MotorMode.POSITION, setpoint);
    }

    default void setVelocitySetpoint(double setpoint) {
        setSetpoint(MotorMode.VELOCITY, setpoint);
    }

    double getSetpoint();

    MotorMode getSetpointType();

    double getError();

    void setTolerance(double tolerance);

    double getTolerance();

    default boolean onTarget() {
        return getError() < getTolerance();
    }
}
