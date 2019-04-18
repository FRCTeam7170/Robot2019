package frc.team7170.lib.looping;

import java.util.HashSet;
import java.util.Set;

public class BaseLooper implements Looper {

    private final Set<Loop> loops = new HashSet<>();
    private boolean running = false;

    @Override
    public synchronized void registerLoop(Loop loop) {
        loops.add(loop);
        if (running) {
            loop.onStart();
        }
    }

    @Override
    public synchronized void removeLoop(Loop loop) throws IllegalArgumentException {
        if (!loops.remove(loop)) {
            throw new IllegalArgumentException("given loop not in looper");
        }
        if (running) {
            loop.onStop();
        }
    }

    @Override
    public synchronized void startLoops() throws IllegalStateException {
        if (!running) {
            for (Loop loop : loops) {
                loop.onStart();
            }
            running = true;
        } else {
            throw new IllegalStateException("looper already started");
        }
    }

    @Override
    public synchronized void loop() throws IllegalStateException {
        if (running) {
            for (Loop loop : loops) {
                loop.onLoop();
            }
        } else {
            throw new IllegalStateException("looper not started");
        }
    }

    @Override
    public synchronized void stopLoops() throws IllegalStateException {
        if (running) {
            running = false;
            for (Loop loop : loops) {
                loop.onStop();
            }
        } else {
            throw new IllegalStateException("looper already stopped");
        }
    }

    @Override
    public synchronized boolean isRunning() {
        return running;
    }
}
