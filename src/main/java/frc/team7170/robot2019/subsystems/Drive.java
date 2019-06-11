package frc.team7170.robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.team7170.lib.Named;
import frc.team7170.lib.math.CalcUtil;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.commands.CmdDriveTeleop;

import java.util.logging.Logger;

public class Drive extends Subsystem implements Named {

    private static final Logger LOGGER = Logger.getLogger(Drive.class.getName());

    // private static final Unit<UniversalUnitType> DISTANCE_UNIT = Units.newTalonQuadratureEncoderDistanceUnit(
    //         Constants.TALON_MAG_ENCODER_ROTATION_UNIT, Constants.Dimensions.WHEEL_DIAMETER_INCHES);
    // private static final Unit<UniversalUnitType> VELOCITY_UNIT = Units.newTalonQuadratureEncoderVelocityUnit(
    //         DISTANCE_UNIT);

    private final TalonSRX leftMaster = new TalonSRX(Constants.CAN.DRIVE_TALON_LEFT_MASTER);
    private final TalonSRX leftFollower = new TalonSRX(Constants.CAN.DRIVE_TALON_LEFT_FOLLOWER);
    private final TalonSRX rightMaster = new TalonSRX(Constants.CAN.DRIVE_TALON_RIGHT_MASTER);
    private final TalonSRX rightFollower = new TalonSRX(Constants.CAN.DRIVE_TALON_RIGHT_FOLLOWER);

    private final NetworkTableEntry leftEncoderEntry;
    private final NetworkTableEntry rightEncoderEntry;

    private boolean squareInputs = Constants.Drive.SQUARE_INPUTS;
    private boolean enableTankForwardAssist = Constants.Drive.ENABLE_TANK_FORWARD_ASSIST;
    private double tankForwardAssistThreshold = Constants.Drive.TANK_FORWARD_ASSIST_THRESHOLD;
    private double multiplier = Constants.Drive.TURTLE_MULTIPLIER;
    private boolean isTurtleMode = true;

    private Drive() {
        super("drive");

        configTalon(leftMaster, true);
        configTalon(leftFollower, true);
        configTalon(rightMaster, false);
        configTalon(rightFollower, false);

        leftFollower.follow(leftMaster);
        rightFollower.follow(rightMaster);

        leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);

        leftMaster.config_kP(Constants.Drive.POSITION_PROFILE, Constants.Drive.P_LEFT);
        leftMaster.config_kI(Constants.Drive.POSITION_PROFILE, Constants.Drive.I_LEFT);
        leftMaster.config_kD(Constants.Drive.POSITION_PROFILE, Constants.Drive.D_LEFT);
        leftMaster.config_kF(Constants.Drive.POSITION_PROFILE, Constants.Drive.F_LEFT);
        leftMaster.config_IntegralZone(Constants.Drive.POSITION_PROFILE, Constants.Drive.IZONE_LEFT);

        rightMaster.config_kP(Constants.Drive.POSITION_PROFILE, Constants.Drive.P_RIGHT);
        rightMaster.config_kI(Constants.Drive.POSITION_PROFILE, Constants.Drive.I_RIGHT);
        rightMaster.config_kD(Constants.Drive.POSITION_PROFILE, Constants.Drive.D_RIGHT);
        rightMaster.config_kF(Constants.Drive.POSITION_PROFILE, Constants.Drive.F_RIGHT);
        rightMaster.config_IntegralZone(Constants.Drive.POSITION_PROFILE, Constants.Drive.IZONE_RIGHT);

        ShuffleboardTab driveTab = Shuffleboard.getTab("drive");

