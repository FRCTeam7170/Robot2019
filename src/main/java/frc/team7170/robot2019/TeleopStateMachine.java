package frc.team7170.robot2019;

import frc.team7170.lib.Name;
import frc.team7170.lib.fsm.*;
import frc.team7170.robot2019.commands.*;
import frc.team7170.robot2019.subsystems.Drive;

public class TeleopStateMachine {

    private static final Drive drive = Drive.getInstance();

    private double driveMultiplier = Constants.State.HOME_MULTIPLIER;

    private final FiniteStateMachine fsm = new FiniteStateMachine.Builder().permitMistrigger().build();

    private final State normalState = fsm.newState(new Name("normal"));
    private final State pickupPrepareState = fsm.newState(new Name("pickupPrepare"));
    private final State pickupState = fsm.newState(new Name("pickup"));
    private final State ejectingState = fsm.newState(new Name("ejecting"));

    public final Trigger driveTrigger = fsm.newTrigger(new Name("drive"));
    public final Trigger elevateTrigger = fsm.newTrigger(new Name("elevate"));
    public final Trigger ejectTrigger = fsm.newTrigger(new Name("eject"));
    public final Trigger ejectFinishedTrigger = fsm.newTrigger(new Name("ejectFinished"));
    // For manual movement of lateral slide...
    // public final Trigger lateralSlideTrigger = fsm.newTrigger(new Name("lateralSlide"));
    // public final Trigger pickupPrepareTrigger = fsm.newTrigger(new Name("pickupPrepare"));
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
    @SuppressWarnings("unchecked")
    private final Transition ejectTransition = fsm.newTransition(
            new Transition.TransitionConfig(normalState, ejectingState, ejectTrigger)
                    .onStart(this::eject)
    );
    @SuppressWarnings("unchecked")
    private final Transition ejectFinishedTransition = fsm.newTransition(
            new Transition.TransitionConfig(ejectingState, normalState, ejectFinishedTrigger)
                    .onStart(this::ejectFinished)
    );
    @SuppressWarnings("unchecked")
    private final Transition pickupPrepareTransition = fsm.newTransition(
            new Transition.TransitionConfig(normalState, pickupPrepareState, pickupTrigger)
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
        new CmdMoveElevator(elevatorLevel.getHeightMetres(), true).start();
        driveMultiplier = elevatorLevel.getDriveMultiplier();
    }

    private void eject(Event event) {
        event.assertArgumentsCount(0);
        new CmdEject().start();
    }

    private void ejectFinished(Event event) {
        event.assertArgumentsCount(0);
        new CmdMoveLateralSlide(Constants.EndEffector.LATERAL_SLIDE_CENTRE_METRES).start();
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