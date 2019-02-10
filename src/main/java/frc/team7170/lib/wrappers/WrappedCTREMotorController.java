package frc.team7170.lib.wrappers;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import frc.team7170.lib.unit.Unit;
import frc.team7170.lib.unit.Units;
import frc.team7170.lib.unit.UniversalUnitType;

class WrappedCTREMotorController extends AbstractPIDMotor {

    private static final Unit<UniversalUnitType> ELECTRICAL_POTENTIAL_UNIT = Units.VOLT;
    private static final Unit<UniversalUnitType> CURRENT_UNIT = Units.AMPERE;

    private final BaseMotorController ctreMotorController;
    private double P, I, D, F;

    WrappedCTREMotorController(BaseMotorController ctreMotorController,
                               Unit<UniversalUnitType> positionUnit, Unit<UniversalUnitType> velocityUnit) {
        super(ELECTRICAL_POTENTIAL_UNIT, CURRENT_UNIT, positionUnit, velocityUnit);
        this.ctreMotorController = ctreMotorController;

        setPIDF(0.0, 0.0, 0.0, 0.0);
    }

    @Override
    void setSetpointRaw(MotorMode motorMode, double setpoint) {
        ctreMotorController.set(motorMode.toTalonSRXControlMode(), setpoint);
    }

    @Override
    double getSetpointRaw() {
        return ctreMotorController.getClosedLoopTarget();
    }

    @Override
    double getErrorRaw() {
        return ctreMotorController.getClosedLoopError();
    }

    @Override
    void setToleranceRaw(double tolerance) {
        // TODO: restricted to first slot...
        ctreMotorController.configAllowableClosedloopError(0, (int) tolerance);
    }

    @Override
    public MotorMode getSetpointMode() {
        return MotorMode.fromTalonSRXControlMode(ctreMotorController.getControlMode());
    }

    @Override
    public double getMotor() {
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
    public void setP(double P) {
        this.P = P;
        ctreMotorController.config_kP(0, P);
    }

    @Override
    public double getP() {
        return P;
    }

    @Override
    public void setI(double I) {
        this.I = I;
        ctreMotorController.config_kI(0, I);
    }

    @Override
    public double getI() {
        return I;
    }

    @Override
    public void setD(double D) {
        this.D = D;
        ctreMotorController.config_kD(0, D);
    }

    @Override
    public double getD() {
        return D;
    }

    @Override
    public void setF(double F) {
        this.F = F;
        ctreMotorController.config_kF(0, F);
    }

    @Override
    public double getF() {
        return F;
    }
}
