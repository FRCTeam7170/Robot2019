package frc.team7170.lib.wrappers;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.PIDSourceType;

public class WrappedSparkMax implements PIDMotor {

    private final CANSparkMax sparkMax;

    WrappedSparkMax(CANSparkMax sparkMax) {
        this.sparkMax = sparkMax;
    }

    @Override
    public void setSetpoint(PIDSourceType sourceType, double setpoint) {
    }

    @Override
    public double getSetpoint() {
        return 0;
    }

    @Override
    public PIDSourceType getSetpointType() {
        return null;
    }

    @Override
    public double getError() {
        return 0;
    }

    @Override
    public void setTolerance(double tolerance) {

    }

    @Override
    public double getTolerance() {
        return 0;
    }

    @Override
    public void setMotor(double percent) {

    }

    @Override
    public double getMotor() {
        return 0;
    }

    @Override
    public void setMotorInverted(boolean inverted) {

    }

    @Override
    public boolean isMotorInverted() {
        return false;
    }

    @Override
    public void setP(double P) {

    }

    @Override
    public double getP() {
        return 0;
    }

    @Override
    public void setI(double I) {

    }

    @Override
    public double getI() {
        return 0;
    }

    @Override
    public void setD(double D) {

    }

    @Override
    public double getD() {
        return 0;
    }

    @Override
    public void setF(double F) {

    }

    @Override
    public double getF() {
        return 0;
    }
}
