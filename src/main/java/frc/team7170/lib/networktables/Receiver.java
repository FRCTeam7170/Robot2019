package frc.team7170.lib.networktables;

import edu.wpi.first.networktables.*;

import java.util.function.Consumer;

public class Receiver extends Communicator {

    private final Consumer<EntryNotification> consumer;
    private final int flags;
    private int listenerId;
    private boolean running = false;

    Receiver(Consumer<EntryNotification> consumer, int flags, NetworkTableEntry entry) {
        super(entry);
        this.consumer = consumer;
        this.flags = flags;
    }

    @Override
    public boolean start() {
        if (!running) {
            listenerId = getEntry().addListener(consumer, flags);
            running = true;
        }
        return false;
    }

    @Override
    public boolean cancel() {
        if (running) {
            getEntry().removeListener(listenerId);
            running = false;
        }
        return false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void invoke() {
        consumer.accept(new EntryNotification(
                NetworkTableInstance.getDefault(),
                listenerId,
                getEntry().getHandle(),
                getName(),
                getEntry().getValue(),
                EntryListenerFlags.kUpdate | EntryListenerFlags.kLocal
        ));
    }

    public int getFlags() {
        return flags;
    }
}
