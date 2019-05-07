package frc.team7170.lib.looping;

/**
 * <p>
 * A loop with a {@linkplain Loop#onStart() start action}, {@linkplain Loop#onLoop() loop action}, and
 * {@linkplain Loop#onStop() stop action}.
 * </p>
 * <p>
 * {@code Loop}s are scheduled with {@link Looper Looper}s to be started, looped, and stopped. {@code Loop} behaviour
 * is undefined if {@linkplain Looper#registerLoop(Loop) registered} with more than one {@code Looper}.
 * </p>
 * <p>
 * The intended way of using {@code Loop} is through subclassing (especially with anonymous inner classes) and
 * overriding {@code onStart}, {@code onLoop}, and {@code onStop}.
 * </p>
 *
 * @apiNote This class is not strictly abstract (it contains no abstract methods), but directly instantiating
 * {@code Loop} would be pointless.
 *
 * @author Robert Russell
 * @see Looper
 */
public abstract class Loop {

    /**
     * Called whenever a {@code Loop} starts. This is guaranteed by {@link Looper Looper} to always be called once
     * before any potential {@link Loop#onLoop() onLoop} calls and never again until {@link Loop#onStop() onStop} is
     * called and the cycle repeats.
     *
     * @implNote Warning: if the {@code Looper} implementation used for this {@code Loop} is threaded, the contained
     * code must be thread-safe!
     */
    protected void onStart() {}

    /**
     * Called whenever a {@code Loop} loops. This is guaranteed by {@link Looper Looper} to only be called after a call
     * to {@link Loop#onStart() onStart} and before a call to {@link Loop#onStop() onStop}. Do note, however, that no
     * calls to {@code onLoop} may be made if a {@code Loop} is stopped before any iterations occur.
     *
     * @implNote Warning: if the {@code Looper} implementation used for this {@code Loop} is threaded, the contained
     * code must be thread-safe!
     */
    protected void onLoop() {}

    /**
     * Called whenever a {@code Loop} stops. This is guaranteed by {@link Looper Looper} to always (unless a crash
     * occurs) be called once after {@link Loop#onStart() onStart} and any potential {@link Loop#onLoop() onLoop} calls
     * and never again until the cycle repeats.
     *
     * @implNote Warning: if the {@code Looper} implementation used for this {@code Loop} is threaded, the contained
     * code must be thread-safe!
     */
    protected void onStop() {}
}
