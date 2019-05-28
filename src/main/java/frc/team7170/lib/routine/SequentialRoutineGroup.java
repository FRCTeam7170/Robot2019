package frc.team7170.lib.routine;

import java.util.*;

class SequentialRoutineGroup extends BaseRoutineGroup {

    private final Iterator<Routine> iterator;
    private Routine current;

    SequentialRoutineGroup(boolean interruptable, Routine... routines) {
        super(
                interruptable,
                extractRequirements(Objects.requireNonNull(routines, "routines must be non-null")),
                List.of(routines)
        );
        iterator = this.routines.iterator();
    }

    @Override
    protected void onStart() {
        scheduleNext();
    }

    @Override
    protected void onLoop() {
        if (current.hasCompleted() && iterator.hasNext()) {
            scheduleNext();
        }
    }

    @Override
    protected boolean isFinished() {
        return current.hasCompleted() && !iterator.hasNext();
    }

    private void scheduleNext() {
        current = iterator.next();
        scheduleInternal(current);
    }

    private static Set<Subsystem> extractRequirements(Routine... routines) {
        Set<Subsystem> requirements = new HashSet<>();
        for (Routine routine : routines) {
            requirements.addAll(
                    Objects.requireNonNull(routine, "cannot run null routine in sequence").requirements
            );
        }
        return requirements;
    }
}
