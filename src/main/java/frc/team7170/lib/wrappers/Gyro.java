package frc.team7170.lib.wrappers;

public interface Gyro {

    // degrees
    double getXAngle();

    // degrees
    double getYAngle();

    // degrees
    double getZAngle();

    // degrees/s
    double getXAngleRate();

    // degrees/s
    double getYAngleRate();

    // degrees/s
    double getZAngleRate();

    void calibrateGyro();

    void resetGyro();
}
