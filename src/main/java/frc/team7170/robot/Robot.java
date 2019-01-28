package frc.team7170.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.team7170.actions.AxisActions;
import frc.team7170.actions.ButtonActions;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.lib.oi.KeyMap;
import frc.team7170.lib.oi.LE3DPJoystick;
import frc.team7170.lib.oi.LF310Gamepad;
import frc.team7170.lib.util.debug.AveragePrinter;

public class Robot extends TimedRobot {

    private LE3DPJoystick joystick = new LE3DPJoystick(new GenericHID(Constants.OI.JOYSTICK_PORT) {
        @Override
        public double getX(Hand hand) {
            return getRawAxis(0);
        }

        @Override
        public double getY(Hand hand) {
            return getRawAxis(1);
        }
    });

    private LF310Gamepad gamepad = new LF310Gamepad(new GenericHID(Constants.OI.GAMEPAD_PORT) {
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

    private KeyMap defaultKeyMap;

    private TalonSRX linActuator = new TalonSRX(14);  // TODO: TEMP
    private Encoder encoder = new Encoder(0, 1);  // TODO: TEMP
    private AveragePrinter ap = new AveragePrinter(10, "ENCODER: ");

    @Override
    public void robotInit() {
        KeyBindings.registerAxisActions(AxisActions.values());
        KeyBindings.registerButtonActions(ButtonActions.values());
        KeyBindings.registerController(joystick);
        KeyBindings.registerController(gamepad);
        defaultKeyMap = new KeyMap.Builder("default")
                .addPair(AxisActions.LIN_ACTUATOR, gamepad, gamepad.A_RY)  // TODO: TEMP
                .build();
        KeyBindings.registerKeyMap(defaultKeyMap, false);
        KeyBindings.setCurrKeyMap(defaultKeyMap);
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
