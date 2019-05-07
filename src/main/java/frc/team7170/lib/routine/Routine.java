package frc.team7170.lib.routine;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.Named;
import frc.team7170.lib.looping.Loop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class Routine implements Named {

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
            interruptOnStop.forEach(Routine::stop);
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
    private Scheduler scheduler;
    private boolean started = false;
    private boolean running = false;
    final RoutineLoop loop = new RoutineLoop();

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

    public final boolean isInterruptable() {
        return interruptable;
    }

    public final Set<Subsystem> getRequirements() {
        return Set.copyOf(requirements);
    }

    public final boolean requires(Subsystem requirement) {
        return requirements.contains(
                Objects.requireNonNull(requirement, "cannot check requirement on null Subsystem")
        );
    }

    public final boolean isRunning() {
        return running;
    }

    public final boolean isCompleted() {
        return started && !running;
    }

    public final boolean start(Scheduler scheduler) {
        if (started) {
            throw new IllegalStateException("Routine already started");
        }
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler must be non-null");
        running = started = scheduler.trySchedule(this);
        return started;
    }

    public final boolean start() {
        return start(Scheduler.getDefault());
    }

    public final void stop() {
        scheduler.unschedule(this);
    }

    // comment on reentrancy
    protected final boolean scheduleInternal(Routine routine, boolean interruptOnStop) {
        if (!running) {
            throw new IllegalStateException("Routine not running");
        }
        if (interruptOnStop) {
            loop.interruptOnStop(routine);
        }
        return scheduler.tryScheduleInContext(routine, requirements);
    }

    protected final boolean scheduleInternal(Routine routine) {
        return scheduleInternal(routine, true);
    }

    public final Routine then(Routine other) {
        return null;
    }

    public final Routine and(Routine other) {
        return null;
    }

    public final Command asCommand() {
        return new Command() {
            @Override
            protected boolean isFinished() {
                return Routine.this.isFinished();
            }
        };
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    protected void onStart() {}

    protected void onLoop() {}

    protected void onStop(boolean graceful) {}

    protected abstract boolean isFinished();
}
