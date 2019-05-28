package frc.team7170.lib.routine;

import frc.team7170.lib.BooleanConsumer;
import frc.team7170.lib.looping.Loop;

import java.util.*;
import java.util.function.BooleanSupplier;

// TODO: comment on why essentially remaking Command system; one-time use; immutability

/**
 * Routine impl ideas:
 * conditional routines
 * deadline group
 * debug wrapper / logging
 */
public abstract class Routine {

    private class RoutineLoop extends Loop {

        private boolean gracefulExit = false;
        private List<Routine> interruptOnStop;

        @Override
        protected void onStart() {
            Routine.this.onStart();
        }

        @Override
        protected void onLoop() {
            Routine.this.onLoop();
            if (isFinished()) {
                gracefulExit = true;
                scheduler.unschedule(Routine.this);
            }
        }

        @Override
        protected void onStop() {
            interruptOnStop.stream().filter(Routine::isRunning).forEach(Routine::stop);
            Routine.this.onStop(gracefulExit);
            running = false;
        }

        private void interruptOnStop(Routine routine) {
            if (interruptOnStop == null) {
                interruptOnStop = new ArrayList<>();
            }
            interruptOnStop.add(routine);
        }
    }

    private final boolean interruptable;
    final Set<Subsystem> requirements;  // comment on package private
    final RoutineLoop loop = new RoutineLoop();
    private RoutineScheduler scheduler;
    private boolean started = false;
    private boolean running = false;
    private Routine claimedBy = null;

    public Routine(boolean interruptable, Subsystem... requirements) {
        this.interruptable = interruptable;
        for (Subsystem requirement : Objects.requireNonNull(requirements, "requirements must be non-null")) {
            Objects.requireNonNull(requirement, "cannot require null Subsystem");
        }
        this.requirements = Set.of(requirements);
    }

    public Routine(Subsystem... requirements) {
        this(true, requirements);
    }

    public Routine(boolean interruptable, Collection<Subsystem> requirements) {
        this.interruptable = interruptable;
        if (Objects.requireNonNull(requirements, "requirements must be non-null")
                .stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("cannot require null Subsystem");
        }
        this.requirements = Set.copyOf(requirements);
    }

    public Routine(Collection<Subsystem> requirements) {
        this(true, requirements);
    }

    public final Set<Subsystem> getRequirements() {
        return Set.copyOf(requirements);
    }

    public final boolean requires(Subsystem requirement) {
        return requirements.contains(
                Objects.requireNonNull(requirement, "cannot check requirement on null Subsystem")
        );
    }

    public final boolean isInterruptable() {
        return interruptable;
    }

    public final boolean hasStarted() {
        return started;
    }

    public final boolean isRunning() {
        return running;
    }

    public final boolean hasCompleted() {
        return started && !running;
    }

    public final boolean isComposed() {
        return claimedBy != null;
    }

    public final Routine getClaimedBy() {
        return claimedBy;
    }

    public final boolean start(RoutineScheduler scheduler) {
        checkIfCanRun();
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler must be non-null");
        running = started = scheduler.trySchedule(this);
        return started;
    }

    final void checkIfCanRun() {
        if (isComposed()) {
            throw new IllegalStateException("cannot start a composed Routine");
        }
        if (hasStarted()) {
            throw new IllegalStateException("Routine already started");
        }
    }

    public final boolean start() {
        return start(RoutineScheduler.getDefault());
    }

    public final void stop() {
        if (!isRunning()) {
            throw new IllegalStateException("Routine not running");
        }
        scheduler.unschedule(this);
    }

    public final Routine then(Routine... others) {
        return Routine.sequential(isInterruptable(), prependRoutine(others, this));
    }

    public final Routine and(Routine... others) {
        return Routine.parallel(isInterruptable(), prependRoutine(others, this));
    }

    public final Routine raceWith(Routine... others) {
        return Routine.race(isInterruptable(), prependRoutine(others, this));
    }

