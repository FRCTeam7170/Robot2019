package frc.team7170.lib.wrappers;

public interface Motor {

    void setMotor(double percent);

    double getMotor();

    default void killMotor() {
        setMotor(0.0);
    }

    void setMotorInverted(boolean inverted);

    boolean isMotorInverted();
}
