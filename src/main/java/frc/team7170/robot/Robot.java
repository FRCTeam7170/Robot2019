package frc.team7170.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.team7170.robot.actions.AxisActions;
import frc.team7170.robot.actions.ButtonActions;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.lib.oi.KeyMap;
import frc.team7170.lib.oi.LE3DPJoystick;
import frc.team7170.lib.oi.LF310Gamepad;
import frc.team7170.lib.util.debug.AveragePrinter;
import frc.team7170.robot.commands.CmdDriveTeleop;
import frc.team7170.robot.subsystems.Drive;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

// TODO: Refactor package structure: move all 3rd level packages into frc.team7170.robot (and maybe rename that to robot2019?)

public class Robot extends TimedRobot {

    /**
     * Entry point. ({@code main()} was removed from {@link edu.wpi.first.wpilibj.RobotBase} this year.)
     */
    public static void main(String[] args) {
        Robot.startRobot(Robot::new);
    }

    private LE3DPJoystick joystick;
    private LF310Gamepad gamepad;

    private KeyMap defaultKeyMap;

    private TalonSRX linActuator = new TalonSRX(14);  // TODO: TEMP
    private Encoder encoder = new Encoder(0, 1);  // TODO: TEMP
    private AveragePrinter ap = new AveragePrinter(10, "ENCODER: ");  // TODO: TEMP

    @Override
    public void robotInit() {
        // TODO: allow joystick to be loaded via dashboard
        if (DriverStation.getInstance().getJoystickType(Constants.OI.JOYSTICK_PORT) == 1) {
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
            KeyBindings.registerController(joystick);
        }

        if (DriverStation.getInstance().getJoystickType(Constants.OI.GAMEPAD_PORT) == 1) {
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
            KeyBindings.registerController(gamepad);
        }

        // Setup keybindings
        KeyBindings.registerAxisActions(AxisActions.values());
        KeyBindings.registerButtonActions(ButtonActions.values());
        defaultKeyMap = new KeyMap.Builder("default")
                .addPair(AxisActions.LIN_ACTUATOR, gamepad, gamepad.A_RY)  // TODO: TEMP
                .build();
        KeyBindings.registerKeyMap(defaultKeyMap, false);
        KeyBindings.setCurrKeyMap(defaultKeyMap);

        // Setup subsystem default commands (for whatever reason, this cannot be done in singleton subsystem constructor)
        Drive.INSTANCE.setDefaultCommand(new CmdDriveTeleop());
    }

    @Override
    public void disabledInit() {}

    @Override
    public void autonomousInit() {}

    @Override
    public void teleopInit() {}

    @Override
    public void testInit() {}

    @Override
    public void robotPeriodic() {}

    @Override
    public void disabledPeriodic() {}

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopPeriodic() {
        // TODO: TEMP
        linActuator.set(ControlMode.PercentOutput, KeyBindings.actionToAxis(AxisActions.LIN_ACTUATOR).get());
        ap.feed(encoder.get());
    }

    @Override
    public void testPeriodic() {}
}
