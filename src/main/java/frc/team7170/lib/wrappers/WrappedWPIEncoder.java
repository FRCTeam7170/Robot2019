package frc.team7170.lib.wrappers;

import edu.wpi.first.wpilibj.Encoder;

public class WrappedWPIEncoder extends AbstractEncoder {

    private final Encoder encoder;
    private boolean inverted;

    WrappedWPIEncoder(Encoder encoder, int cyclesPerRotation) {
        this.encoder = encoder;

        encoder.setDistancePerPulse(1.0);
        setTicksPerRotation(cyclesPerRotation);
        setDistancePerRotation(1.0);
        setEncoderInverted(false);
    }

    @Override
    int getEncoderRaw() {
        return encoder.get();
    }

    @Override
    double getEncoderRawRate() {
        return encoder.getRate();
    }

    @Override
    public void setEncoderInverted(boolean inverted) {
        this.inverted = inverted;
        encoder.setReverseDirection(inverted);
    }

    @Override
    public boolean isEncoderInverted() {
        return inverted;
    }

    @Override
    public void resetEncoder() {
        encoder.reset();
    }
}
