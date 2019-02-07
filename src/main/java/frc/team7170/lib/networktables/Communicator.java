package frc.team7170.lib.networktables;

import edu.wpi.first.networktables.NetworkTableEntry;
import frc.team7170.lib.Named;

// TODO: Make a more general version of this (no NT entry) to extend a DataLogger class from and control data logging from annotations
public abstract class Communicator implements Named {

    private final NetworkTableEntry entry;

    protected Communicator(NetworkTableEntry entry) {
        this.entry = entry;
    }

    public abstract boolean start();

    public abstract boolean cancel();

    public abstract boolean isRunning();

    public abstract void invoke();

    public NetworkTableEntry getEntry() {
        return entry;
    }

    @Override
    public String getName() {
        return entry.getName();
    }
}
