package frc.team7170.lib.networktables.stream;

import edu.wpi.first.networktables.NetworkTableEntry;

public class DoubleStream extends NTStream<Double> {

    private static final Double[] empty = new Double[0];

    public static class Config extends NTStream.Config {
        @Override
        protected boolean isMatchingEntryType(NetworkTableEntry entry) {
            switch (entry.getType()) {
                case kDouble:
                case kDoubleArray:
                    return true;
            }
            return false;
        }

        @Override
        void setUnassignedEntry(NetworkTableEntry entry) {
            entry.setNumberArray(empty);
        }
    }

    public DoubleStream(NTStream.Config config) {
        super(config);
    }

    @Override
    protected Double[] getReceiving() {
        return receivingEntry.getDoubleArray(empty);
    }

    @Override
    protected void clearReceiving() {
        receivingEntry.setNumberArray(empty);
    }

    @Override
    protected void setTransmitting(Double[] data) {
        transmittingEntry.setNumberArray(data);
    }

    @Override
    protected Double[] getTransmitting() {
        return transmittingEntry.getDoubleArray(empty);
    }
}