        driveTab.add("P_LEFT", Constants.Drive.P_LEFT).getEntry().addListener(notification -> {
            leftMaster.config_kP(Constants.Drive.POSITION_PROFILE, notification.value.getDouble());
        }, EntryListenerFlags.kUpdate);
        driveTab.add("I_LEFT", Constants.Drive.I_LEFT).getEntry().addListener(notification -> {
            leftMaster.config_kI(Constants.Drive.POSITION_PROFILE, notification.value.getDouble());
        }, EntryListenerFlags.kUpdate);
        driveTab.add("D_LEFT", Constants.Drive.D_LEFT).getEntry().addListener(notification -> {
            leftMaster.config_kD(Constants.Drive.POSITION_PROFILE, notification.value.getDouble());
        }, EntryListenerFlags.kUpdate);
        driveTab.add("F_LEFT", Constants.Drive.F_LEFT).getEntry().addListener(notification -> {
            leftMaster.config_kF(Constants.Drive.POSITION_PROFILE, notification.value.getDouble());
        }, EntryListenerFlags.kUpdate);
        driveTab.add("IZONE_LEFT", Constants.Drive.IZONE_LEFT).getEntry().addListener(notification -> {
            leftMaster.config_IntegralZone(Constants.Drive.POSITION_PROFILE, (int) notification.value.getDouble());
        }, EntryListenerFlags.kUpdate);

        driveTab.add("P_RIGHT", Constants.Drive.P_RIGHT).getEntry().addListener(notification -> {
            rightMaster.config_kP(Constants.Drive.POSITION_PROFILE, notification.value.getDouble());
        }, EntryListenerFlags.kUpdate);
        driveTab.add("I_RIGHT", Constants.Drive.I_RIGHT).getEntry().addListener(notification -> {
            rightMaster.config_kI(Constants.Drive.POSITION_PROFILE, notification.value.getDouble());
        }, EntryListenerFlags.kUpdate);
        driveTab.add("D_RIGHT", Constants.Drive.D_RIGHT).getEntry().addListener(notification -> {
            rightMaster.config_kD(Constants.Drive.POSITION_PROFILE, notification.value.getDouble());
        }, EntryListenerFlags.kUpdate);
        driveTab.add("F_RIGHT", Constants.Drive.F_RIGHT).getEntry().addListener(notification -> {
            rightMaster.config_kF(Constants.Drive.POSITION_PROFILE, notification.value.getDouble());
        }, EntryListenerFlags.kUpdate);
        driveTab.add("IZONE_RIGHT", Constants.Drive.IZONE_RIGHT).getEntry().addListener(notification -> {
            rightMaster.config_kF(Constants.Drive.POSITION_PROFILE, (int) notification.value.getDouble());
        }, EntryListenerFlags.kUpdate);

        driveTab.add("TOLERANCE", Constants.Drive.ALLOWABLE_CLOSED_LOOP_POSITION_ERROR).getEntry().addListener(notification -> {
            leftMaster.configAllowableClosedloopError(Constants.Drive.POSITION_PROFILE, (int) notification.value.getDouble());
            rightMaster.configAllowableClosedloopError(Constants.Drive.POSITION_PROFILE, (int) notification.value.getDouble());
        }, EntryListenerFlags.kUpdate);

        driveTab.add("EANBLE_TANK_FORWARD_ASSIST", enableTankForwardAssist).getEntry().addListener(
                notification -> enableTankForwardAssist = notification.value.getBoolean(),
                EntryListenerFlags.kUpdate
        );
        driveTab.add("TANK_FORWARD_ASSIST_THRESHOLD", tankForwardAssistThreshold).getEntry().addListener(
                notification -> tankForwardAssistThreshold = notification.value.getDouble(),
                EntryListenerFlags.kUpdate
        );
        driveTab.add("SQUARE_INPUTS", squareInputs).getEntry().addListener(
                notification -> squareInputs = notification.value.getBoolean(),
                EntryListenerFlags.kUpdate
        );

        leftEncoderEntry = driveTab.add("ENC_LEFT", 0.0).getEntry();
        rightEncoderEntry = driveTab.add("ENC_RIGHT", 0.0).getEntry();

