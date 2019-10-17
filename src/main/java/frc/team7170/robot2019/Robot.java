package frc.team7170.robot2019;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.EntryNotification;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.team7170.lib.math.CalcUtil;
import frc.team7170.lib.Name;
import frc.team7170.lib.Named;
import frc.team7170.lib.oi.*;
import frc.team7170.robot2019.actions.AxisActions;
import frc.team7170.robot2019.actions.ButtonActions;
import frc.team7170.robot2019.commands.*;
import frc.team7170.robot2019.subsystems.*;

import java.util.ArrayList;
import java.util.List;

// TODO: spooky-console: NTBrowser needs ability to make new entry
// TODO: "primitive-mode" option
// TODO: make zeroable interface for all zeroable subsystems
// TODO: replace logger statements with Shuffleboard events

public class Robot extends TimedRobot implements Named {

    /**
     * Entry point. ({@code main(...)} was removed from {@link edu.wpi.first.wpilibj.RobotBase} this year.)
     */
    public static void main(String[] args) {
        Robot.startRobot(Robot::new);
    }

    // TODO: no need to keep references here
//    private LE3DPJoystick joystick;
    private LF310Gamepad gamepad;
    private KeyMap defaultKeyMap;

    private final Compressor compressor = new Compressor(Constants.CAN.PCM);

    private final UsbCamera camera0 = CameraServer.getInstance().startAutomaticCapture(Constants.Camera.CAMERA0_NAME, 0);
    // private final UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(Constants.Camera.CAMERA1_NAME, 1);

    private NetworkTableEntry matchTimeEntry;

    // For button board
    private NetworkTableEntry pressedEntry;
    private NetworkTableEntry releasedEntry;

