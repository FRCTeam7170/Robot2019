package frc.team7170.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team7170.lib.Name;
import frc.team7170.lib.Named;
import frc.team7170.lib.oi.*;
import frc.team7170.robot.actions.AxisActions;
import frc.team7170.robot.actions.ButtonActions;
import frc.team7170.robot.subsystems.ClimbLegs;

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

    private LE3DPJoystick joystick;
    private LF310Gamepad gamepad;

    private KeyMap defaultKeyMap;

    @Override
    public void robotInit() {
        joystick = new LE3DPJoystick(new GenericHID(Constants.OI.JOYSTICK_PORT) {
            @Override
            public double getX(Hand hand) {
                return getRawAxis(0);
            }

            @Override
            public double getY(Hand hand) {
                return getRawAxis(1);
            }
        });

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

        // Setup keybindings system
        KeyBindings.getInstance().registerController(joystick);
        KeyBindings.getInstance().registerController(gamepad);
        KeyBindings.getInstance().registerAxisActions(AxisActions.values());
        KeyBindings.getInstance().registerButtonActions(ButtonActions.values());
        defaultKeyMap = new SerializableKeyMap.Builder(new Name("default"))
                .addPair(AxisActions.LIN_ACTUATOR, gamepad, gamepad.A_RY)
                .build();
        KeyBindings.getInstance().registerKeyMap(defaultKeyMap);
        KeyBindings.getInstance().setCurrKeyMap(defaultKeyMap);

        // Setup subsystem default commands (for whatever reason, this cannot be done in a singleton subsystem constructor)
        // Drive.INSTANCE.setDefaultCommand(new CmdDriveTeleop());
    }

    @Override
    public void disabledInit() {}

    @Override
    public void autonomousInit() {}

    @Override
    public void teleopInit() {
        double value = KeyBindings.getInstance().actionToAxis(AxisActions.LIN_ACTUATOR).get();
        ClimbLegs.getInstance().getLeftLinearActuator().setPercent(value);
        ClimbLegs.getInstance().getRightLinearActuator().setPercent(value);
    }

    @Override
    public void testInit() {}

    @Override
    public void robotPeriodic() {
         Scheduler.getInstance().run();
    }

    @Override
    public void disabledPeriodic() {}

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopPeriodic() {}

    @Override
    public void testPeriodic() {}

    @Override
    public String getName() {
        return "robot";
    }
}
