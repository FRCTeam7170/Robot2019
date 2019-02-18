package frc.team7170.robot;

import frc.team7170.lib.Name;
import frc.team7170.lib.fsm.*;
import frc.team7170.robot.commands.CmdMoveElevator;
import frc.team7170.robot.commands.CmdPickup;
import frc.team7170.robot.commands.CmdRotateFrontArms;
import frc.team7170.robot.subsystems.Drive;
import frc.team7170.robot.subsystems.Elevator;
import frc.team7170.robot.subsystems.EndEffector;
import frc.team7170.robot.subsystems.FrontArms;

import java.util.logging.Logger;

// TODO: issue warnings when transitions fail?
public class TeleopStateMachine {

    private static final Logger LOGGER = Logger.getLogger(TeleopStateMachine.class.getName());

    private static final Drive drive = Drive.getInstance();
    private static final FrontArms frontArms = FrontArms.getInstance();
    private static final EndEffector endEffector = EndEffector.getInstance();
    private static final Elevator elevator = Elevator.getInstance();

    private double driveMultiplier = Constants.State.HOME_MULTIPLIER;

    private final FiniteStateMachine fsm = new FiniteStateMachine.Builder().permitMistrigger().build();

    private final State normalState = fsm.newState(new Name("normal"));
    private final State pickupPrepareState = fsm.newState(new Name("pickupPrepare"));
    private final State pickupState = fsm.newState(new Name("pickup"));
    // private final State ejectingState = fsm.newState(new Name("ejecting"));

    public final Trigger driveTrigger = fsm.newTrigger(new Name("drive"));
    public final Trigger elevateTrigger = fsm.newTrigger(new Name("elevate"));
    // public final Trigger ejectTrigger = fsm.newTrigger(new Name("eject"));
    // public final Trigger lateralSlideTrigger = fsm.newTrigger(new Name("lateralSlide"));
    public final Trigger pickupPrepareTrigger = fsm.newTrigger(new Name("pickupPrepare"));
    public final Trigger pickupTrigger = fsm.newTrigger(new Name("pickup"));
    public final Trigger pickupCancelTrigger = fsm.newTrigger(new Name("pickupCancel"));
    public final Trigger pickupFinishedTrigger = fsm.newTrigger(new Name("pickupFinished"));

    @SuppressWarnings("unchecked")
    private final Transition driveTransition = fsm.newTransition(
            Transition.TransitionConfig.newInternal(new State[] {normalState, pickupPrepareState}, driveTrigger)
                    .onStart(this::drive)
    );
    @SuppressWarnings("unchecked")
    private final Transition elevateTransition = fsm.newTransition(
            Transition.TransitionConfig.newInternal(normalState, elevateTrigger)
                    .onStart(this::elevate)
    );
    // private final Transition ejectTransition = fsm.newTransition(
    //         new Transition.TransitionConfig(normalState, ejectingState, ejectTrigger)
    // );  TODO: will need finishedEject trigger/transition too
    @SuppressWarnings("unchecked")
    private final Transition pickupPrepareTransition = fsm.newTransition(
            new Transition.TransitionConfig(normalState, pickupPrepareState, pickupPrepareTrigger)
                    .onStart(this::pickupPrepare)
    );
    @SuppressWarnings("unchecked")
    private final Transition pickupTransition = fsm.newTransition(
            new Transition.TransitionConfig(pickupPrepareState, pickupState, pickupTrigger)
                    .onStart(this::pickup)
    );
    @SuppressWarnings("unchecked")
    private final Transition pickupCancelTransition = fsm.newTransition(
            new Transition.TransitionConfig(pickupPrepareState, normalState, pickupCancelTrigger)
                    .onStart(this::pickupCancel)
    );
    @SuppressWarnings("unchecked")
    private final Transition pickupFinishedTransition = fsm.newTransition(
            new Transition.TransitionConfig(pickupState, normalState, pickupFinishedTrigger)
                    .onStart(this::pickupFinished)
    );

    private TeleopStateMachine() {
        fsm.initialize(normalState);
    }

    private static final TeleopStateMachine INSTANCE = new TeleopStateMachine();

    public static TeleopStateMachine getInstance() {
        return INSTANCE;
    }

    private void drive(Event event) {
        event.assertArgumentsTypes(Integer.class, Integer.class, Boolean.class);

        double val0 = driveMultiplier * (double) event.arguments[0];
        double val1 = driveMultiplier * (double) event.arguments[1];
        boolean tankDrive = (boolean) event.arguments[2];

        if (tankDrive) {
            drive.tankDrive(val0, val1);
        } else {
            drive.arcadeDrive(val0, val1);
        }
    }

    private void elevate(Event event) {
        event.assertArgumentsTypes(ElevatorLevel.class);
        ElevatorLevel elevatorLevel = (ElevatorLevel) event.arguments[0];
        new CmdMoveElevator(elevatorLevel.getHeightMetres(), true);
        driveMultiplier = elevatorLevel.getDriveMultiplier();
    }

    private void eject(Event event) {
        event.assertArgumentsCount(0);
        // TODO
    }

    private void pickupPrepare(Event event) {
        event.assertArgumentsCount(0);
        new CmdRotateFrontArms(Constants.FrontArms.HORIZONTAL_ANGLE_DEGREES, true).start();
        new CmdMoveElevator(Constants.Elevator.RECEIVE_HATCH_PANEL_METRES, true).start();
        driveMultiplier = Constants.State.PICKUP_PREPARE_MULTIPLIER;
    }

    private void pickup(Event event) {
        event.assertArgumentsCount(0);
        new CmdPickup().start();
    }

    private void pickupCancel(Event event) {
        event.assertArgumentsCount(0);
        new CmdRotateFrontArms(Constants.FrontArms.HOME_ANGLE_DEGREES, true).start();
        new CmdMoveElevator(Constants.Elevator.HOME_METRES, true).start();
        driveMultiplier = Constants.State.HOME_MULTIPLIER;
    }

    private void pickupFinished(Event event) {
        event.assertArgumentsCount(0);
        new CmdMoveElevator(Constants.Elevator.LEVEL1_METRES, true).start();
        driveMultiplier = Constants.State.LEVEL1_MULTIPLIER;
    }
}