        zeroEncoders();
    }

    private static final Drive INSTANCE = new Drive();

    public static Drive getInstance() {
        return INSTANCE;
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
        if (left) {
            talon.setInverted(Constants.Drive.INVERT_LEFT);
        } else {  // right
            talon.setInverted(Constants.Drive.INVERT_RIGHT);
        }
        talon.setSensorPhase(Constants.Drive.SENSOR_PHASE);
        talon.overrideLimitSwitchesEnable(false);
    }

    @Override
    public void periodic() {
        leftEncoderEntry.setDouble(getLeftEncoder());
        rightEncoderEntry.setDouble(getRightEncoder());
    }

    public void tankDrive(double left, double right) {
        left = CalcUtil.clamp(left, -1.0, 1.0);
        right = CalcUtil.clamp(right, -1.0, 1.0);

        if (enableTankForwardAssist && CalcUtil.inThreshold(left, right, tankForwardAssistThreshold)) {
            left = right = (left + right) / 2;
        }

        if (squareInputs) {
            left = Math.copySign(left*left, left);
            right = Math.copySign(right*right, right);
        }

        setLeftPercent(multiplier * left);
        setRightPercent(multiplier * right);
    }

    public void arcadeDrive(double y, double z) {
        // The arcade to tank drive conversion here is copied from DifferentialDrive in WPILib.
        y = CalcUtil.clamp(y, -1.0, 1.0);
        z = -CalcUtil.clamp(z, -1.0, 1.0);

        if (squareInputs) {
            y = Math.copySign(y*y, y);
            z = Math.copySign(z*z, z);
        }

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

        setLeftPercent(multiplier * l);
        setRightPercent(multiplier * -r);
    }

    public void toRabbitMode() {
        multiplier = Constants.Drive.RABBIT_MULTIPLIER;
        isTurtleMode = false;
    }

    public void toTurtleMode() {
        multiplier = Constants.Drive.TURTLE_MULTIPLIER;
        isTurtleMode = true;
    }

    public void toggleTurtleRabbitMode() {
        if (isTurtleMode) {
            toRabbitMode();
        } else {
            toTurtleMode();
        }
    }

    public void zeroEncoders() {
        leftMaster.setSelectedSensorPosition(0);
        rightMaster.setSelectedSensorPosition(0);
    }

    public void killMotors() {
        setPercent(0.0);
    }

    public boolean isLeftErrorTolerable() {
        return isErrorTolerable(leftMaster);
    }

    public boolean isRightErrorTolerable() {
        return isErrorTolerable(rightMaster);
    }

    public boolean isErrorTolerable() {
        return isLeftErrorTolerable() && isRightErrorTolerable();
    }

    private boolean isErrorTolerable(TalonSRX talonSRX) {
        switch (talonSRX.getControlMode()) {
            case Position:
                return CalcUtil.inThreshold(talonSRX.getClosedLoopError(Constants.Drive.POSITION_PROFILE), 0,
                        Constants.Drive.ALLOWABLE_CLOSED_LOOP_POSITION_ERROR);
            default:
                return true;
        }
    }

    public void setPercent(double percent) {
        setLeftPercent(percent);
        setRightPercent(percent);
    }

    public void setLeftPercent(double percent) {
        leftMaster.set(ControlMode.PercentOutput, percent);
    }

    public void setRightPercent(double percent) {
        rightMaster.set(ControlMode.PercentOutput, percent);
    }

    public void setPosition(double distanceMetres) {
        setLeftPosition(distanceMetres);
        setRightPosition(distanceMetres);
    }

    public void setLeftPosition(double distanceMetres) {
        leftMaster.set(ControlMode.Position, metresToTalonUnits(-distanceMetres));
    }

    public void setRightPosition(double distanceMetres) {
        rightMaster.set(ControlMode.Position, metresToTalonUnits(-distanceMetres));
    }

    public double getLeftEncoder() {
        return leftMaster.getSelectedSensorPosition();
    }

    public double getRightEncoder() {
        return rightMaster.getSelectedSensorPosition();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new CmdDriveTeleop());
    }

    private static double metresToTalonUnits(double value) {
        return value / (Math.PI * Constants.Dimensions.WHEEL_DIAMETER_INCHES / 39.3701) * (Constants.Drive.ENCODER_CPR * 4);
    }
}
