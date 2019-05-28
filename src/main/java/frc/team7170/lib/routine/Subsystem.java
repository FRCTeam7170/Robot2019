package frc.team7170.lib.routine;

import frc.team7170.lib.Named;

public interface Subsystem extends Named {

    default void onLoop() {}

    // TODO: comment on how this is different from WPI lib version
    default Routine getDefaultRoutine() {
        return null;
    }
}
