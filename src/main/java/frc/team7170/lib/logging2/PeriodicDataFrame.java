package frc.team7170.lib.logging2;

import frc.team7170.lib.looping.Loop;
import frc.team7170.lib.looping.Looper;
import frc.team7170.lib.looping.ThreadedLooper;

public class PeriodicDataFrame extends StaticDataFrame implements Loop {

    private final Looper looper;

    public PeriodicDataFrame(DataLogger dataLogger, int pollPeriodMs) {
        super(dataLogger);
        looper = new ThreadedLooper(pollPeriodMs);
        looper.registerLoop(this);
        looper.startLoops();
    }

    @Override
    public void onStart() {}

    @Override
    public void onLoop() {
        update();
    }

    @Override
    public void onStop() {}
}
