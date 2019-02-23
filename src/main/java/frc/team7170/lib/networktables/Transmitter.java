package frc.team7170.lib.networktables;

import edu.wpi.first.networktables.NetworkTableEntry;
import frc.team7170.lib.command.CmdTimedRunnable;

public class Transmitter extends Communicator {

    private final Runnable runnable;
    private final int pollRateMs;
    private CmdTimedRunnable cmdTimedRunnable;

    Transmitter(Runnable runnable, int pollRateMs, NetworkTableEntry entry) {
        super(entry);
        this.runnable = runnable;
        this.pollRateMs = pollRateMs;
    }

    @Override
    public boolean start() {
        if (!isRunning() && pollRateMs != TransmitFrequency.STATIC) {
            cmdTimedRunnable = new CmdTimedRunnable(runnable, pollRateMs);
            cmdTimedRunnable.start();
            return true;
        }
        return false;
    }

    @Override
    public boolean cancel() {
        if (isRunning()) {
            cmdTimedRunnable.cancel();
            cmdTimedRunnable = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean isRunning() {
        return cmdTimedRunnable != null;
    }

    @Override
    public void invoke() {
        runnable.run();
    }

    public int getPollRateMs() {
        return pollRateMs;
    }
}
