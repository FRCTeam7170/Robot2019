package frc.team7170.lib.wrappers;

import edu.wpi.first.wpilibj.PIDOutput;

public interface Motor extends PIDOutput {

    void setPercent(double percent);

    double getPercent();

    default void killMotor() {
        setPercent(0.0);
    }

    void setMotorInverted(boolean inverted);

    boolean isMotorInverted();

    @Override
    default void pidWrite(double output) {
        setPercent(output);
    }
}
