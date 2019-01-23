package frc.team7170.lib.networktables.stream;

import edu.wpi.first.networktables.NetworkTableEntry;

public class StringStream extends NTStream<String> {

    private static final String[] empty = new String[0];

    public static class Config extends NTStream.Config {
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
    }

    public StringStream(NTStream.Config config) {
        super(config);
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
