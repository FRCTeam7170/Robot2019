package frc.team7170.lib.wrappers;

public interface Encoder {

    // Raw ticks
    int getEncoder();

    // m
    double getEncoderDistance();

    // m/s
    double getEncoderRate();

    boolean isEncoderStopped();

    // true if ticks increasing
    boolean getEncoderDirection();

    void setDistancePerEncoderPulse(double metresPerPulse);

    // m
    double getDistancePerEncoderPulse();

    void setEncoderInverted(boolean inverted);

    boolean isEncoderInverted();

    void resetEncoder();
}
