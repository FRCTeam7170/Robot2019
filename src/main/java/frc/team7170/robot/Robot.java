package frc.team7170.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.Name;
import frc.team7170.lib.Named;
import frc.team7170.lib.command.CmdTimedRunnable;
import frc.team7170.lib.networktables.Communication;
import frc.team7170.lib.networktables.Receive;
import frc.team7170.lib.oi.KeyMap;
import frc.team7170.robot.actions.AxisActions;
import frc.team7170.robot.actions.ButtonActions;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.lib.oi.SerializableKeyMap;
import frc.team7170.lib.oi.LF310Gamepad;
import frc.team7170.lib.AveragePrinter;

// TODO: Make NT Simulator like in spooky-console
// TODO: spooky-console: NTBrowser needs ability to make new entry
// TODO: make everything a singleton instead of static

public class Robot extends TimedRobot implements Named {

    private class CmdFullExtendLA extends Command {

        @Override
        protected void initialize() {
            System.out.println("INIT EXTEND");
            pidController.enable();
            pidController.setSetpoint(encoderLower);
        }

        @Override
        protected void end() {
            System.out.println("END EXTEND");
            pidController.disable();
        }

        @Override
        protected boolean isFinished() {
            return checkLowerLS();
        }
    }

    private class CmdFullRetractLA extends Command {

        @Override
        protected void initialize() {
            System.out.println("INIT RETRACT");
            pidController.enable();
            pidController.setSetpoint(encoderUpper);
        }

        @Override
        protected void end() {
            System.out.println("END RETRACT");
            pidController.disable();
        }

        @Override
        protected boolean isFinished() {
            return checkUpperLS();
        }
    }

    private class TalonPID implements PIDOutput {
        @Override
        public void pidWrite(double output) {
            System.out.println("COMMANDED OUTPUT: " + output + ", ERROR: " + pidController.getError());
            linActuator.set(ControlMode.PercentOutput, output);
        }
    }

    /**
     * Entry point. ({@code main()} was removed from {@link edu.wpi.first.wpilibj.RobotBase} this year.)
     */
    public static void main(String[] args) {
        Robot.startRobot(Robot::new);
    }

    // private LE3DPJoystick joystick;
    private LF310Gamepad gamepad;

    private KeyMap defaultKeyMap;

    // TODO: TEMP
    private TalonSRX linActuator = new TalonSRX(14);
    private DigitalInput lowerLS = new DigitalInput(0);
    private DigitalInput upperLS = new DigitalInput(1);
    private Encoder encoder = new Encoder(2, 3);
    private AveragePrinter ap = new AveragePrinter(10, "ENCODER: ");

    private double laSpeed = 0.00;
    private CmdTimedRunnable decSpeedCmd;
    private static final int encoderUpper = 0;
    private static final int encoderLower = 6180;
    private boolean zeroed = false;
    private PIDController pidController = new PIDController(0.0, 0.0, 0.0, encoder, new TalonPID(), 0.01);
    private Command currCommand;

    @Override
    public void robotInit() {
        /*
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
        */

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
        // KeyBindings.registerController(joystick);
        KeyBindings.getInstance().registerController(gamepad);
        KeyBindings.getInstance().registerAxisActions(AxisActions.values());
        KeyBindings.getInstance().registerButtonActions(ButtonActions.values());
        // TODO: TEMP
        defaultKeyMap = new SerializableKeyMap.Builder(new Name("default"))
                .addPair(AxisActions.LIN_ACTUATOR, gamepad, gamepad.A_RY)
                .addPair(ButtonActions.INC_LA_SPEED, gamepad, gamepad.B_A)
                .addPair(ButtonActions.DEC_LA_SPEED, gamepad, gamepad.B_B)
                .addPair(ButtonActions.STOP_LA, gamepad, gamepad.B_X)
                .addPair(ButtonActions.FULL_EXTEND_LA, gamepad, gamepad.B_RBUMPER)
                .addPair(ButtonActions.FULL_RETRACT_LA, gamepad, gamepad.B_LBUMPER)
                .addPair(ButtonActions.PRINT_PID, gamepad, gamepad.B_Y)
                .build();
        KeyBindings.getInstance().registerKeyMap(defaultKeyMap);
        KeyBindings.getInstance().setCurrKeyMap(defaultKeyMap);

        // TODO: TEMP TABLE
        Communication.getInstance().registerCommunicator(this, NetworkTableInstance.getDefault().getTable("whattheheck"));
        NetworkTable table = NetworkTableInstance.getDefault().getTable("/Receive");
        table.getEntry("P").setDouble(0.0);
        table.getEntry("I").setDouble(0.0);
        table.getEntry("D").setDouble(0.0);

        linActuator.configOpenloopRamp(1);

        // Setup subsystem default commands (for whatever reason, this cannot be done in a singleton subsystem constructor)
        // Drive.INSTANCE.setDefaultCommand(new CmdDriveTeleop());
    }

