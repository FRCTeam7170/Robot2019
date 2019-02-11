package frc.team7170.lib.wrappers;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;

class WrappedSparkMax extends AbstractPIDFController implements PIDFMotor {

    private final CANSparkMax sparkMax;
    private final CANPIDController pidController;

    WrappedSparkMax(CANSparkMax sparkMax) {
        this.sparkMax = sparkMax;
        this.pidController = sparkMax.getPIDController();
    }

    @Override
    void setSetpointRaw(MotorMode motorMode, double setpoint) {
        pidController.setReference(setpoint, motorMode.toSparkMaxControlType());
    }

    @Override
    double getErrorRaw() {
        switch (getSetpointMode()) {
            case PERCENT:
                return getSetpoint() - sparkMax.getAppliedOutput();
            case VOLTAGE:
                return getSetpoint() - sparkMax.getAppliedOutput() * sparkMax.getBusVoltage();
            case POSITION:
                return getSetpoint() - sparkMax.getEncoder().getPosition();
            case VELOCITY:
                return getSetpoint() - sparkMax.getEncoder().getVelocity();
            default:
                throw new AssertionError();
        }
    }

    @Override
    public double getPercent() {
        return sparkMax.get();
    }

    @Override
    public void setMotorInverted(boolean inverted) {
        sparkMax.setInverted(inverted);
    }

    @Override
    public boolean isMotorInverted() {
        return sparkMax.getInverted();
    }

    @Override
    ProfileAccessor newProfileAccessor(int slot) {
        return new ProfileAccessorImpl();
    }

    @Override
    void loadProfileAccessor(ProfileAccessor profileAccessor) {
        pidController.setP(profileAccessor.getP());
        pidController.setI(profileAccessor.getI());
        pidController.setD(profileAccessor.getD());
        pidController.setFF(profileAccessor.getF());
        pidController.setIZone(profileAccessor.getIZone());
        pidController.setOutputRange(profileAccessor.getMinimumOutput(), profileAccessor.getMaximumOutput());
    }
}
