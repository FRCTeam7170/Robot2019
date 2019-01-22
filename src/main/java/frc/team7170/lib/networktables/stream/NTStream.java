package frc.team7170.lib.networktables.stream;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableType;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public abstract class NTStream<T> {

    public static class Builder {

        private final NetworkTableType kind;
        private NetworkTableEntry receivingEntry;
        private NetworkTableEntry transmittingEntry;
        private int cacheSize = 100;
        private int minSendSize = 0;

        public Builder(NetworkTableType kind) {
            switch (kind) {
                case kBoolean:
                case kBooleanArray:
                case kDouble:
                case kDoubleArray:
                case kString:
                case kStringArray:
                    break;
                default:
                    throw new IllegalArgumentException("only the three basic NetworkTableTypes and " +
                            "their array variants are supported");
            }
            this.kind = kind;
        }

        public Builder receivingEntry(NetworkTableEntry receivingEntry) {
            assertMatchingEntryType(receivingEntry, kind);
            this.receivingEntry = receivingEntry;
            return this;
        }

        public Builder transmittingEntry(NetworkTableEntry transmittingEntry) {
            assertMatchingEntryType(transmittingEntry, kind);
            this.transmittingEntry = transmittingEntry;
            return this;
        }

        public Builder cacheSize(int cacheSize) {
            this.cacheSize = cacheSize;
            return this;
        }

        public Builder minSendSize(int minSendSize) {
            this.minSendSize = minSendSize;
            return this;
        }

        public NTStream build() {
            if (receivingEntry == null && transmittingEntry == null) {
                throw new IllegalStateException("at least one entry must be given");
            }
            if (transmittingEntry != null && cacheSize < minSendSize) {
                throw new IllegalStateException("cacheSize must be bigger than minSendSize");
            }
            return new NTStream(this);
        }

        private static void assertMatchingEntryType(NetworkTableEntry entry, NetworkTableType kind) {
            NetworkTableType entryType = entry.getType();
            switch (kind) {
                case kBoolean:
                case kBooleanArray:
                    if (entryType == NetworkTableType.kUnassigned) {
                        entry.setBooleanArray(new boolean[] {});
                    } else if (entryType != NetworkTableType.kBooleanArray) {
                        entryTypeDiscrepancyError(entryType, kind);
                    }
                    break;
                case kDouble:
                case kDoubleArray:
                    if (entryType == NetworkTableType.kUnassigned) {
                        entry.setDoubleArray(new double[] {});
                    } else if (entryType != NetworkTableType.kDoubleArray) {
                        entryTypeDiscrepancyError(entryType, kind);
                    }
                    break;
                case kString:
                case kStringArray:
                    if (entryType == NetworkTableType.kUnassigned) {
                        entry.setStringArray(new String[] {});
                    } else if (entryType != NetworkTableType.kStringArray) {
                        entryTypeDiscrepancyError(entryType, kind);
                    }
                    break;
            }
        }

        private static void entryTypeDiscrepancyError(NetworkTableType entryType, NetworkTableType kind) {
            throw new IllegalStateException("entry type (" + entryType.name() + ") and given data type ("
                    + kind.name() + ") conflict");
        }
    }

    private static class LimitedCache<E> extends LinkedList<E> {
        // TODO: this is a pretty naive implementation if memory is a concern (which it isn't, but it's worth noting)

        private final int capacity;

        public LimitedCache(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            boolean ret = super.addAll(c);
            checkCapacity();
            return ret;
        }

        private void checkCapacity() {
            while (size() > capacity) {
                remove();
            }
        }
    }

    private final NetworkTableType kind;
    private final NetworkTableEntry receivingEntry;
    private final NetworkTableEntry transmittingEntry;
    private final int minSendSize;
    private final LimitedCache<T> cache;

    private NTStream(Builder builder) {
        // Sanity checks are done in the builder.
        this.kind = builder.kind;
        this.receivingEntry = builder.receivingEntry;
        this.transmittingEntry = builder.transmittingEntry;
        this.minSendSize = builder.minSendSize;
        cache = new LimitedCache<>(builder.cacheSize);
    }

    public T[] read() {
        assertReadable("cannot read a write-only stream");
        T[] data = rawRead();
        clearReceiving();
        return data;
    }

    public boolean write(Collection<T> data) {
        assertWriteable("cannot write to a read-only stream");
        cache.addAll(data);
        return flush(false);
    }

    public boolean write(T... data) {
        return write(Arrays.asList(data));
    }

    public boolean flush(boolean force) {
        assertWriteable("cannot flush a read-only stream");
        if () {

        }
        return false;
    }

    public boolean flush() {
        return flush(true);
    }

    private boolean outputBlocked() {

    }

    public boolean isReadable() {
        return receivingEntry != null;
    }

    public boolean isWriteable() {
        return transmittingEntry != null;
    }

    private void assertReadable(String msg) {
        if (!isReadable()) {
            throw new IllegalStateException(msg);
        }
    }

    private void assertWriteable(String msg) {
        if (!isWriteable()) {
            throw new IllegalStateException(msg);
        }
    }

    T[] rawRead() {
        return null;
    }

    void clearReceiving() {}

    void rawWrite() {}

    void getTransmitting() {}
}
