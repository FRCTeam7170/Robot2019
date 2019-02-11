package frc.team7170.lib.wrappers;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

class WrappedCTREMotorController extends AbstractPIDFController implements PIDFMotor {

    private final BaseMotorController ctreMotorController;

    WrappedCTREMotorController(BaseMotorController ctreMotorController) {
        this.ctreMotorController = ctreMotorController;
    }

    @Override
    void setSetpointRaw(MotorMode motorMode, double setpoint) {
        ctreMotorController.set(motorMode.toTalonSRXControlMode(), setpoint);
    }

    @Override
    double getErrorRaw() {
        return ctreMotorController.getClosedLoopError();
    }

    @Override
    public double getPercent() {
        return ctreMotorController.getMotorOutputPercent();
    }

    @Override
    public void setMotorInverted(boolean inverted) {
        ctreMotorController.setInverted(inverted);
    }

    @Override
    public boolean isMotorInverted() {
        return ctreMotorController.getInverted();
    }

    @Override
    ProfileAccessor newProfileAccessor(int slot) {
        return new ProfileAccessorImpl();
    }

    @Override
    void loadProfileAccessor(ProfileAccessor profileAccessor) {
        ctreMotorController.config_kP(0, profileAccessor.getP());
        ctreMotorController.config_kI(0, profileAccessor.getI());
        ctreMotorController.config_kD(0, profileAccessor.getD());
        ctreMotorController.config_kF(0, profileAccessor.getF());
        ctreMotorController.config_IntegralZone(0, (int) profileAccessor.getIZone());
        ctreMotorController.configNominalOutputForward(profileAccessor.getMinimumOutput());
        ctreMotorController.configNominalOutputReverse(profileAccessor.getMinimumOutput());
        ctreMotorController.configPeakOutputForward(profileAccessor.getMaximumOutput());
        ctreMotorController.configPeakOutputReverse(profileAccessor.getMaximumOutput());
        ctreMotorController.configAllowableClosedloopError(0, (int) profileAccessor.getTolerance());
    }
}
