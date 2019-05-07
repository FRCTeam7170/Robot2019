package frc.team7170.lib.looping;

import edu.wpi.first.wpilibj.Notifier;

/**
 * @author Robert Russell
 */
public class NotifiedLooper extends BaseSynchronizedLooper {

    private final double loopPeriodSec;
    private final Notifier notifier = new Notifier(this::loop);

    public NotifiedLooper(int loopPeriodMs) {
        loopPeriodSec = (double) loopPeriodMs / 1000.0;
    }

    @Override
    public synchronized void startLoops() {
        super.startLoops();
        notifier.startPeriodic(loopPeriodSec);
    }

    @Override
    public synchronized void stopLoops() {
        super.stopLoops();
        notifier.stop();
    }
}
