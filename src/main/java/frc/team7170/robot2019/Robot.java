package frc.team7170.robot2019;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.team7170.lib.CalcUtil;
import frc.team7170.lib.Name;
import frc.team7170.lib.Named;
import frc.team7170.lib.command.CmdRunnable;
import frc.team7170.lib.command.CmdSleep;
import frc.team7170.lib.oi.*;
import frc.team7170.robot2019.actions.AxisActions;
import frc.team7170.robot2019.actions.ButtonActions;
import frc.team7170.robot2019.commands.*;
import frc.team7170.robot2019.subsystems.*;

import java.util.concurrent.CopyOnWriteArrayList;

// TODO: spooky-console: NTBrowser needs ability to make new entry
// TODO: make everything a singleton instead of static
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

    //private LE3DPJoystick joystick;
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

        compressor.start();

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
        // KeyBindings.getInstance().registerController(joystick);
        KeyBindings.getInstance().registerController(gamepad);
        KeyBindings.getInstance().registerAxisActions(AxisActions.values());
        KeyBindings.getInstance().registerButtonActions(ButtonActions.values());
        defaultKeyMap = new SerializableKeyMap.Builder(new Name("default"))
                .addPair(AxisActions.DRIVE_L, gamepad, gamepad.A_LY)
                .addPair(AxisActions.DRIVE_R, gamepad, gamepad.A_RY)
                .addPair(AxisActions.ELEVATOR, gamepad, gamepad.A_TRIGGERS)
                // .addPair(AxisActions.LEFT_LINEAR_ACTUATOR, gamepad, gamepad.A_LTRIGGER)
                // .addPair(AxisActions.RIGHT_LINEAR_ACTUATOR, gamepad, gamepad.A_RTRIGGER)
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
//                .addPair(ButtonActions.LOAD, gamepad, gamepad.B_Y)
                // .addPair(ButtonActions.CLIMB, gamepad, gamepad.B_START)
                .addPair(ButtonActions.TOGGLE_PIN, gamepad, gamepad.B_A)
                // .addPair(ButtonActions.TEST_GENERIC_0, gamepad, gamepad.B_START)
                // .addPair(ButtonActions.TEST_GENERIC_1, gamepad, gamepad.B_BACK)
                // .addPair(AxisActions.LEFT_LINEAR_ACTUATOR, gamepad, gamepad.A_LY)
                // .addPair(AxisActions.RIGHT_LINEAR_ACTUATOR, gamepad, gamepad.A_RY)
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
    }

    // private double lastCommandedPosition;

    // AKA: sandstormInit
    @Override
    public void autonomousInit() {
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
        teleopInit();
//        new CmdZeroFrontArms().start();
//        currLeftCommand = new CmdZeroLinearActuator(ClimbLegs.getInstance().getLeftLinearActuator());
//        currRightCommand = new CmdZeroLinearActuator(ClimbLegs.getInstance().getRightLinearActuator());
//        currLeftCommand.start();
//        currRightCommand.start();
//        new CmdZeroSystems().start();
    }

    private static class CmdTest extends Command {

        private final double sleepS;
        private final String print;
        private double  startS;

        public CmdTest(String print, int sleepMs) {
            this.print = print;
            this.sleepS = (double) sleepMs / 1000.0;
        }

        @Override
        protected void initialize() {
            System.out.print("Running '" + print + "'...");
            startS = Timer.getFPGATimestamp();
        }

        @Override
        protected void end() {
            System.out.println("Done.");
        }

        @Override
        protected boolean isFinished() {
            return Timer.getFPGATimestamp() >= (sleepS + startS);
        }
    }

    @Override
    public void teleopInit() {
        // Set these as default cmd in subsystems.
        // new CmdDriveTeleop().start();
        // new CmdFrontArmsTeleop().start();
        // new CmdElevatorTeleop().start();
        // new CmdEndEffectorTeleop().start();
//        System.out.println("STARTING CMD TEST");
//        new CommandGroup() {
//            public CommandGroup init() {
//                addSequential(new CmdTest("1", 1000));
//                addSequential(new CmdTest("2", 1000));
//                addSequential(new CmdTest("3", 1000));
//                addSequential(new CmdTest("4", 1000));
//                return this;
//            }
//        }.init().start();
    }

    @Override
    public void testInit() {}

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
        matchTimeEntry.setDouble(DriverStation.getInstance().getMatchTime());
