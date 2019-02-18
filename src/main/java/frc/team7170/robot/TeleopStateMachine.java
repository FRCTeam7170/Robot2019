package frc.team7170.robot;

import frc.team7170.lib.Name;
import frc.team7170.lib.fsm.FiniteStateMachine;
import frc.team7170.lib.fsm.State;
import frc.team7170.lib.fsm.Transition;
import frc.team7170.lib.fsm.Trigger;
import frc.team7170.robot.commands.CmdRotateFrontArms;
import frc.team7170.robot.subsystems.Drive;
import frc.team7170.robot.subsystems.Elevator;
import frc.team7170.robot.subsystems.EndEffector;
import frc.team7170.robot.subsystems.FrontArms;

public class TeleopStateMachine {

    private static final Drive drive = Drive.getInstance();
    private static final FrontArms frontArms = FrontArms.getInstance();
    private static final EndEffector endEffector = EndEffector.getInstance();
    private static final Elevator elevator = Elevator.getInstance();

    private double driveMultiplier = 1.0;

    private final FiniteStateMachine fsm = new FiniteStateMachine.Builder().permitMistrigger().build();

    private final State normalState = fsm.newState(new Name("normal"));
    // private final State elevatedState = fsm.newState(new Name("elevated"));
    private final State pickupPrepareState = fsm.newState(
            new State.StateConfig(new Name("pickupPrepare"))
                    .onEnter(event -> new CmdRotateFrontArms(Constants.FrontArms.HORIZONTAL_ANGLE_DEGREES, true).start())
    );
    private final State pickupState = fsm.newState(new Name("pickup"));
    private final State ejectingState = fsm.newState(new Name("ejecting"));

    public final Trigger driveTrigger = fsm.newTrigger(new Name("drive"));
    public final Trigger elevatorTrigger = fsm.newTrigger(new Name("elevator"));
    public final Trigger ejectTrigger = fsm.newTrigger(new Name("eject"));
    // public final Trigger lateralSlideTrigger = fsm.newTrigger(new Name("lateralSlide"));
    public final Trigger pickupPrepareTrigger = fsm.newTrigger(new Name("pickupPrepare"));
    public final Trigger pickupTrigger = fsm.newTrigger(new Name("pickup"));

    private final Transition pickupPrepareTransition = fsm.newTransition(
            new Transition.TransitionConfig(normalState, pickupPrepareState, pickupPrepareTrigger)
                    .onStart(event -> )
    );
    private final Transition pickupTransition = fsm.newTransition(
            new Transition.TransitionConfig(pickupPrepareState, pickupState, pickupTrigger)
    );
    private final Transition driveTransition = fsm.newTransition(
            Transition.TransitionConfig.newInternal(new State[] {normalState, pickupPrepareState}, driveTrigger)
    );
    private final Transition elevatorTransition = fsm.newTransition(
            Transition.TransitionConfig.newInternal(normalState, elevatorTrigger)
    );
    private final Transition ejectTransition = fsm.newTransition(
            new Transition.TransitionConfig(normalState, ejectingState, ejectTrigger)
    );

    private TeleopStateMachine() {}

    private static final TeleopStateMachine INSTANCE = new TeleopStateMachine();

    public static TeleopStateMachine getInstance() {
        return INSTANCE;
    }

    private static void assurePinDeployed() {
        endEffector.deployPin();
    }

    private static void assurePinRetracted() {
        endEffector.retractPin();
    }
}
