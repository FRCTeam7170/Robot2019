package frc.team7170.lib.routine;

import edu.wpi.first.wpilibj.Timer;

class BlockRoutine extends Routine {

    private final double timeoutSec;
    private double startTimeSec;

    BlockRoutine(int timeoutMs, boolean interruptable, Subsystem... requirements) {
        super(interruptable, requirements);
        this.timeoutSec = (double) timeoutMs / 1000.0;
    }

    @Override
    public String toString() {
        // "BlockRoutine(0.123s)"
        return super.toString() + String.format("(%.03fs)", timeoutSec);
    }

    @Override
    protected void onStart() {
        startTimeSec = Timer.getFPGATimestamp();
    }

    @Override
    protected boolean isFinished() {
        return Timer.getFPGATimestamp() >= (startTimeSec + timeoutSec);
    }
}
