package frc.team7170.lib.wrappers;

public interface PIDMotor extends Motor, PIDFAccessor {
    // TODO: need to support multiple parameter slots

    @Override
    default void setMotor(double percent) {
        setSetpoint(MotorMode.PERCENT, percent);
    }

    // See above
    void setSetpoint(MotorMode motorMode, double setpoint);

    // m
    default void setPositionSetpoint(double setpoint) {
        setSetpoint(MotorMode.POSITION, setpoint);
    }

    // m/s
    default void setVelocitySetpoint(double setpoint) {
        setSetpoint(MotorMode.VELOCITY, setpoint);
    }

    // See above
    double getSetpoint();

    MotorMode getSetpointMode();

    // See above
    double getError();

    // See above
    void setTolerance(double tolerance);

    // See above
    double getTolerance();

    default boolean onTarget() {
        return getError() < getTolerance();
    }
}
