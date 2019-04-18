package frc.team7170.lib.looping;

public class ThreadedLooper extends BaseLooper {

    private class LooperThread extends Thread {

        private LooperThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                loop();
                try {
                    Thread.sleep(loopPeriodMs);
                } catch (InterruptedException e) {
                    // Normally one would re-set the interrupt flag here, but breaking has the same effect.
                    break;
                }
            }
        }
    }

    private final int loopPeriodMs;
    private Thread thread;

    public ThreadedLooper(int loopPeriodMs) {
        this.loopPeriodMs = loopPeriodMs;
    }

    @Override
    public synchronized void startLoops() throws IllegalStateException {
        super.startLoops();
        thread = new LooperThread();
        thread.start();
    }

    @Override
    public synchronized void stopLoops() throws IllegalStateException {
        super.stopLoops();
        thread.interrupt();
        thread = null;
    }
}
