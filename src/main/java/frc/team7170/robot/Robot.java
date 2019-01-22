package frc.team7170.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.team7170.actions.AxisActions;
import frc.team7170.actions.ButtonActions;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.lib.oi.KeyMap;
import frc.team7170.lib.oi.LE3DPJoystick;
import frc.team7170.lib.oi.LF310Gamepad;

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

    @Override
    public void robotInit() {
        KeyBindings.registerAxisActions(AxisActions.values());
        KeyBindings.registerButtonActions(ButtonActions.values());
        KeyBindings.registerController(joystick);
        KeyBindings.registerController(gamepad);
        defaultKeyMap = new KeyMap.Builder("default").build();  // TODO: add bindings here
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
    public void teleopPeriodic() {}

    @Override
    public void testPeriodic() {}
}
