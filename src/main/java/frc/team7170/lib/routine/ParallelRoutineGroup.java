package frc.team7170.lib.routine;

import java.util.*;

class ParallelRoutineGroup extends BaseRoutineGroup {

    ParallelRoutineGroup(boolean interruptable, Routine... routines) {
        super(
                interruptable,
                extractAndCheckRequirements(Objects.requireNonNull(routines, "routines must be non-null")),
                Set.of(routines)
        );
    }

    @Override
    protected void onStart() {
        // This schedules with interruptOnStop set to the default of true, meaning all composed Routines will be
        // interrupted if this outer Routine is stopped.
        routines.forEach(this::scheduleInternal);
    }

    @Override
    protected boolean isFinished() {
        return routines.stream().allMatch(Routine::hasCompleted);
    }

    private static Set<Subsystem> extractAndCheckRequirements(Routine... routines) {
        Set<Subsystem> requirements = new HashSet<>();
        for (Routine routine : routines) {
            for (Subsystem requirement :
                    Objects.requireNonNull(routine, "cannot run null routine in parallel").requirements) {
                if (!requirements.add(requirement)) {
                    throw new IllegalArgumentException("cannot run conflicting Routines in parallel");
                }
            }
        }
        return requirements;
    }
}
