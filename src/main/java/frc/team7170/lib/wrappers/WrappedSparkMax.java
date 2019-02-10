package frc.team7170.lib.wrappers;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import frc.team7170.lib.unit.Unit;
import frc.team7170.lib.unit.Units;
import frc.team7170.lib.unit.UniversalUnitType;

import java.util.Optional;

class WrappedSparkMax extends AbstractPIDMotor {

    private static final Unit<UniversalUnitType> ELECTRICAL_POTENTIAL_UNIT = Units.VOLT;
    private static final Unit<UniversalUnitType> CURRENT_UNIT = Units.AMPERE;

    private final CANSparkMax sparkMax;
    private final CANPIDController pidController;
    private double setpoint = 0.0;

    WrappedSparkMax(CANSparkMax sparkMax, Unit<UniversalUnitType> positionUnit, Unit<UniversalUnitType> velocityUnit) {
        super(ELECTRICAL_POTENTIAL_UNIT, CURRENT_UNIT, positionUnit, velocityUnit);
        this.sparkMax = sparkMax;
        this.pidController = sparkMax.getPIDController();
    }

    @Override
    void setSetpointRaw(MotorMode motorMode, double setpoint) {
        this.setpoint = setpoint;
        pidController.setReference(setpoint, motorMode.toSparkMaxControlType());
    }

    @Override
    double getSetpointRaw() {
        return setpoint;
    }

    @Override
    double getErrorRaw() {
        // TODO: this is pretty awful
        switch (getSetpointMode()) {
            case PERCENT:
                return setpoint - sparkMax.getAppliedOutput();
            case VOLTAGE:
                return setpoint - sparkMax.getAppliedOutput() * sparkMax.getBusVoltage();
            case POSITION:
                return setpoint - sparkMax.getEncoder().getPosition();
            case VELOCITY:
                return setpoint - sparkMax.getEncoder().getVelocity();
            default:
                throw new AssertionError();
        }
    }

    @Override
    void setToleranceRaw(double tolerance) {}

    @Override
    public MotorMode getSetpointMode() {
        Optional<Integer> ctrlType = sparkMax.getParameterInt(CANSparkMaxLowLevel.ConfigParameter.kCtrlType);
        if (ctrlType.isPresent()) {
            switch (ctrlType.get()) {
                case 0:
                    return MotorMode.PERCENT;
                case 1:
                    return MotorMode.VELOCITY;
                case 2:
                    return MotorMode.VOLTAGE;
                case 3:
                    return MotorMode.POSITION;
                default:
                    throw new AssertionError();
            }
        }
        throw new RuntimeException("Spark Max ControlType not available");
    }

    @Override
    public double getMotor() {
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
    public void setP(double P) {
        pidController.setP(P);
    }

    @Override
    public double getP() {
        return pidController.getP();
    }

    @Override
    public void setI(double I) {
        pidController.setI(I);
    }

    @Override
    public double getI() {
        return pidController.getI();
    }

    @Override
    public void setD(double D) {
        pidController.setD(D);
    }

    @Override
    public double getD() {
        return pidController.getD();
    }

    @Override
    public void setF(double F) {
        pidController.setFF(F);
    }

    @Override
    public double getF() {
        return pidController.getFF();
    }
}
