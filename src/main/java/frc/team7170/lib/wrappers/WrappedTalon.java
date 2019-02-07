package frc.team7170.lib.wrappers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

class WrappedTalon implements PIDMotor {

    private final TalonSRX talonSRX;
    private double P, I, D, F;
    private int tolerance;
    // TODO: standardize units

    WrappedTalon(TalonSRX talonSRX, double initP, double initI, double initD, double initF, int initTolerance) {
        this.talonSRX = talonSRX;
        this.P = initP;
        this.I = initI;
        this.D = initD;
        this.F = initF;
        this.tolerance = initTolerance;
        setPIDF(initP, initI, initD, initF);
        setTolerance(initTolerance);
    }

    @Override
    public void setSetpoint(MotorMode sourceType, double setpoint) {
        talonSRX.set(sourceType.toTalonSRXControlMode(), setpoint);
    }

    @Override
    public double getSetpoint() {
        return talonSRX.getClosedLoopTarget();
    }

    @Override
    public MotorMode getSetpointType() {
        return MotorMode.fromTalonSRXControlMode(talonSRX.getControlMode());
    }

    @Override
    public double getError() {
        return talonSRX.getClosedLoopError();
    }

    @Override
    public void setTolerance(double tolerance) {
        this.tolerance = (int) tolerance;
        talonSRX.configAllowableClosedloopError(0, this.tolerance);
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }

    @Override
    public void setMotor(double percent) {
        talonSRX.set(ControlMode.PercentOutput, percent);
    }

    @Override
    public double getMotor() {
        return talonSRX.getMotorOutputPercent();
    }

    @Override
    public void setMotorInverted(boolean inverted) {
        talonSRX.setInverted(inverted);
    }

    @Override
    public boolean isMotorInverted() {
        return talonSRX.getInverted();
    }

    @Override
    public void setP(double P) {
        this.P = P;
        talonSRX.config_kP(0, P);
    }

    @Override
    public double getP() {
        return P;
    }

    @Override
    public void setI(double I) {
        this.I = I;
        talonSRX.config_kI(0, I);
    }

    @Override
    public double getI() {
        return I;
    }

    @Override
    public void setD(double D) {
        this.D = D;
        talonSRX.config_kD(0, D);
    }

    @Override
    public double getD() {
        return D;
    }

    @Override
    public void setF(double F) {
        this.F = F;
        talonSRX.config_kF(0, F);
    }

    @Override
    public double getF() {
        return F;
    }
}
