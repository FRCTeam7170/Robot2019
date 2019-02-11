package frc.team7170.lib.wrappers;

public interface PIDFMotor extends Motor, PIDFController {

    @Override
    default void setPercent(double percent) {
        setSetpoint(MotorMode.PERCENT, percent);
    }

    default void setPositionSetpoint(double setpoint) {
        setSetpoint(MotorMode.POSITION, setpoint);
    }

    default void setVelocitySetpoint(double setpoint) {
        setSetpoint(MotorMode.VELOCITY, setpoint);
    }
}
