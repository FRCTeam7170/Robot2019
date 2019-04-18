package frc.team7170.lib.looping;

public interface Looper {

    void registerLoop(Loop loop);

    void removeLoop(Loop loop) throws IllegalArgumentException;

    void startLoops() throws IllegalStateException;

    void loop() throws IllegalStateException;

    void stopLoops() throws IllegalStateException;

    boolean isRunning();
}
