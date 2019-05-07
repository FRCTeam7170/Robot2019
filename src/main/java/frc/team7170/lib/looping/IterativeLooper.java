package frc.team7170.lib.looping;

import frc.team7170.lib.PeriodicRunnable;

/**
 * A non-threaded {@link Looper Looper} meant to be {@linkplain IterativeLooper#run() updated} in some external loop
 * ("loop" as in a {@code for} or {@code while} loop, for example, <em>not</em> a {@link Loop Loop}). Contained
 * {@code Loop}s will be {@linkplain Loop#onLoop() looped} once a duration of time greater than or equal to the given
 * "loop period" has passed; this duration of time will match the given loop period better and better as the frequency
 * of calls to {@link IterativeLooper#run() run} increases.
 *
 * @author Robert Russell
 */
public class IterativeLooper extends BaseLooper {

    /**
     * The backing {@code PeriodicRunnable} responsible for timing invocations of {@link Looper#loop() loop}.
     */
    private final PeriodicRunnable periodicRunnable;

    /**
     * @param loopPeriodMs the minimum loop period in milliseconds. If practice, the loop period will always be slightly
     *                     greater than this depending on how frequently {@link IterativeLooper#run() run} is called.
     * @throws IllegalArgumentException if the given loop period is negative.
     */
    public IterativeLooper(int loopPeriodMs) {
        periodicRunnable = new PeriodicRunnable(this::loop, loopPeriodMs);
    }

    /**
     * <p>
     * Check if the time past since the last invocation of {@link Looper#loop() loop} is at least the loop period given
     * upon construction and run {@code loop} if so.
     * </p>
     * <p>
     * Calls to this method should be place in a really fast loop in the same thread that the contained {@code Loop}s
     * are expected to run in.
     * </p>
     *
     * @throws IllegalStateException if this {@code IterativeLooper} is not running.
     */
    public void run() {
        if (isRunning()) {
            periodicRunnable.run();
        } else {
            throw new IllegalStateException("Looper not started");
        }
    }
}
