package frc.team7170.robot2019.actions;

import frc.team7170.lib.oi.ButtonAction;

public enum ButtonActions implements ButtonAction {
    ELEVATOR_LEVEL1,
    ELEVATOR_LEVEL2,
    ELEVATOR_LEVEL3,

    // PICKUP_PREPARE,
    PICKUP,
    PICKUP_CANCEL,

    LOAD,
    CLIMB_LEVEL2,
    CLIMB_LEVEL3,

    EJECT,
    EJECT_CANCEL,

    LATERAL_SLIDE_LEFT,
    LATERAL_SLIDE_RIGHT,

    TURTLE_MODE,
    RABBIT_MODE,
    TURTLE_RABBIT_TOGGLE,

    TOGGLE_PIN,  // TODO: TEMP
    TEST_GENERIC_0,  // TODO: TEMP
    TEST_GENERIC_1,  // TODO: TEMP
    NEW_CMD,  // TODO: TEMP
}
