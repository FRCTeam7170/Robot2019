package frc.team7170.lib.networktables.stream;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.EntryNotification;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

public abstract class NTStream<T> {

    public abstract static class Config {

        private NetworkTableEntry receivingEntry;
        private NetworkTableEntry transmittingEntry;
        private int cacheSize = 100;
        private int minSendSize = 0;

        public Config receivingEntry(NetworkTableEntry receivingEntry) {
            checkMatchingEntryType(receivingEntry);
            this.receivingEntry = receivingEntry;
            return this;
        }

        public Config transmittingEntry(NetworkTableEntry transmittingEntry) {
            checkMatchingEntryType(transmittingEntry);
            this.transmittingEntry = transmittingEntry;
            return this;
        }

        public Config cacheSize(int cacheSize) {
            this.cacheSize = cacheSize;
            return this;
        }

        public Config minSendSize(int minSendSize) {
            this.minSendSize = minSendSize;
            return this;
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
    }

    private static class LimitedCache<E> extends LinkedList<E> {
        // This is a pretty naive implementation if memory is a concern (which it isn't, but it's worth noting)
        // (Because addAll adds all the given elements *before* trimming to the max capacity)

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

    private static class Listener {

        public final Consumer<EntryNotification> callback;
        public final int flags;
        public final boolean peekOnly;

        public Listener(Consumer<EntryNotification> callback, int flags, boolean peekOnly) {
            this.callback = callback;
            this.flags = flags;
            this.peekOnly = peekOnly;
        }
    }

    // Attributes for receiving streams:
    final NetworkTableEntry receivingEntry;
    private int listenerFlags = 0;
    private int listenerCallbackID;
    private final ArrayList<Listener> listeners;

    // Attributes for transmitting streams:
    final NetworkTableEntry transmittingEntry;
    private final int minSendSize;
    private final LimitedCache<T> cache;

    NTStream(Config config) {
        if (config.receivingEntry == null && config.transmittingEntry == null) {
            throw new IllegalStateException("at least one entry must be given");
        }
        if (config.transmittingEntry != null && config.cacheSize < config.minSendSize) {
            throw new IllegalStateException("cacheSize must be bigger than minSendSize");
        }
        this.receivingEntry = config.receivingEntry;
        this.transmittingEntry = config.transmittingEntry;
        this.minSendSize = config.minSendSize;

        listeners = isReadable() ? new ArrayList<>() : null;
        cache = isWriteable() ? new LimitedCache<>(config.cacheSize) : null;
    }

    public T[] read() {
        assertReadable("cannot read a write-only stream");
        T[] data = getReceiving();
        clearReceiving();
        return data;
    }

    public T[] peek() {
        assertReadable("cannot read a write-only stream");
        return getReceiving();
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

    public void addListener(Consumer<EntryNotification> callback, int flags, boolean peakOnly) {
        assertReadable("cannot add a listener to a write-only stream");
        checkListeningWithFlags(flags);
        listeners.add(new Listener(callback, flags, peakOnly));
    }

    public void addListener(Consumer<EntryNotification> callback) {
        addListener(callback, EntryListenerFlags.kUpdate, false);
    }

    public void removeListener(Consumer<EntryNotification> callback) {
        Listener listener = null;
        for (Listener l : listeners) {
            if (l.callback == callback) {
                listener = l;
                break;
            }
        }
        if (listener == null) {
            throw new IllegalArgumentException("given listener was not previously registered and could not be removed");
        }
        listeners.remove(listener);
        if (listeners.isEmpty()) {
            receivingEntry.removeListener(listenerCallbackID);
            listenerFlags = 0;
        }
    }

    private void checkListeningWithFlags(int flags) {
        int mergedFlags = listenerFlags | flags;
        if (mergedFlags != listenerFlags) {
            // listenerFlags are only zero if no listener has yet been registered.
            if (listenerFlags != 0) {
                receivingEntry.removeListener(listenerCallbackID);
            }
            listenerCallbackID = receivingEntry.addListener(this::listenerCallback, mergedFlags);
            listenerFlags = mergedFlags;
        }
    }

    private void listenerCallback(EntryNotification notification) {
        boolean clearAfter = false;
        for (Listener listener : listeners) {
            if ((listener.flags & notification.flags) == notification.flags) {
                if (!listener.peekOnly) {
                    clearAfter = true;
                }
                listener.callback.accept(notification);
            }
        }
        if (clearAfter) {
            clearReceiving();
        }
    }

    abstract T[] getReceiving();

    abstract void clearReceiving();

    abstract void setTransmitting(T[] data);

    abstract T[] getTransmitting();
}