//        if (KeyBindings.getInstance().actionToButton(ButtonActions.PRINT).get()) {
//            System.out.println(String.format("%.03f, %.03f", EndEffector.LateralSlide.getInstance().getFeedback(), EndEffector.LateralSlide.getInstance().getFeedbackRaw()));
//        }
    }

    @Override
    public void disabledPeriodic() {}

//    private Command currLeftCommand = null;
//    private Command currRightCommand = null;
//    private Command currBothCommand = null;
//    private Command currCommand;
//    private double currSetpoint = 0.0;
//    private int state = 0;

    // AKA: sandstormPeriodic
    @Override
    public void autonomousPeriodic() {
//        if (KeyBindings.getInstance().actionToButton(ButtonActions.NEW_CMD).getPressed()) {
//            if (currCommand != null) {
//                currCommand.cancel();
//            }
//            System.out.println("STARTED NEW CMD");
//            currCommand = new CmdDriveStraight(0.5);
//            currCommand.start();
//        } else if (KeyBindings.getInstance().actionToButton(ButtonActions.CANCEL_CMD).getPressed()) {
//            if (currCommand != null) {
//                System.out.println("CANCELLED CMD");
//                currCommand.cancel();
//                currCommand = null;
//            }
//        } else if (KeyBindings.getInstance().actionToButton(ButtonActions.RESET_ENC).getPressed()) {
//            System.out.println("RESET ENCODERS");
//            Drive.getInstance().zeroEncoders();
//        }
//        if (KeyBindings.getInstance().actionToButton(ButtonActions.TEST_GENERIC_0).getPressed()) {
//            if (currCommand != null) {
//                currCommand.cancel();
//            }
//            switch (state) {
//                case 0:
//                    currCommand = new CommandGroup() {
//                        public CommandGroup init() {
//                            addSequential(new CmdRotateFrontArms(Constants.FrontArms.HORIZONTAL_ANGLE_DEGREES, true));
//                            addSequential(new CmdRunnable(EndEffector.getInstance()::deployPin));
//                            return this;
//                        }
//                    }.init();
//                    break;
//                case 1:
//                    currCommand = new CmdRotateFrontArms(Constants.FrontArms.PICKUP_ANGLE_DEGREES, true);
//                    break;
//                case 2:
//                    currCommand = new CommandGroup() {
//                        public CommandGroup init() {
//                            addSequential(new CmdRotateFrontArms(Constants.FrontArms.VERTICAL_ANGLE_DEGREES, true));
//                            addSequential(new CmdSleep(1000, EndEffector.getInstance()));
//                            addSequential(new CmdRunnable(EndEffector.getInstance()::retractPin, EndEffector.getInstance()));
//                            addSequential(new CmdRotateFrontArms(Constants.FrontArms.HOME_ANGLE_DEGREES, true));
//                            return this;
//                        }
//                    }.init();
//                    state = -1;
//                    break;
//                default:
//                    throw new AssertionError();
//            }
//            currCommand.start();
//            state++;
//            System.out.println("STARTED FA CMD");
//        } else if (KeyBindings.getInstance().actionToButton(ButtonActions.TEST_GENERIC_1).getPressed()) {
//            if (currCommand != null) {
//                currCommand.cancel();
//                System.out.println("CANCELLED FA CMD");
//                currCommand = null;
//            }
//        }
//        if (currLeftCommand.isCompleted() && currRightCommand.isCompleted() && currSetpoint < 0.3) {
//            currSetpoint += 0.05;
//            currLeftCommand = new CmdExtendLinearActuator(ClimbLegs.getInstance().getLeftLinearActuator(), currSetpoint, true);
//            currRightCommand = new CmdExtendLinearActuator(ClimbLegs.getInstance().getRightLinearActuator(), currSetpoint, true);
//            currLeftCommand.start();
//            currRightCommand.start();
//            System.out.println("NEW SETPOINT");
//        }
//        if (currLeftCommand.isCompleted() && currRightCommand.isCompleted() && currBothCommand == null) {
//            currBothCommand = new CmdSynchronousLegsExtend(0.3);
//            currBothCommand.start();
//        }
//        if (KeyBindings.getInstance().actionToButton(ButtonActions.CLIMB).getPressed()) {
//            new CmdClimb(ClimbLevel.LEVEL_3).start();
//        }if (KeyBindings.getInstance().actionToButton(ButtonActions.TEST_GENERIC_0).getPressed()) {
//        if (KeyBindings.getInstance().actionToButton(ButtonActions.TEST_GENERIC_0).getPressed()) {
//            if (currCommand != null) {
//                currCommand.cancel();
//            }
//            currCommand = new CmdMoveElevator(Constants.Elevator.LEVEL2_METRES, true);
//            currCommand.start();
//            System.out.println("STARTED ELEVATOR CMD");
//        } else if (KeyBindings.getInstance().actionToButton(ButtonActions.TEST_GENERIC_1).getPressed()) {
//            if (currCommand != null) {
//                currCommand.cancel();
//                System.out.println("CANCELLED ELEVATOR CMD");
//                currCommand = null;
//            }
//        }
        teleopPeriodic();
    }

    @Override
    public void teleopPeriodic() {
//        FrontArms.getInstance().setPercent(KeyBindings.getInstance().actionToAxis(AxisActions.FRONT_ARMS).get());
//        ClimbLegs.getInstance().getLeftLinearActuator().setPercent(KeyBindings.getInstance().actionToAxis(AxisActions.LEFT_LINEAR_ACTUATOR).get());
//        ClimbLegs.getInstance().getRightLinearActuator().setPercent(KeyBindings.getInstance().actionToAxis(AxisActions.RIGHT_LINEAR_ACTUATOR).get());
//        Elevator.getInstance().setPercent(KeyBindings.getInstance().actionToAxis(AxisActions.ELEVATOR).get());
//        if (KeyBindings.getInstance().actionToButton(ButtonActions.TEST_GENERIC_0).getPressed()) {
//            System.out.println("ZEROING ELEVATOR");
//            Elevator.getInstance().zeroEncoder();
//        }
        double elevatorReading = KeyBindings.getInstance().actionToAxis(AxisActions.ELEVATOR).get();
        if (!CalcUtil.inThreshold(elevatorReading, 0, Constants.Elevator.MANUAL_THRESH)) {
            if (elevatorReading < 0) {
                Elevator.getInstance().setPercent(0.25 * elevatorReading);
            } else {
                Elevator.getInstance().setPercent(elevatorReading);
            }
        }
    }

    // private TimedRunnable timedRunnable = new TimedRunnable(() -> System.out.println(EndEffector.ReflectanceSensorArray.getInstance()), 3000);

    // private TimedRunnable timedRunnable3 = new TimedRunnable(() -> System.out.println(EndEffector.LateralSlide.getInstance().getFeedbackVoltage()), 50);

    @Override
    public void testPeriodic() {
//        timedRunnable.run();
//        timedRunnable3.run();
//        EndEffector.LateralSlide.getInstance().set(KeyBindings.getInstance().actionToAxis(AxisActions.LATERAL_SLIDE).get());
//        if (KeyBindings.getInstance().actionToButton(ButtonActions.RESET_SERVO).getPressed()) {
//            EndEffector.LateralSlide.getInstance().resetFeedback();
//        }
    }

    @Override
    public String getName() {
        return "robot";
    }
}
