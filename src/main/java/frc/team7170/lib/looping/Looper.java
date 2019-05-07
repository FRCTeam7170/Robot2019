package frc.team7170.lib.looping;

/**
 * <p>
 * A manager of {@link Loop Loop}s. {@code Looper}s take care of {@linkplain Loop#onStart() starting},
 * {@linkplain Loop#onLoop() looping}, and {@linkplain Loop#onStop() stopping} {@code Loop}s at the appropriate times.
 * </p>
 * <p>
 * {@code Looper}s manage {@code Loop}s according to the following rules ({@code Loop}s are scheduled in a cyclical
 * fashion with each cycle beginning when {@link Loop#onStart() onStart} is called and ending when
 * {@link Loop#onStop() onStop} is called; any use of the terms "before" and "after" are used in the context of the
 * current cycle):
 * <ul>
 *     <li>
 *         {@link Loop#onStart() onStart} is always called before any potential {@link Loop#onLoop() onLoop} calls and
 *         {@link Loop#onStop() onStop};
 *     </li>
 *     <li>
 *         {@link Loop#onLoop() onLoop} is called zero or more times after {@link Loop#onStart() onStart} and before
 *         {@link Loop#onStop() onStop};
 *     </li>
 *     <li>
 *         {@link Loop#onStop() onStop} is always called after {@link Loop#onStart() onStart} and any potential
 *         {@link Loop#onLoop() onLoop} calls;
 *     </li>
 *     <li>
 *         none of the above specifications are guaranteed if a {@code Loop} is
 *         {@linkplain Looper#registerLoop(Loop) registered} with more than one {@code Looper};
 *     </li>
 *     <li>
 *         {@link Loop#onStop() onStop} may not be called if an error occurs at runtime.
 *     </li>
 * </ul>
 * </p>
 *
 * @implSpec {@code Looper}s may be either threaded or not so long as they obey the above rules.
 *
 * @author Robert Russell
 * @see Loop
 */
public interface Looper {

    /**
     * Register the given {@link Loop Loop} with this {@code Looper}.
     *
     * @implSpec If a {@code Loop} is added while a {@code Looper} is running, {@link Loop#onStart() onStart} should be
     * immediately called.
     *
     * @param loop the {@code Loop} to register.
     * @throws NullPointerException if the given {@code Loop} is {@code null}.
     * @throws IllegalArgumentException if the given {@code Loop} is already registered with this {@code Looper}.
     */
    void registerLoop(Loop loop);

    /**
     * Remove the given {@link Loop Loop} from this {@code Looper}. If the {@code Looper} is running when the given
     * {@code Loop} is removed, the {@code Loop} will be {@linkplain Loop#onStop() stopped}.
     *
     * @param loop the {@code Loop} to remove.
     * @throws NullPointerException if the given {@code Loop} is {@code null}.
     * @throws IllegalArgumentException if the given {@code Loop} is not registered with this {@code Looper}.
     */
    void removeLoop(Loop loop);

    /**
     * Start the {@code Looper}. This means {@linkplain Loop#onStart() starting} all the contained {@link Loop Loop}s
     * and allowing the invocation of {@link Looper#loop() loop}.
     *
     * @throws IllegalStateException if the {@code Looper} is already running.
     */
    void startLoops();

    /**
     * Run one iteration of the {@code Looper}. This means {@linkplain Loop#onLoop() looping} all the contained
     * {@link Loop Loop}s.
     *
     * @throws IllegalStateException if the {@code Looper} is not running.
     */
    void loop();

    /**
     * Stop the {@code Looper}. This means {@linkplain Loop#onStop() stopping} all the contained {@link Loop Loop}s and
     * disallowing the invocation of {@link Looper#loop() loop}.
     *
     * @throws IllegalStateException if the {@code Looper} is already not running.
     */
    void stopLoops();

    /**
     * Get whether or not the {@code Looper} is currently running.
     *
     * @return whether or not the {@code Looper} is currently running.
     */
    boolean isRunning();
}
