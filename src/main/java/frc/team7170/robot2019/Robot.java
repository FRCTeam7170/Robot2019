package frc.team7170.robot2019;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team7170.lib.CalcUtil;
import frc.team7170.lib.Name;
import frc.team7170.lib.Named;
import frc.team7170.lib.TimedRunnable;
import frc.team7170.lib.oi.*;
import frc.team7170.robot2019.actions.AxisActions;
import frc.team7170.robot2019.actions.ButtonActions;
import frc.team7170.robot2019.commands.*;
import frc.team7170.robot2019.subsystems.*;

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

    // TODO: TEMP?
    private final Compressor compressor = new Compressor(Constants.CAN.PCM);

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

        compressor.stop();  // TODO: TEMP - compressor stopped

        // Setup keybindings system
        // KeyBindings.getInstance().registerController(joystick);
        KeyBindings.getInstance().registerController(gamepad);
        KeyBindings.getInstance().registerAxisActions(AxisActions.values());
        KeyBindings.getInstance().registerButtonActions(ButtonActions.values());
        defaultKeyMap = new SerializableKeyMap.Builder(new Name("default"))
                .addPair(ButtonActions.RESET_ENC, gamepad, gamepad.B_Y)
                .addPair(AxisActions.DRIVE_L, gamepad, gamepad.A_LY)
                .addPair(AxisActions.DRIVE_R, gamepad, gamepad.A_RY)
                .addPair(ButtonActions.NEW_CMD, gamepad, gamepad.B_A)
                .addPair(ButtonActions.CANCEL_CMD, gamepad, gamepad.B_X)
                .build();
        KeyBindings.getInstance().registerKeyMap(defaultKeyMap);
        KeyBindings.getInstance().setCurrKeyMap(defaultKeyMap);

        // Setup subsystem default commands (for whatever reason, this cannot be done in a singleton subsystem constructor)
        Drive.getInstance().setDefaultCommand(new CmdDriveTeleop());
        // FrontArms.getInstance().setDefaultCommand(new CmdFrontArmsTeleop());
        // Elevator.getInstance().setDefaultCommand(new CmdElevatorTeleop());
        // EndEffector.getInstance().setDefaultCommand(new CmdEndEffectorTeleop());
    }

    @Override
    public void disabledInit() {
        // For characterizing the servo feedback:
        // System.out.println(String.format("SD: %f; MEAN: %f",
        //         EndEffector.LateralSlide.getInstance().wdc.getStdDeviation(),
        //         EndEffector.LateralSlide.getInstance().wdc.getMean()));
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

    }

    @Override
    public void teleopInit() {}

    @Override
    public void testInit() {
        Drive.getInstance();
    }

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
        // TODO: currently having CPU issues
//        if (KeyBindings.getInstance().actionToButton(ButtonActions.PRINT).get()) {
//            System.out.println(String.format("%.03f, %.03f", EndEffector.LateralSlide.getInstance().getFeedback(), EndEffector.LateralSlide.getInstance().getFeedbackRaw()));
//        }
    }

    @Override
    public void disabledPeriodic() {}

    private Command currCommand = null;

    // AKA: sandstormPeriodic
    @Override
    public void autonomousPeriodic() {
        if (KeyBindings.getInstance().actionToButton(ButtonActions.NEW_CMD).getPressed()) {
            if (currCommand != null) {
                currCommand.cancel();
            }
            System.out.println("STARTED NEW CMD");
            currCommand = new CmdDriveStraight(0.5);
            currCommand.start();
        } else if (KeyBindings.getInstance().actionToButton(ButtonActions.CANCEL_CMD).getPressed()) {
            if (currCommand != null) {
                System.out.println("CANCELLED CMD");
                currCommand.cancel();
                currCommand = null;
            }
        } else if (KeyBindings.getInstance().actionToButton(ButtonActions.RESET_ENC).getPressed()) {
            System.out.println("RESET ENCODERS");
            Drive.getInstance().zeroEncoders();
        }
    }

    @Override
    public void teleopPeriodic() {}

    // private TimedRunnable timedRunnable = new TimedRunnable(() -> System.out.println(EndEffector.ReflectanceSensorArray.getInstance()), 3000);

    // private TimedRunnable timedRunnable3 = new TimedRunnable(() -> System.out.println(EndEffector.LateralSlide.getInstance().getFeedbackVoltage()), 50);

    @Override
    public void testPeriodic() {
        // timedRunnable.run();
        // timedRunnable3.run();
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
