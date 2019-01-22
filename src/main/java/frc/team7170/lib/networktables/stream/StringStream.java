package frc.team7170.lib.networktables.stream;

import edu.wpi.first.networktables.NetworkTableEntry;

public class StringStream extends NTStream<String> {

    private static final String[] empty = new String[0];

    public class Builder extends NTStream.Builder {
        @Override
        protected boolean isMatchingEntryType(NetworkTableEntry entry) {
            switch (entry.getType()) {
                case kString:
                case kStringArray:
                    return true;
            }
            return false;
        }

        @Override
        void setUnassignedEntry(NetworkTableEntry entry) {
            entry.setStringArray(empty);
        }

        @Override
        protected NTStream buildInstance() {
            return new StringStream(this);
        }
    }

    private StringStream(Builder builder) {
        super(builder);
    }

    @Override
    protected String[] getReceiving() {
        return receivingEntry.getStringArray(empty);
    }

    @Override
    protected void clearReceiving() {
        receivingEntry.setStringArray(empty);
    }

    @Override
    protected void setTransmitting(String[] data) {
        transmittingEntry.setStringArray(data);
    }

    @Override
    protected String[] getTransmitting() {
        return transmittingEntry.getStringArray(empty);
    }
}
