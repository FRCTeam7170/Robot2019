package frc.team7170.lib.networktables.stream;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableType;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public abstract class NTStream<T> {

    abstract static class Builder {

        private NetworkTableEntry receivingEntry;
        private NetworkTableEntry transmittingEntry;
        private int cacheSize = 100;
        private int minSendSize = 0;

        public Builder receivingEntry(NetworkTableEntry receivingEntry) {
            checkMatchingEntryType(receivingEntry);
            this.receivingEntry = receivingEntry;
            return this;
        }

        public Builder transmittingEntry(NetworkTableEntry transmittingEntry) {
            checkMatchingEntryType(transmittingEntry);
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
            return buildInstance();
        }

        private void checkMatchingEntryType(NetworkTableEntry entry) {
            NetworkTableType entryType = receivingEntry.getType();
            if (entryType == NetworkTableType.kUnassigned) {
                setUnassignedEntry(entry);
            } else if (!isMatchingEntryType(receivingEntry)) {
                throw new IllegalStateException("invalid entry type (" + entryType.name() + ")");
            }
        }

        abstract boolean isMatchingEntryType(NetworkTableEntry entry);

        abstract void setUnassignedEntry(NetworkTableEntry entry);

        abstract NTStream buildInstance();
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

    final NetworkTableEntry receivingEntry;
    final NetworkTableEntry transmittingEntry;
    private final int minSendSize;
    private final LimitedCache<T> cache;

    NTStream(Builder builder) {
        // Sanity checks are done in the builder.
        this.receivingEntry = builder.receivingEntry;
        this.transmittingEntry = builder.transmittingEntry;
        this.minSendSize = builder.minSendSize;
        cache = new LimitedCache<>(builder.cacheSize);
    }

    public T[] read() {
        assertReadable("cannot read a write-only stream");
        T[] data = getReceiving();
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

    @SuppressWarnings("unchecked")
    public boolean flush(boolean force) {
        assertWriteable("cannot flush a read-only stream");
        if (!outputBlocked() && (force || enoughDataInCache())) {
            setTransmitting((T[]) cache.toArray());
            cache.clear();
            return true;
        }
        return false;
    }

    public boolean flush() {
        return flush(true);
    }

    private boolean outputBlocked() {
        return getTransmitting().length > 0;
    }

    private boolean enoughDataInCache() {
        return cache.size() >= minSendSize;
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

    abstract T[] getReceiving();

    abstract void clearReceiving();

    abstract void setTransmitting(T[] data);

    abstract T[] getTransmitting();
}
