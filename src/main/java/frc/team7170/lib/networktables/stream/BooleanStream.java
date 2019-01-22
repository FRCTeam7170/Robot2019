package frc.team7170.lib.networktables.stream;

import edu.wpi.first.networktables.NetworkTableEntry;

public class BooleanStream extends NTStream<Boolean> {

    private static final Boolean[] empty = new Boolean[0];

    public class Builder extends NTStream.Builder {
        @Override
        protected boolean isMatchingEntryType(NetworkTableEntry entry) {
            switch (entry.getType()) {
                case kBoolean:
                case kBooleanArray:
                    return true;
            }
            return false;
        }

        @Override
        void setUnassignedEntry(NetworkTableEntry entry) {
            entry.setBooleanArray(empty);
        }

        @Override
        protected NTStream buildInstance() {
            return new BooleanStream(this);
        }
    }

    private BooleanStream(Builder builder) {
        super(builder);
    }

    @Override
    protected Boolean[] getReceiving() {
        return receivingEntry.getBooleanArray(empty);
    }

    @Override
    protected void clearReceiving() {
        receivingEntry.setBooleanArray(empty);
    }

    @Override
    protected void setTransmitting(Boolean[] data) {
        transmittingEntry.setBooleanArray(data);
    }

    @Override
    protected Boolean[] getTransmitting() {
        return transmittingEntry.getBooleanArray(empty);
    }
}