    @Override
    public void disabledInit() {
        zeroed = false;
        laSpeed = 0.00;
    }

    @Override
    public void autonomousInit() {
        decSpeedCmd = new CmdTimedRunnable(() -> laSpeed -= 0.01, 500, true);
    }

    @Override
    public void teleopInit() {}

    @Override
    public void testInit() {}

    @Override
    public void robotPeriodic() {
        // Scheduler.getInstance().run();
        // TODO: TEMP
        ap.feed(encoder.get());
    }

    @Override
    public void disabledPeriodic() {}

    @Override
    public void autonomousPeriodic() {
        if (!zeroed) {
            if (!checkUpperLS()) {
                // linActuator.set(ControlMode.PercentOutput, laSpeed);
                linActuator.set(ControlMode.PercentOutput, -0.25);
                if (!encoder.getStopped()) {
                    decSpeedCmd.cancel();
                }
            } else {
                zeroed = true;
                encoder.reset();
                System.out.println("Successfully zeroed encoder.");
            }
        } else {
            if (KeyBindings.getInstance().actionToButton(ButtonActions.FULL_EXTEND_LA).getPressed()) {
                System.out.println("STARTING EXTEND");
                if (currCommand != null) {
                    currCommand.cancel();
                }
                currCommand = new CmdFullExtendLA();
                currCommand.start();
            } else if (KeyBindings.getInstance().actionToButton(ButtonActions.FULL_RETRACT_LA).getPressed()) {
                System.out.println("STARTING RETRACT");
                if (currCommand != null) {
                    currCommand.cancel();
                }
                currCommand = new CmdFullRetractLA();
                currCommand.start();
            } else if (currCommand == null || KeyBindings.getInstance().actionToButton(ButtonActions.STOP_LA).get()) {
                linActuator.set(ControlMode.PercentOutput, 0.0);
            }
        }
    }

    @Override
    public void teleopPeriodic() {
        double reading = KeyBindings.getInstance().actionToAxis(AxisActions.LIN_ACTUATOR).get();
        if (reading > 0.0) {
            if (!checkUpperLS()) {
                linActuator.set(ControlMode.PercentOutput, reading);
            }
        } else {
            if (!checkLowerLS()) {
                linActuator.set(ControlMode.PercentOutput, reading);
            }
        }
        // linActuator.set(ControlMode.PercentOutput, KeyBindings.actionToAxis(AxisActions.LIN_ACTUATOR).get());
    }

    @Override
    public void testPeriodic() {
        if (KeyBindings.getInstance().actionToButton(ButtonActions.INC_LA_SPEED).getPressed()) {
            laSpeed += 0.01;
        } else if (KeyBindings.getInstance().actionToButton(ButtonActions.DEC_LA_SPEED).getPressed()) {
            laSpeed -= 0.01;
        } else if (KeyBindings.getInstance().actionToButton(ButtonActions.STOP_LA).get()) {
            laSpeed = 0.00;
        } else if (KeyBindings.getInstance().actionToButton(ButtonActions.PRINT_PID).getPressed()) {
            System.out.println(String.format("P: %.4f, I: %.4f, D: %.4f", pidController.getP(), pidController.getI(), pidController.getD()));
        }
        linActuator.set(ControlMode.PercentOutput, laSpeed);
    }

    @Override
    public String getName() {
        return "robot";
    }

    private boolean checkUpperLS() {
        if (upperLS.get()) {
            linActuator.set(ControlMode.PercentOutput, 0.0);
            return true;
        }
        return false;
    }

    private boolean checkLowerLS() {
        if (lowerLS.get()) {
            linActuator.set(ControlMode.PercentOutput, 0.0);
            return true;
        }
        return false;
    }

    @Receive("P")
    private void setP(double P) {
        pidController.setP(P);
    }

    @Receive("I")
    private void setI(double I) {
        pidController.setI(I);
    }

    @Receive("D")
    private void setD(double D) {
        pidController.setD(D);
    }
}
