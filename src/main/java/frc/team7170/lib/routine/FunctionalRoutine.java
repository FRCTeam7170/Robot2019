package frc.team7170.lib.routine;

import frc.team7170.lib.BooleanConsumer;

import java.util.Objects;
import java.util.function.BooleanSupplier;

class FunctionalRoutine extends Routine {

    static final Runnable EMPTY_RUNNABLE = () -> {};
    static final BooleanConsumer EMPTY_BOOLEAN_CONSUMER = b -> {};
    static final BooleanSupplier TRUE_BOOLEAN_SUPPLIER = () -> true;
    static final BooleanSupplier FALSE_BOOLEAN_SUPPLIER = () -> false;

    private final Runnable onStart;
    private final Runnable onLoop;
    private final BooleanConsumer onStop;
    private final BooleanSupplier isFinished;

    FunctionalRoutine(Runnable onStart, Runnable onLoop, BooleanConsumer onStop, BooleanSupplier isFinished,
                      boolean interruptable, Subsystem... requirements) {
        super(interruptable, requirements);
        this.onStart = Objects.requireNonNull(onStart, "onStart must be non-null");
        this.onLoop = Objects.requireNonNull(onLoop, "onLoop must be non-null");
        this.onStop = Objects.requireNonNull(onStop, "onStop must be non-null");
        this.isFinished = Objects.requireNonNull(isFinished, "isFinished must be non-null");
    }

    @Override
    protected void onStart() {
        onStart.run();
    }

    @Override
    protected void onLoop() {
        onLoop.run();
    }

    @Override
    protected void onStop(boolean graceful) {
        onStop.accept(graceful);
    }

    @Override
    protected boolean isFinished() {
        return isFinished.getAsBoolean();
    }
}
