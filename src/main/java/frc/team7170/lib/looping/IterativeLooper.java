package frc.team7170.lib.looping;

import frc.team7170.lib.PeriodicRunnable;

public class IterativeLooper extends BaseLooper {

    private final PeriodicRunnable periodicRunnable;

    public IterativeLooper(int loopPeriodMs) {
        periodicRunnable = new PeriodicRunnable(this::loop, loopPeriodMs);
    }

    public void run() {
        if (isRunning()) {
            periodicRunnable.run();
        } else {
            throw new IllegalStateException("looper not started");
        }
    }
}
