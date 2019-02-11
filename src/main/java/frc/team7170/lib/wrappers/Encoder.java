package frc.team7170.lib.wrappers;

public interface Encoder extends Sensor {

    // rotations
    double getEncoder();

    // rotations/s
    double getEncoderRotationRate();

    // distance
    double getEncoderDistance();

    // distance/s
    double getEncoderDistanceRate();

    boolean isEncoderStopped();

    // true if getEncoder() increasing
    boolean getEncoderDirection();

    void setTicksPerRotation(int ticksPerRotation);

    // ticks/rotation
    int getTicksPerRotation();

    void setDistancePerRotation(double distancePerRotation);

    // m/rotation
    double getDistancePerRotation();

    void setEncoderInverted(boolean inverted);

    boolean isEncoderInverted();

    // rotations/s
    void setMaxRotationRate(double maxRotationRate);

    // rotations/s
    double getMaxRotationRate();

    // m/s
    default void setMaxDistanceRate(double maxDistanceRate) {
        setMaxRotationRate(maxDistanceRate / getDistancePerRotation());
    }

    // m/s
    default double getMaxDistanceRate() {
        return getMaxRotationRate() * getDistancePerRotation();
    }

    void resetEncoder();

    @Override
    default double pidGet() {
        switch (getPIDSourceType()) {
            case kDisplacement:
                return getEncoderDistance();
            case kRate:
                return getEncoderDistanceRate();
            default:
                throw new IllegalStateException("invalid PIDSourceType");
        }
    }
}
