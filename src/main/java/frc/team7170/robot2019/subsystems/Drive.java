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
import frc.team7170.lib.CalcUtil;
import frc.team7170.robot2019.Constants;

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

    private double P_LEFT = Constants.Drive.P_LEFT;
    private double I_LEFT = Constants.Drive.I_LEFT;
    private double D_LEFT = Constants.Drive.D_LEFT;
    private double F_LEFT = Constants.Drive.F_LEFT;
    private int IZONE_LEFT = Constants.Drive.IZONE_LEFT;

    private double P_RIGHT = Constants.Drive.P_RIGHT;
    private double I_RIGHT = Constants.Drive.I_RIGHT;
    private double D_RIGHT = Constants.Drive.D_RIGHT;
    private double F_RIGHT = Constants.Drive.F_RIGHT;
    private int IZONE_RIGHT = Constants.Drive.IZONE_RIGHT;

    private final NetworkTableEntry leftEncoderEntry;
    private final NetworkTableEntry rightEncoderEntry;

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

        leftMaster.config_kP(Constants.Drive.POSITION_PROFILE, P_LEFT);
        leftMaster.config_kI(Constants.Drive.POSITION_PROFILE, I_LEFT);
        leftMaster.config_kD(Constants.Drive.POSITION_PROFILE, D_LEFT);
        leftMaster.config_kF(Constants.Drive.POSITION_PROFILE, F_LEFT);
        leftMaster.config_IntegralZone(Constants.Drive.POSITION_PROFILE, IZONE_LEFT);

        rightMaster.config_kP(Constants.Drive.POSITION_PROFILE, P_RIGHT);
        rightMaster.config_kI(Constants.Drive.POSITION_PROFILE, I_RIGHT);
        rightMaster.config_kD(Constants.Drive.POSITION_PROFILE, D_RIGHT);
        rightMaster.config_kF(Constants.Drive.POSITION_PROFILE, F_RIGHT);
        rightMaster.config_IntegralZone(Constants.Drive.POSITION_PROFILE, IZONE_RIGHT);

        ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");

        driveTab.add("P_LEFT", P_LEFT).getEntry().addListener(notification -> {
            System.out.println("SET P_LEFT");
            P_LEFT = notification.value.getDouble();
            leftMaster.config_kP(Constants.Drive.POSITION_PROFILE, P_LEFT);
        }, EntryListenerFlags.kUpdate);
        driveTab.add("I_LEFT", I_LEFT).getEntry().addListener(notification -> {
            System.out.println("SET I_LEFT");
            I_LEFT = notification.value.getDouble();
            leftMaster.config_kI(Constants.Drive.POSITION_PROFILE, I_LEFT);
        }, EntryListenerFlags.kUpdate);
        driveTab.add("D_LEFT", D_LEFT).getEntry().addListener(notification -> {
            System.out.println("SET D_LEFT");
            D_LEFT = notification.value.getDouble();
            leftMaster.config_kD(Constants.Drive.POSITION_PROFILE, D_LEFT);
        }, EntryListenerFlags.kUpdate);
        driveTab.add("F_LEFT", F_LEFT).getEntry().addListener(notification -> {
            System.out.println("SET F_LEFT");
            F_LEFT = notification.value.getDouble();
            leftMaster.config_kF(Constants.Drive.POSITION_PROFILE, F_LEFT);
        }, EntryListenerFlags.kUpdate);
        driveTab.add("IZONE_LEFT", IZONE_LEFT).getEntry().addListener(notification -> {
            System.out.println("SET IZONE_LEFT");
            IZONE_LEFT = (int) notification.value.getDouble();
            leftMaster.config_IntegralZone(Constants.Drive.POSITION_PROFILE, IZONE_LEFT);
        }, EntryListenerFlags.kUpdate);

        driveTab.add("P_RIGHT", P_RIGHT).getEntry().addListener(notification -> {
            System.out.println("SET P_RIGHT");
            P_RIGHT = notification.value.getDouble();
            rightMaster.config_kP(Constants.Drive.POSITION_PROFILE, P_RIGHT);
        }, EntryListenerFlags.kUpdate);
        driveTab.add("I_RIGHT", I_RIGHT).getEntry().addListener(notification -> {
            System.out.println("SET I_RIGHT");
            I_RIGHT = notification.value.getDouble();
            rightMaster.config_kI(Constants.Drive.POSITION_PROFILE, I_RIGHT);
        }, EntryListenerFlags.kUpdate);
        driveTab.add("D_RIGHT", D_RIGHT).getEntry().addListener(notification -> {
            System.out.println("SET D_RIGHT");
            D_RIGHT = notification.value.getDouble();
            rightMaster.config_kD(Constants.Drive.POSITION_PROFILE, D_RIGHT);
        }, EntryListenerFlags.kUpdate);
        driveTab.add("F_RIGHT", F_RIGHT).getEntry().addListener(notification -> {
            System.out.println("SET F_RIGHT");
            F_RIGHT = notification.value.getDouble();
            rightMaster.config_kF(Constants.Drive.POSITION_PROFILE, F_RIGHT);
        }, EntryListenerFlags.kUpdate);
        driveTab.add("IZONE_RIGHT", IZONE_RIGHT).getEntry().addListener(notification -> {
            System.out.println("SET IZONE_RIGHT");
            IZONE_RIGHT = (int) notification.value.getDouble();
            rightMaster.config_kF(Constants.Drive.POSITION_PROFILE, IZONE_RIGHT);
        }, EntryListenerFlags.kUpdate);

        driveTab.add("TOLERANCE", Constants.Drive.ALLOWABLE_CLOSED_LOOP_POSITION_ERROR).getEntry().addListener(notification -> {
            System.out.println("SET TOLERANCE");
            leftMaster.configAllowableClosedloopError(Constants.Drive.POSITION_PROFILE, (int) notification.value.getDouble());
            rightMaster.configAllowableClosedloopError(Constants.Drive.POSITION_PROFILE, (int) notification.value.getDouble());
        }, EntryListenerFlags.kUpdate);

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
    }

    @Override
    public void periodic() {
        leftEncoderEntry.setDouble(getLeftEncoder());
        rightEncoderEntry.setDouble(getRightEncoder());
    }

    public void tankDrive(double left, double right) {
        left = CalcUtil.applyBounds(left, -1.0, 1.0);
        right = CalcUtil.applyBounds(right, -1.0, 1.0);

        if (Constants.Drive.ENABLE_SMART_TANK_DRIVE &&
                CalcUtil.inThreshold(left, right, Constants.Drive.SMART_TANK_DRIVE_THRESHOLD)) {
            left = right = (left + right) / 2;
        }

        setLeftPercent(left);
        setRightPercent(right);
    }

    public void arcadeDrive(double y, double z) {
        // The arcade to tank drive conversion here is copied from DifferentialDrive in WPILib.
        y = CalcUtil.applyBounds(y, -1.0, 1.0);
        z = -CalcUtil.applyBounds(z, -1.0, 1.0);  // TODO: negated z

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
        leftMaster.set(ControlMode.Position, metresToTalonUnits(distanceMetres));
    }

    public void setRightPosition(double distanceMetres) {
        rightMaster.set(ControlMode.Position, metresToTalonUnits(distanceMetres));
    }

    public double getLeftEncoder() {
        return leftMaster.getSelectedSensorPosition();
    }

    public double getRightEncoder() {
        return rightMaster.getSelectedSensorPosition();
    }

    @Override
    protected void initDefaultCommand() {}

    /*
    private static double talonUnitsToRotations(double value) {
        return Units.convertAndCheck(value, Constants.TALON_MAG_ENCODER_ROTATION_UNIT, Units.REVOLUTION);
    }

    private static double talonUnitsToMetres(double value) {
         return Units.convertAndCheck(value, DISTANCE_UNIT, Units.METRE);
    }

    private static double talonUnitsToMetresPerSecond(double value) {
        return Units.convertAndCheck(value, VELOCITY_UNIT, Units.METRES_PER_SECOND);
    }

    private static double rotationsToTalonUnits(double value) {
        return Units.convertAndCheck(value, Units.REVOLUTION, Constants.TALON_MAG_ENCODER_ROTATION_UNIT);
    }
    */

    private static double metresToTalonUnits(double value) {
        return value / (Math.PI * Constants.Dimensions.WHEEL_DIAMETER_INCHES / 39.3701) * (Constants.Drive.ENCODER_CPR * 4);
    }

    /*
    private static double metresPerSecondToTalonUnits(double value) {
        return Units.convertAndCheck(value, Units.METRES_PER_SECOND, VELOCITY_UNIT);
    }
    */
}
