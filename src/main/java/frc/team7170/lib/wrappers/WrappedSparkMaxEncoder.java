package frc.team7170.lib.wrappers;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

// TODO: state here isn't enforced elsewhere...
class WrappedSparkMaxEncoder extends AbstractEncoder {

    private final CANSparkMax sparkMax;
    private final CANEncoder encoder;
    private boolean inverted = false;
    private double offset = 0.0;

    WrappedSparkMaxEncoder(CANSparkMax sparkMax, int cyclesPerRotation) {
        this.sparkMax = sparkMax;
        this.encoder = sparkMax.getEncoder();

        setTicksPerRotation(cyclesPerRotation);
        setDistancePerRotation(1.0);
    }

    @Override
    int getEncoderRaw() {
        throw new UnsupportedOperationException();
    }

    @Override
    double getEncoderRawRate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getEncoder() {
        return applyInvert(encoder.getPosition() - offset);
    }

    @Override
    public double getEncoderRotationRate() {
        return applyInvert(encoder.getVelocity());
    }

    private double applyInvert(double value) {
        return inverted ? -value : value;
    }

    @Override
    public void setTicksPerRotation(int ticksPerRotation) {
        super.setTicksPerRotation(ticksPerRotation);
        sparkMax.setParameter(CANSparkMaxLowLevel.ConfigParameter.kEncoderCountsPerRev, ticksPerRotation);
    }

    @Override
    public void setEncoderInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public boolean isEncoderInverted() {
        return inverted;
    }

    // TODO: changes from this not reflected in actual motor... where else is this true?
    @Override
    public void resetEncoder() {
        offset = getEncoderRaw();
    }
}
