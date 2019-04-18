package frc.team7170.lib;

import edu.wpi.first.wpilibj.Timer;

public class PeriodicRunnable implements Runnable {

    private final Runnable runnable;
    private final double delaySec;
    private double lastTime = Timer.getFPGATimestamp();

    public PeriodicRunnable(Runnable runnable, int delayMs) {
        this.runnable = runnable;
        this.delaySec = (double) delayMs / 1000.0;
    }

    @Override
    public void run() {
        if (Timer.getFPGATimestamp() >= lastTime + delaySec) {
            runnable.run();
            lastTime = Timer.getFPGATimestamp();
        }
    }
}
