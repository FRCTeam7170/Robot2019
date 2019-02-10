package frc.team7170.lib.wrappers;

import edu.wpi.first.wpilibj.PIDOutput;

public interface Motor extends PIDOutput {

    void setMotor(double percent);

    double getMotor();

    default void killMotor() {
        setMotor(0.0);
    }

    void setMotorInverted(boolean inverted);

    boolean isMotorInverted();

    @Override
    default void pidWrite(double output) {
        setMotor(output);
    }
}