    public final Routine withTimeout(int timeoutMs) {
        // The interruptability of the BlockRoutine does not matter.
        return Routine.parallel(isInterruptable(), this, new BlockRoutine(timeoutMs, false));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    // comment on reentrancy and how internal commands inherit interruptability
    protected final boolean scheduleInternal(Routine routine, boolean interruptOnStop) {
        if (!isRunning()) {
            throw new IllegalStateException("cannot schedule an internal Routine on a non-running Routine");
        }
        if (routine.isComposed() && routine.getClaimedBy() != this) {
            throw new IllegalStateException("cannot schedule an internal Routine claimed by a different Routine");
        }
        if (interruptOnStop) {
            loop.interruptOnStop(routine);
        }
        return scheduler.tryScheduleInContext(routine, requirements);
    }

    protected final boolean scheduleInternal(Routine routine) {
        return scheduleInternal(routine, true);
    }

    protected final void claimForComposition(Routine claimer) {
        if (hasStarted()) {
            throw new IllegalStateException("cannot compose a started Routine");
        }
        if (isComposed()) {
            throw new IllegalStateException("this Routine is already composed");
        }
        claimedBy = Objects.requireNonNull(claimer, "cannot claim Routine with a null Routine");
    }

    protected void onStart() {}

    protected void onLoop() {}

    protected void onStop(boolean graceful) {}

    protected abstract boolean isFinished();

    private static Routine[] prependRoutine(Routine[] array, Routine routine) {
        Routine[] routines = new Routine[array.length + 1];
        System.arraycopy(array, 0, routines, 1, array.length);
        routines[0] = routine;
        return routines;
    }

    public static Routine sequential(boolean interruptable, Routine... routines) {
        // The constructor handles error checking.
        return new SequentialRoutineGroup(interruptable, routines);
    }

    public static Routine sequential(Routine... routines) {
        return Routine.sequential(true, routines);
    }

    public static Routine parallel(boolean interruptable, Routine... routines) {
        // The constructor handles error checking.
        return new ParallelRoutineGroup(interruptable, routines);
    }

    public static Routine parallel(Routine... routines) {
        return Routine.parallel(true, routines);
    }

    public static Routine race(boolean interruptable, Routine... routines) {
        // The constructor handles error checking.
        return new RaceRoutineGroup(interruptable, routines);
    }

    public static Routine race(Routine... routines) {
        return Routine.race(true, routines);
    }

    public static Routine newFunctional(Runnable onStart,
                                        Runnable onLoop,
                                        BooleanConsumer onStop,
                                        BooleanSupplier isFinished,
                                        boolean interruptable,
                                        Subsystem... requirements) {
        // The constructor handles error checking.
        return new FunctionalRoutine(onStart, onLoop, onStop, isFinished, interruptable, requirements);
    }

    public static Routine newFunctional(Runnable onStart,
                                        Runnable onLoop,
                                        BooleanConsumer onStop,
                                        BooleanSupplier isFinished,
                                        Subsystem... requirements) {
        return newFunctional(onStart, onLoop, onStop, isFinished, true, requirements);
    }

    public static Routine newSingleShot(Runnable runnable, Subsystem... requirements) {
        return newFunctional(
                runnable,
                FunctionalRoutine.EMPTY_RUNNABLE,
                FunctionalRoutine.EMPTY_BOOLEAN_CONSUMER,
                FunctionalRoutine.TRUE_BOOLEAN_SUPPLIER,
                false,
                requirements
        );
    }

    public static Routine newPerpetual(Runnable onStart,
                                       Runnable onLoop,
                                       boolean interruptable,
                                       Subsystem... requirements) {
        return newFunctional(
                onStart,
                onLoop,
                FunctionalRoutine.EMPTY_BOOLEAN_CONSUMER,
                FunctionalRoutine.FALSE_BOOLEAN_SUPPLIER,
                interruptable,
                requirements
        );
    }

    public static Routine newPerpetual(Runnable onStart, Runnable onLoop, Subsystem... requirements) {
        return newPerpetual(onStart, onLoop, true, requirements);
    }

    public static Routine newPerpetual(Runnable onLoop, boolean interruptable, Subsystem... requirements) {
        return newPerpetual(FunctionalRoutine.EMPTY_RUNNABLE, onLoop, interruptable, requirements);
    }

    public static Routine newPerpetual(Runnable onLoop, Subsystem... requirements) {
        return newPerpetual(onLoop, true, requirements);
    }
}
