package frc.team7170.robot2019;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team7170.lib.Name;
import frc.team7170.lib.Named;
import frc.team7170.lib.TimedRunnable;
import frc.team7170.lib.oi.*;
import frc.team7170.lib.wrappers.AnalogInput;
import frc.team7170.robot2019.actions.AxisActions;
import frc.team7170.robot2019.actions.ButtonActions;
import frc.team7170.robot2019.commands.*;
import frc.team7170.robot2019.subsystems.*;

// TODO: spooky-console: NTBrowser needs ability to make new entry
// TODO: make everything a singleton instead of static
// TODO: "primitive-mode" option

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

        compressor.start();

        // Setup keybindings system
        // KeyBindings.getInstance().registerController(joystick);
        KeyBindings.getInstance().registerController(gamepad);
        KeyBindings.getInstance().registerAxisActions(AxisActions.values());
        KeyBindings.getInstance().registerButtonActions(ButtonActions.values());
        defaultKeyMap = new SerializableKeyMap.Builder(new Name("default"))
                .addPair(ButtonActions.EJECT, gamepad, gamepad.B_A)
                .addPair(ButtonActions.TOGGLE_PIN, gamepad, gamepad.B_B)
                .addPair(AxisActions.LATERAL_SLIDE, gamepad, gamepad.A_RX)
                .build();
        KeyBindings.getInstance().registerKeyMap(defaultKeyMap);
        KeyBindings.getInstance().setCurrKeyMap(defaultKeyMap);

        // Setup subsystem default commands (for whatever reason, this cannot be done in a singleton subsystem constructor)
        Drive.getInstance().setDefaultCommand(new CmdDriveTeleop());
        FrontArms.getInstance().setDefaultCommand(new CmdFrontArmsTeleop());
        Elevator.getInstance().setDefaultCommand(new CmdElevatorTeleop());
        EndEffector.getInstance().setDefaultCommand(new CmdEndEffectorTeleop());
    }

    @Override
    public void disabledInit() {
        // For characterizing the servo feedback:
        // System.out.println(String.format("SD: %f; MEAN: %f",
        //         EndEffector.LateralSlide.getInstance().wdc.getStdDeviation(),
        //         EndEffector.LateralSlide.getInstance().wdc.getMean()));
    }

    // AKA: sandstormInit
    @Override
    public void autonomousInit() {
        new CmdZeroLateralSlide().start();
        new CmdMoveLateralSlide(Constants.EndEffector.LATERAL_SLIDE_CENTRE_METRES).start();
    }

    @Override
    public void teleopInit() {}

    @Override
    public void testInit() {}

    @Override
    public void robotPeriodic() {
         Scheduler.getInstance().run();
         // TODO: currently having CPU issues
    }

    @Override
    public void disabledPeriodic() {}

    // AKA: sandstormPeriodic
    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopPeriodic() {}

    // private TimedRunnable timedRunnable = new TimedRunnable(() -> System.out.println(EndEffector.ReflectanceSensorArray.getInstance()), 3000);

    private TimedRunnable timedRunnable2 = new TimedRunnable(() -> System.out.println(EndEffector.LateralSlide.getInstance().getFeedbackRaw()), 100);

    @Override
    public void testPeriodic() {
        // timedRunnable.run();
        timedRunnable2.run();
        EndEffector.LateralSlide.getInstance().set(KeyBindings.getInstance().actionToAxis(AxisActions.LATERAL_SLIDE).get());
    }

    @Override
    public String getName() {
        return "robot";
    }
}
