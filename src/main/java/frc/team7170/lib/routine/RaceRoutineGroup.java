package frc.team7170.lib.routine;

class RaceRoutineGroup extends ParallelRoutineGroup {

    RaceRoutineGroup(boolean interruptable, Routine... routines) {
        super(interruptable, routines);
    }

    @Override
    protected boolean isFinished() {
        // Note the use of "anyMatch" as opposed to "allMatch" like in the super class.
        return routines.stream().anyMatch(Routine::hasCompleted);
    }
}
