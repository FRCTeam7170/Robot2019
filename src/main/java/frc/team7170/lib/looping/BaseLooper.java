package frc.team7170.lib.looping;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A base, non-synchronized {@link Looper Looper} implementation which fully satisfies the {@code Looper} contract, but
 * has no functionality for automatically calling {@link BaseLooper#loop() loop}.
 *
 * @apiNote Two separate {@code Looper} base classes ({@code BaseLooper} and
 * {@link BaseSynchronizedLooper BaseSynchronizedLooper}) exist so that non-threaded {@code Looper} implementations need
 * not suffer from synchronization overhead.
 *
 * @author Robert Russell
 * @see BaseSynchronizedLooper
 */
public class BaseLooper implements Looper {

    /**
     * The set of {@code Loop}s contained by this {@code BaseLooper}.
     */
    private final Set<Loop> loops = new HashSet<>();

    /**
     * Whether or not this {@code BaseLooper} is currently running.
     */
    private boolean running = false;

    @Override
    public void registerLoop(Loop loop) {
        if (loops.contains(Objects.requireNonNull(loop, "cannot register null Loop"))) {
            throw new IllegalArgumentException("given Loop already registered");
        }
        loops.add(loop);
        if (running) {
            loop.onStart();
        }
    }

    @Override
    public void removeLoop(Loop loop) {
        if (!loops.remove(Objects.requireNonNull(loop, "cannot remove null Loop"))) {
            throw new IllegalArgumentException("given Loop not in Looper");
        }
        if (running) {
            loop.onStop();
        }
    }

    @Override
    public void startLoops() {
        if (!running) {
            for (Loop loop : loops) {
                loop.onStart();
            }
            running = true;
        } else {
            throw new IllegalStateException("Looper already started");
        }
    }

    @Override
    public void loop() {
        if (running) {
            for (Loop loop : loops) {
                loop.onLoop();
            }
        } else {
            throw new IllegalStateException("Looper not started");
        }
    }

    @Override
    public void stopLoops() {
        if (running) {
            running = false;
            for (Loop loop : loops) {
                loop.onStop();
            }
        } else {
            throw new IllegalStateException("Looper already stopped");
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
