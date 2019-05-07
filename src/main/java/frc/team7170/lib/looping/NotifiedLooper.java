package frc.team7170.lib.looping;

import edu.wpi.first.wpilibj.Notifier;

/**
 * <p>
 * A threaded {@link Looper Looper} that employs a {@link Notifier Notifier} to periodically call
 * {@link Looper#loop() loop}.
 * </p>
 * <p>
 * Since {@code NotifiedLooper}s are threaded, all {@link Loop Loop}s {@linkplain Looper#registerLoop(Loop) registered}
 * with {@code NotifiedLooper}s must be thread-safe.
 * </p>
 *
 * @author Robert Russell
 */
public class NotifiedLooper extends BaseSynchronizedLooper {

    /**
     * The loop period in seconds.
     */
    private final double loopPeriodSec;

    /**
     * The {@link Notifier Notifier} responsible for periodically calling {@link Looper#loop() loop}.
     */
    private final Notifier notifier = new Notifier(this::loop);

    /**
     * @param loopPeriodMs the loop period in milliseconds.
     * @throws IllegalArgumentException if the given loop period is negative.
     */
    public NotifiedLooper(int loopPeriodMs) {
        if (loopPeriodMs < 0) {
            throw new IllegalArgumentException("negative loop period");
        }
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
