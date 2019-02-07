package frc.team7170.robot;

import frc.team7170.lib.fsm.Event;
import frc.team7170.lib.fsm.FiniteStateMachine;
import frc.team7170.lib.fsm.State;

import java.util.function.Consumer;

public class ClimbStateMachine extends FiniteStateMachine {

    State reversingState = newState("reversing");
    State waitingForTargetState = newState("waitingForTarget");

    @SuppressWarnings("unchecked")
    public ClimbStateMachine() {
        super(true, new Consumer[0], new Consumer[0]);
    }
}