    @Override
    public void robotInit() {
//        joystick = new LE3DPJoystick(new GenericHID(Constants.OI.JOYSTICK_PORT) {
//            @Override
//            public double getX(Hand hand) {
//                return getRawAxis(0);
//            }
//
//            @Override
//            public double getY(Hand hand) {
//                return getRawAxis(1);
//            }
//        });

        gamepad = new LF310Gamepad(new GenericHID(Constants.OI.GAMEPAD_PORT) {
            @Override
            public double getX(Hand hand) {
                if (hand == Hand.kRight) {
                    return getRawAxis(4);
                }
                // Default to returning the left.
                return getRawAxis(0);
            }

            @Override
            public double getY(Hand hand) {
                if (hand == Hand.kRight) {
                    return getRawAxis(5);
                }
                // Default to returning the left.
                return getRawAxis(1);
            }
        });

        compressor.start();
//        compressor.stop();

        ShuffleboardTab mainTab = Shuffleboard.getTab(Constants.Shuffleboard.MAIN_TAB);

        mainTab.add(Constants.Camera.CAMERA0_NAME, camera0);
        mainTab.add("compressorEnabled", true).getEntry().addListener(notification -> {
            if (notification.value.getBoolean()) {
                compressor.start();
            } else {
                compressor.stop();
            }
        }, EntryListenerFlags.kUpdate);
        matchTimeEntry = mainTab.add("matchTime", 0.0).getEntry();

        // Force load subsystems
        Drive.getInstance();
        EndEffector.getInstance();
        FrontArms.getInstance();
        Elevator.getInstance();
        ClimbLegs.getInstance();
        ClimbDrive.getInstance();

        // Setup keybindings system
//        KeyBindings.getInstance().registerController(joystick);
        KeyBindings.getInstance().registerController(gamepad);
        KeyBindings.getInstance().registerAxisActions(AxisActions.values());
        KeyBindings.getInstance().registerButtonActions(ButtonActions.values());
        defaultKeyMap = new SerializableKeyMap.Builder(new Name("default"))
                .addPair(AxisActions.DRIVE_L, gamepad, gamepad.A_LY)
                .addPair(AxisActions.DRIVE_R, gamepad, gamepad.A_RY)
                .addPair(ButtonActions.TURTLE_RABBIT_TOGGLE, gamepad, gamepad.B_RJOY)
                .addPair(AxisActions.ELEVATOR, gamepad, gamepad.A_TRIGGERS)
                // .addPair(AxisActions.LEFT_LINEAR_ACTUATOR, gamepad, gamepad.A_LY)
                // .addPair(AxisActions.RIGHT_LINEAR_ACTUATOR, gamepad, gamepad.A_RY)
                // .addPair(AxisActions.FRONT_ARMS, gamepad, gamepad.A_LY)
                // .addPair(AxisActions.CLIMB_DRIVE, gamepad, gamepad.A_RY)
                .addPair(ButtonActions.ELEVATOR_LEVEL1, gamepad, gamepad.B_B)
                .addPair(ButtonActions.ELEVATOR_LEVEL2, gamepad, gamepad.B_X)
                 .addPair(ButtonActions.ELEVATOR_LEVEL3, gamepad, gamepad.B_Y)
                 .addPair(ButtonActions.PICKUP, gamepad, gamepad.B_LBUMPER)
                 .addPair(ButtonActions.PICKUP_CANCEL, gamepad, gamepad.B_X)
                .addPair(ButtonActions.EJECT, gamepad, gamepad.B_RBUMPER)
                // .addPair(ButtonActions.EJECT_CANCEL, gamepad, gamepad.B_A)
                 //.addPair(ButtonActions.LATERAL_SLIDE_LEFT, gamepad, gamepad.POV270)
                 //.addPair(ButtonActions.LATERAL_SLIDE_RIGHT, gamepad, gamepad.POV90)
                //.addPair(ButtonActions.LOAD, gamepad, gamepad.B_Y)
                // .addPair(ButtonActions.CLIMB_LEVEL2, gamepad, gamepad.B_BACK)
//                 .addPair(ButtonActions.CLIMB_LEVEL3, gamepad, gamepad.B_START)
                .addPair(ButtonActions.TOGGLE_PIN, gamepad, gamepad.B_A)
                 .addPair(ButtonActions.TEST_GENERIC_0, gamepad, gamepad.B_START)
//                 .addPair(ButtonActions.TEST_GENERIC_1, gamepad, gamepad.B_BACK)
                .build();
        KeyBindings.getInstance().registerKeyMap(defaultKeyMap);
        KeyBindings.getInstance().setCurrKeyMap(defaultKeyMap);

        NetworkTableInstance instance = NetworkTableInstance.getDefault();
        pressedEntry = instance.getEntry("buttonBoardPressed");
        releasedEntry = instance.getEntry("buttonBoardReleased");
        pressedEntry.addListener(this::buttonPressed, EntryListenerFlags.kUpdate);
        releasedEntry.addListener(this::buttonRealeased, EntryListenerFlags.kUpdate);
    }

    @Override
    public void disabledInit() {
        // For characterizing the servo feedback:
//        System.out.println(String.format("SD: %f; MEAN: %f",
//                EndEffector.LateralSlide.getInstance().wdc.getStdDeviation(),
//                EndEffector.LateralSlide.getInstance().wdc.getMean()));

        // Put all subsystems into a safe state.
        Drive.getInstance().killMotors();
        EndEffector.LateralSlide.getInstance().kill();
        FrontArms.getInstance().killMotors();
        Elevator.getInstance().killMotor();
        ClimbLegs.getInstance().getLeftLinearActuator().killMotor();
        ClimbLegs.getInstance().getRightLinearActuator().killMotor();
        ClimbDrive.getInstance().killMotors();
        TeleopStateMachine.getInstance().reset();
    }

    private Command currCmd;
//    private double lastCommandedPosition;

