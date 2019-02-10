package frc.team7170.lib.wrappers;

abstract class AbstractEncoder extends AbstractSensor implements Encoder {

    private int ticksPerRotation = 0;
    private double metresPerRotation = 0.0;
    private double maxRotationRate = 0.001;

    // ticks
    abstract int getEncoderRaw();

    // ticks/s
    abstract double getEncoderRawRate();

    @Override
    public double getEncoder() {
        return (double) getEncoderRaw() / ticksPerRotation;
    }

    @Override
    public double getEncoderRotationRate() {
        return getEncoderRawRate() / ticksPerRotation;
    }

    @Override
    public double getEncoderDistance() {
        return getEncoder() * metresPerRotation;
    }

    @Override
    public double getEncoderDistanceRate() {
        return getEncoderRotationRate() * metresPerRotation;
    }

    @Override
    public boolean isEncoderStopped() {
        return getEncoderRotationRate() <= maxRotationRate;
    }

    @Override
    public boolean getEncoderDirection() {
        return getEncoderRotationRate() > 0.0;
    }

    @Override
    public void setTicksPerRotation(int ticksPerRotation) {
        this.ticksPerRotation = ticksPerRotation;
    }

    @Override
    public int getTicksPerRotation() {
        return ticksPerRotation;
    }

    @Override
    public void setDistancePerRotation(double metresPerRotation) {
        this.metresPerRotation = metresPerRotation;
    }

    @Override
    public double getDistancePerRotation() {
        return metresPerRotation;
    }

    @Override
    public void setMaxRotationRate(double maxRotationRate) {
        this.maxRotationRate = maxRotationRate;
    }

    @Override
    public double getMaxRotationRate() {
        return maxRotationRate;
    }
}
