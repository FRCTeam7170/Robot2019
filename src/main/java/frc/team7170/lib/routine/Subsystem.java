package frc.team7170.lib.routine;

import frc.team7170.lib.Named;

public interface Subsystem extends Named {

    default void onLoop() {}

    default Routine getDefaultRoutine() {
        return null;
    }
}