    // AKA: sandstormInit
    @Override
    public void autonomousInit() {
//        teleopInit();
        currCmd = new CmdZeroSystems();
        currCmd.start();
//        new CommandGroup() {
//            public CommandGroup initSubcommands() {
//                addSequential(new CmdZeroLateralSlide());
//                addSequential(new CmdMoveLateralSlide(Constants.EndEffector.LATERAL_SLIDE_CENTRE_METRES));
//                addParallel(new Command() {
//                    @Override
//                    protected void execute() {
//                        EndEffector.ReflectanceSensorArray.LineDeviation lineDeviation =
//                                EndEffector.ReflectanceSensorArray.getInstance().getDeviationFromLine();
//                        if (!lineDeviation.isError() && !CalcUtil.epsilonEquals(lastCommandedPosition, lineDeviation.getValue())) {
//                            lastCommandedPosition = lineDeviation.getValue();
//                            new CmdMoveLateralSlide(lastCommandedPosition + Constants.ReflectanceSensorArray.ARRAY_LENGTH_M / 2, false).start();
//                        }
//                    }
//
//                    @Override
//                    protected boolean isFinished() {
//                        return false;
//                    }
//                });
//                addParallel(new Command() {
//                    private final Runnable runnable = new PeriodicRunnable(
//                            () -> System.out.println(EndEffector.ReflectanceSensorArray.getInstance().toString()),
//                        1000
//                    );
//
//                    @Override
//                    protected void execute() {
//                        runnable.run();
//                    }
//
//                    @Override
//                    protected boolean isFinished() {
//                        return false;
//                    }
//                });
//                return this;
//            }
//        }.initSubcommands().start();
    }

    @Override
    public void teleopInit() {
        // If we're not connected to FMS which ensures autonomousInit is run at start of match, invoke it manually.
        if (!DriverStation.getInstance().isFMSAttached()) {
            autonomousInit();
        }
    }

