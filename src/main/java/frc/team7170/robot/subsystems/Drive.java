package frc.team7170.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team7170.lib.util.CalcUtil;
import frc.team7170.robot.Constants;

public class Drive {

    public static final Drive INSTANCE = new Drive();

    private final TalonSRX leftMaster;
    private final TalonSRX leftFollower;
    private final TalonSRX rightMaster;
    private final TalonSRX rightFollower;

    private Drive() {
        leftMaster = new TalonSRX(Constants.CAN.DRIVE_TALON_LEFT_MASTER);
        leftFollower = new TalonSRX(Constants.CAN.DRIVE_TALON_LEFT_FOLLOWER);
        rightMaster = new TalonSRX(Constants.CAN.DRIVE_TALON_RIGHT_MASTER);
        rightFollower = new TalonSRX(Constants.CAN.DRIVE_TALON_RIGHT_FOLLOWER);

        configTalon(leftMaster, true);
        configTalon(leftFollower, true);
        configTalon(rightMaster, false);
        configTalon(rightFollower, false);

        leftFollower.follow(leftMaster);
        rightFollower.follow(rightMaster);

        leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    }

    private void configTalon(TalonSRX talon, boolean left) {
        talon.configClosedloopRamp(Constants.Drive.RAMP_TIME);
        talon.setNeutralMode(Constants.Drive.NEUTRAL_MODE);
        talon.enableVoltageCompensation(Constants.Drive.ENABLE_VOLTAGE_COMPENSATION);
        talon.configVoltageCompSaturation(Constants.Drive.VOLTAGE_COMPENSATION_SATURATION);
        talon.enableCurrentLimit(Constants.Drive.ENABLE_CURRENT_LIMIT);
        talon.configContinuousCurrentLimit(Constants.Drive.CONTINUOUS_CURRENT_LIMIT_AMPS);
        talon.configPeakCurrentLimit(Constants.Drive.PEAK_CURRENT_LIMIT_AMPS);
        talon.configPeakCurrentDuration(Constants.Drive.PEAK_CURRENT_LIMIT_DURATION_MS);
        talon.configAllowableClosedloopError(0, Constants.Drive.ALLOWABLE_CLOSED_LOOP_ERROR);
        if (left) {
            talon.setInverted(Constants.Drive.INVERT_LEFT);
        } else {  // right
            talon.setInverted(Constants.Drive.INVERT_RIGHT);
        }
        talon.setSensorPhase(Constants.Drive.SENSOR_PHASE);
    }

    // TODO: These are raw positions / velocities... put in more useful units?
    public int getLeftEncoder() {
        return leftMaster.getSelectedSensorPosition();
    }

    public int getRightEncoder() {
        return rightMaster.getSelectedSensorPosition();
    }

    public int getLeftVelocity() {
        return leftMaster.getSelectedSensorVelocity();
    }

    public int getRightVelocity() {
        return rightMaster.getSelectedSensorVelocity();
    }

    public void tankDrive(double left, double right) {
        left = CalcUtil.applyBounds(left, -1.0, 1.0);
        right = CalcUtil.applyBounds(right, -1.0, 1.0);

        leftMaster.set(ControlMode.Velocity, percentToVelocity(left));
        rightMaster.set(ControlMode.Velocity, percentToVelocity(right));
    }

    public void arcadeDrive(double y, double z) {
        // The arcade to tank drive conversion here is copied from DifferentialDrive in WPILib.
        y = CalcUtil.applyBounds(y, -1.0, 1.0);
        z = CalcUtil.applyBounds(z, -1.0, 1.0);

        double l;
        double r;

        double maxInput = Math.copySign(Math.max(Math.abs(y), Math.abs(z)), y);

        if (y >= 0.0) {
            // First quadrant, else second quadrant
            if (z >= 0.0) {
                l = maxInput;
                r = y - z;
            } else {
                l = y + z;
                r = maxInput;
            }
        } else {
            // Third quadrant, else fourth quadrant
            if (z >= 0.0) {
                l = y + z;
                r = maxInput;
            } else {
                l = maxInput;
                r = y - z;
            }
        }

        tankDrive(l, -r);
    }

    private double percentToVelocity(double pct) {
        double revolutionsPerSec = pct * Constants.Drive.MAX_VELOCITY /
                (Math.PI * Constants.Dimensions.WHEEL_DIAMETER);
        // The 0.1 is to convert "per second" into "per 0.1 seconds".
        // The 4 is for the quadrature encoder.
        return revolutionsPerSec * 0.1 * Constants.Drive.ENCODER_CYCLES_PER_REVOLUTION * 4;
    }
}
