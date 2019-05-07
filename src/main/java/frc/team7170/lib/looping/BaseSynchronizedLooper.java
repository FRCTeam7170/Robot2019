package frc.team7170.lib.looping;

/**
 * A base, synchronized {@link Looper Looper} implementation which fully satisfies the {@code Looper} contract, but has
 * no functionality for automatically calling {@link BaseLooper#loop() loop}.
 *
 * @apiNote Two separate {@code Looper} base classes ({@link BaseLooper BaseLooper} and {@code BaseSynchronizedLooper})
 * exist so that non-threaded {@code Looper} implementations need not suffer from synchronization overhead.
 *
 * @author Robert Russell
 * @see BaseLooper
 */
public class BaseSynchronizedLooper extends BaseLooper {

    @Override
    public synchronized void registerLoop(Loop loop) {
        super.registerLoop(loop);
    }

    @Override
    public synchronized void removeLoop(Loop loop) {
        super.removeLoop(loop);
    }

    @Override
    public synchronized void startLoops() {
        super.startLoops();
    }

    @Override
    public synchronized void loop() {
        super.loop();
    }

    @Override
    public synchronized void stopLoops() {
        super.stopLoops();
    }

    @Override
    public synchronized boolean isRunning() {
        return super.isRunning();
    }
}