    @Override
    public void testInit() {}

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
        matchTimeEntry.setDouble(DriverStation.getInstance().getMatchTime());
    }

    @Override
    public void disabledPeriodic() {}

    // AKA: sandstormPeriodic
    @Override
    public void autonomousPeriodic() {
//        teleopPeriodic();
        if (currCmd.isCompleted() && KeyBindings.getInstance().actionToButton(ButtonActions.TEST_GENERIC_0).getPressed()) {
            currCmd = new CmdClimb(ClimbLevel.LEVEL_3);
            currCmd.start();
        }
//        if (currCmd.isCompleted() && KeyBindings.getInstance().actionToButton(ButtonActions.TEST_GENERIC_0).getPressed()) {
//            currCmd = new CmdRotateFrontArms(Constants.FrontArms.VERTICAL_ANGLE_DEGREES, true);
//            currCmd.start();
//        }
//        if (currCmd.isCompleted() && KeyBindings.getInstance().actionToButton(ButtonActions.TEST_GENERIC_1).getPressed()) {
//            currCmd = new CmdRotateFrontArms(Constants.FrontArms.HORIZONTAL_ANGLE_DEGREES, true);
//            currCmd.start();
//        }
    }

    private Button climbLevel2Button = new ButtonPollHelper(ButtonActions.CLIMB_LEVEL2);
    private Button climbLevel3Button = new ButtonPollHelper(ButtonActions.CLIMB_LEVEL3);

    @Override
    public void teleopPeriodic() {
//        FrontArms.getInstance().setPercent(KeyBindings.getInstance().actionToAxis(AxisActions.FRONT_ARMS).get());
//        ClimbLegs.getInstance().getLeftLinearActuator().setPercent(KeyBindings.getInstance().actionToAxis(AxisActions.LEFT_LINEAR_ACTUATOR).get());
//        ClimbLegs.getInstance().getRightLinearActuator().setPercent(KeyBindings.getInstance().actionToAxis(AxisActions.RIGHT_LINEAR_ACTUATOR).get());
        if (climbLevel2Button.getPressed()) {
            TeleopStateMachine.getInstance().climbingTrigger.execute(ClimbLevel.LEVEL_2);
        } else if (climbLevel3Button.getPressed()) {
            TeleopStateMachine.getInstance().climbingTrigger.execute(ClimbLevel.LEVEL_3);
        }
    }

    @Override
    public void testPeriodic() {}

    @Override
    public String getName() {
        return "robot";
    }

    private void buttonPressed(EntryNotification notification) {
        for (String command : notification.value.getStringArray()) {
            handlePress(command);
        }
    }

    private void handlePress(String command) {
        if (isEnabled()) {
            switch (command) {
                case "ELEVATOR_LEVEL_1":
                    new CmdMoveElevator(ElevatorLevel.LEVEL1.getHeightMetres(), true).start();
                    break;
                case "ELEVATOR_LEVEL_2":
                    new CmdMoveElevator(ElevatorLevel.LEVEL2.getHeightMetres(), true).start();
                    break;
                case "ELEVATOR_LEVEL_3":
                    new CmdMoveElevator(ElevatorLevel.LEVEL3.getHeightMetres(), true).start();
                    break;
                case "FRONT_ARMS_VERTICAL":
                    new CmdRotateFrontArms(Constants.FrontArms.VERTICAL_ANGLE_DEGREES, true).start();
                    break;
                case "FRONT_ARMS_HORIZONTAL":
                    new CmdRotateFrontArms(Constants.FrontArms.HORIZONTAL_ANGLE_DEGREES, true).start();
                    break;
                case "EJECT":
                    EndEffector.getInstance().eject();
                    break;
                case "PIN_TOGGLE":
                    EndEffector.getInstance().togglePin();
                    break;
                case "LEFT_DRIVE_WHEELS_FORWARD":
                    Drive.getInstance().setLeftPercent(0.30);
                    break;
                case "LEFT_DRIVE_WHEELS_REVERSE":
                    Drive.getInstance().setLeftPercent(-0.30);
                    break;
                case "RIGHT_DRIVE_WHEELS_FORWARD":
                    Drive.getInstance().setRightPercent(0.30);
                    break;
                case "RIGHT_DRIVE_WHEELS_REVERSE":
                    Drive.getInstance().setRightPercent(-0.30);
                    break;
                case "LINEAR_ACTUATOR_RETRACT":
                    new CmdSynchronousExtendLinearActuators(Constants.ClimbLegs.HOME_METRES);
                    break;
                case "LINEAR_ACTUATOR_EXTEND":
                    new CmdSynchronousExtendLinearActuators(Constants.Field.HAB_LEVEL_1_TO_3_METRES);
                    break;
                case "SPIN_CLIMB_DRIVE_WHEEL":
                    ClimbDrive.getInstance().setPercent(0.30);
                    break;
                case "SHUFFLE_LATERAL_SLIDE":
                    new CommandGroup() {

                        public Command init() {
                            addSequential(new CmdMoveLateralSlide(0.2));
                            addSequential(new CmdMoveLateralSlide(-0.2));
                            addSequential(new CmdMoveLateralSlide(0.2));
                            return this;
                        }
                    }.init().start();
                    break;
            }
        }
    }
    private void buttonRealeased(EntryNotification notification) {
        for (String command : notification.value.getStringArray()) {
            handleRealease(command);
        }
    }
    private void handleRealease(String command) {
        if (isEnabled()) {
            switch (command) {
                case "LEFT_DRIVE_WHEELS_FORWARD":
                    Drive.getInstance().setLeftPercent(0);
                    break;
                case "LEFT_DRIVE_WHEELS_REVERSE":
                    Drive.getInstance().setLeftPercent(0);
                    break;
                case "RIGHT_DRIVE_WHEELS_FORWARD":
                    Drive.getInstance().setRightPercent(0);
                    break;
                case "RIGHT_DRIVE_WHEELS_REVERSE":
                    Drive.getInstance().setRightPercent(0);
                    break;
                case "SPIN_CLIMB_DRIVE_WHEEL":
                    ClimbDrive.getInstance().setPercent(0);
                    break;
            }
        }
    }
}
