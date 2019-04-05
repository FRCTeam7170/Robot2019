package frc.team7170.robot2019;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
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

// TODO: spooky-console: NTBrowser needs ability to make new entry
// TODO: "primitive-mode" option
// TODO: make zeroable interface for all zeroable subsystems
// TODO: replace logger statements with Shuffleboard events

public class Robot extends TimedRobot implements Named {

    /**
     * Entry point. ({@code main()} was removed from {@link edu.wpi.first.wpilibj.RobotBase} this year.)
     */
    public static void main(String[] args) {
        Robot.startRobot(Robot::new);
    }

//    private LE3DPJoystick joystick;
    private LF310Gamepad gamepad;
    private KeyMap defaultKeyMap;

    private final Compressor compressor = new Compressor(Constants.CAN.PCM);

    private final UsbCamera camera0 = CameraServer.getInstance().startAutomaticCapture(Constants.Camera.CAMERA0_NAME, 0);
    // private final UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(Constants.Camera.CAMERA1_NAME, 1);

    private NetworkTableEntry matchTimeEntry;

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

        // compressor.start();
        compressor.stop();

        ShuffleboardTab mainTab = Shuffleboard.getTab(Constants.Shuffleboard.MAIN_TAB);

        mainTab.add(Constants.Camera.CAMERA0_NAME, camera0);
        mainTab.add("compressorEnabled", false).getEntry().addListener(notification -> {
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
                // .addPair(AxisActions.DRIVE_L, gamepad, gamepad.A_LY)
                // .addPair(AxisActions.DRIVE_R, gamepad, gamepad.A_RY)
                .addPair(AxisActions.ELEVATOR, gamepad, gamepad.A_TRIGGERS)
                .addPair(AxisActions.LEFT_LINEAR_ACTUATOR, gamepad, gamepad.A_LY)
                .addPair(AxisActions.RIGHT_LINEAR_ACTUATOR, gamepad, gamepad.A_RY)
                // .addPair(AxisActions.FRONT_ARMS, gamepad, gamepad.A_LY)
                // .addPair(AxisActions.CLIMB_DRIVE, gamepad, gamepad.A_RY)
                .addPair(ButtonActions.ELEVATOR_LEVEL1, gamepad, gamepad.B_B)
                // .addPair(ButtonActions.ELEVATOR_LEVEL2, gamepad, gamepad.B_X)
                // .addPair(ButtonActions.ELEVATOR_LEVEL3, gamepad, gamepad.B_Y)
                // .addPair(ButtonActions.PICKUP, gamepad, gamepad.B_LBUMPER)
                // .addPair(ButtonActions.PICKUP_CANCEL, gamepad, gamepad.B_X)
                .addPair(ButtonActions.EJECT, gamepad, gamepad.B_RBUMPER)
                // .addPair(ButtonActions.EJECT_CANCEL, gamepad, gamepad.B_A)
                // .addPair(ButtonActions.LATERAL_SLIDE_LEFT, gamepad, gamepad.POV270)
                // .addPair(ButtonActions.LATERAL_SLIDE_RIGHT, gamepad, gamepad.POV90)
                // .addPair(ButtonActions.LOAD, gamepad, gamepad.B_Y)
                // .addPair(ButtonActions.CLIMB, gamepad, gamepad.B_START)
                .addPair(ButtonActions.TOGGLE_PIN, gamepad, gamepad.B_A)
                .addPair(ButtonActions.TEST_GENERIC_0, gamepad, gamepad.B_START)
                .addPair(ButtonActions.TEST_GENERIC_1, gamepad, gamepad.B_BACK)
                .build();
        KeyBindings.getInstance().registerKeyMap(defaultKeyMap);
        KeyBindings.getInstance().setCurrKeyMap(defaultKeyMap);
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
    }

    private Command currCmd;

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
//                addSequential(new Command() {
//                    @Override
//                    protected void execute() {
//                        EndEffector.ReflectanceSensorArray.LineDeviation lineDeviation =
//                                EndEffector.ReflectanceSensorArray.getInstance().getDeviationFromLine();
//                        if (!lineDeviation.isError() && !CalcUtil.epsilonEquals(lastCommandedPosition, lineDeviation.getValue())) {
//                            lastCommandedPosition = lineDeviation.getValue();
//                            new CmdMoveLateralSlide(lastCommandedPosition + Constants.ReflectanceSensorArray.ARRAY_LENGTH_M / 2).start();
//                        }
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
        // Set these as default cmd in subsystems.
        new CmdDriveTeleop().start();
        new CmdFrontArmsTeleop().start();
        new CmdElevatorTeleop().start();
        new CmdEndEffectorTeleop().start();
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
    }

    @Override
    public void teleopPeriodic() {
//        FrontArms.getInstance().setPercent(KeyBindings.getInstance().actionToAxis(AxisActions.FRONT_ARMS).get());
        ClimbLegs.getInstance().getLeftLinearActuator().setPercent(KeyBindings.getInstance().actionToAxis(AxisActions.LEFT_LINEAR_ACTUATOR).get());
        ClimbLegs.getInstance().getRightLinearActuator().setPercent(KeyBindings.getInstance().actionToAxis(AxisActions.RIGHT_LINEAR_ACTUATOR).get());
        double elevatorReading = KeyBindings.getInstance().actionToAxis(AxisActions.ELEVATOR).get();
        if (!CalcUtil.inThreshold(elevatorReading, 0, Constants.Elevator.MANUAL_THRESH)) {
            if (elevatorReading < 0) {
                Elevator.getInstance().setPercent(0.25 * elevatorReading);
            } else {
                Elevator.getInstance().setPercent(elevatorReading);
            }
        }
    }

    @Override
    public void testPeriodic() {}

    @Override
    public String getName() {
        return "robot";
    }
}
