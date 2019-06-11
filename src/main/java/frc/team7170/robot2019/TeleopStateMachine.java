package frc.team7170.robot2019;

import frc.team7170.lib.Name;
import frc.team7170.lib.command.CmdRunnable;
import frc.team7170.lib.fsm.*;
import frc.team7170.robot2019.commands.*;
import frc.team7170.robot2019.subsystems.Drive;
import frc.team7170.robot2019.subsystems.Elevator;
import frc.team7170.robot2019.subsystems.EndEffector;

// TODO: this whole class is a bit of a mess
// TODO: no need to store references to states or transitions
public class TeleopStateMachine {

    private static final Drive drive = Drive.getInstance();

    private double driveMultiplier = Constants.Drive.HOME_MULTIPLIER;

    private final FiniteStateMachine fsm = new FiniteStateMachine.Builder().permitMistrigger().build();

    private final State normalState = fsm.newState(new Name("normal"));
    private final State pickupPrepareState = fsm.newState(new Name("pickupPrepare"));
    private final State pickupState = fsm.newState(new Name("pickup"));
    private final State ejectingState = fsm.newState(new Name("ejecting"));
    private final State loadingState = fsm.newState(new Name("loading"));
    private final State climbingState = fsm.newState(new Name("climbing"));

    public final Trigger driveTrigger = fsm.newTrigger(new Name("drive"));
    public final Trigger elevateAutoTrigger = fsm.newTrigger(new Name("elevateAuto"));
    public final Trigger elevateManualTrigger = fsm.newTrigger(new Name("elevateManual"));
    public final Trigger ejectTrigger = fsm.newTrigger(new Name("eject"));
    public final Trigger ejectFinishedTrigger = fsm.newTrigger(new Name("ejectFinished"));
    // For manual movement of lateral slide...
    // public final Trigger lateralSlideTrigger = fsm.newTrigger(new Name("lateralSlide"));
    // public final Trigger pickupPrepareTrigger = fsm.newTrigger(new Name("pickupPrepare"));
    public final Trigger pickupTrigger = fsm.newTrigger(new Name("pickup"));
    public final Trigger pickupCancelTrigger = fsm.newTrigger(new Name("pickupCancel"));
    public final Trigger pickupFinishedTrigger = fsm.newTrigger(new Name("pickupFinished"));
    public final Trigger loadTrigger = fsm.newTrigger(new Name("load"));
    public final Trigger climbingTrigger = fsm.newTrigger(new Name("climb"));

    @SuppressWarnings("unchecked")
    private final Transition driveTransition = fsm.newTransition(
            Transition.TransitionConfig.newInternal(new State[] {normalState, pickupPrepareState, loadingState}, driveTrigger)
                    .onStart(this::drive)
    );
    @SuppressWarnings("unchecked")
    private final Transition elevateAutoTransition = fsm.newTransition(
            Transition.TransitionConfig.newInternal(normalState, elevateAutoTrigger)
                    .onStart(this::elevateAuto)
    );
    @SuppressWarnings("unchecked")
    private final Transition elevateManualTransition = fsm.newTransition(
            Transition.TransitionConfig.newInternal(normalState, elevateManualTrigger)
                    .onStart(this::elevateManual)
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
    @SuppressWarnings("unchecked")
    private final Transition loadPrepareTransition = fsm.newTransition(
            new Transition.TransitionConfig(normalState, loadingState, loadTrigger)
                    .onStart(this::loadPrepare)
    );
    @SuppressWarnings("unchecked")
    private final Transition loadTransition = fsm.newTransition(
            new Transition.TransitionConfig(loadingState, normalState, loadTrigger)
                    .onStart(this::load)
    );
    @SuppressWarnings("unchecked")
    private final Transition climbTransition = fsm.newTransition(
            new Transition.TransitionConfig(normalState, climbingState, climbingTrigger)
                    .onStart(this::climb)
    );

    private TeleopStateMachine() {
        fsm.initialize(normalState);
    }

    private static final TeleopStateMachine INSTANCE = new TeleopStateMachine();

    public static TeleopStateMachine getInstance() {
        return INSTANCE;
    }

    public void reset() {
        fsm.forceTo(normalState);
    }

    private void drive(Event event) {
        event.assertArgumentsTypes(Double.class, Double.class, Boolean.class);

        double val0 = driveMultiplier * (double) event.arguments[0];
        double val1 = driveMultiplier * (double) event.arguments[1];
        boolean tankDrive = (boolean) event.arguments[2];

        if (tankDrive) {
            drive.tankDrive(val0, val1);
        } else {
            drive.arcadeDrive(val0, val1);
        }
    }

    private void elevateAuto(Event event) {
        event.assertArgumentsTypes(ElevatorLevel.class);
        ElevatorLevel elevatorLevel = (ElevatorLevel) event.arguments[0];
        new CmdMoveElevator(elevatorLevel.getHeightMetres(), true).start();
//        driveMultiplier = elevatorLevel.getDriveMultiplier();
    }

    private void elevateManual(Event event) {
        event.assertArgumentsTypes(Double.class);
        double elevatorPercent = (double) event.arguments[0];
        Elevator.getInstance().setPercent(elevatorPercent);
//        driveMultiplier = Constants.Drive.HOME_MULTIPLIER;
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
        driveMultiplier = Constants.Drive.PICKUP_PREPARE_MULTIPLIER;
    }

    private void pickup(Event event) {
        event.assertArgumentsCount(0);
        new CmdPickup().start();
    }

    private void pickupCancel(Event event) {
        event.assertArgumentsCount(0);
        new CmdRotateFrontArms(Constants.FrontArms.HOME_ANGLE_DEGREES, true).start();
        new CmdMoveElevator(Constants.Elevator.HOME_METRES, true).start();
        driveMultiplier = Constants.Drive.HOME_MULTIPLIER;
    }

    private void pickupFinished(Event event) {
        event.assertArgumentsCount(0);
        new CmdMoveElevator(Constants.Elevator.LEVEL1_METRES, true).start();
        driveMultiplier = Constants.Drive.LEVEL1_MULTIPLIER;
    }

    private void loadPrepare(Event event) {
        event.assertArgumentsCount(0);
        new CmdRunnable(EndEffector.getInstance()::deployPin, EndEffector.getInstance()).start();
        new CmdMoveElevator(Constants.Elevator.LOAD_INIT_METRES, true).start();
        driveMultiplier = Constants.Drive.LOAD_PREPARE_MULTIPLIER;
    }

    private void load(Event event) {
        event.assertArgumentsCount(0);
        new CmdRunnable(EndEffector.getInstance()::retractPin, EndEffector.getInstance()).start();
        new CmdMoveElevator(Constants.Elevator.LOAD_MOVE_UP_METRES, true).start();
        driveMultiplier = Constants.Drive.HOME_MULTIPLIER;
    }

    private void climb(Event event) {
        event.assertArgumentsTypes(ClimbLevel.class);
        new CmdClimb((ClimbLevel) event.arguments[0]).start();
    }
}
