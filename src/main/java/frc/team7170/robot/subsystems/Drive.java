package frc.team7170.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team7170.lib.Named;
import frc.team7170.lib.unit.*;
import frc.team7170.lib.CalcUtil;
import frc.team7170.robot.Constants;

import java.util.logging.Logger;

public class Drive extends Subsystem implements Named {

    private static final Logger LOGGER = Logger.getLogger(Drive.class.getName());

    private static final Unit<UniversalUnitType> DISTANCE_UNIT = Units.newTalonQuadratureEncoderDistanceUnit(
            Constants.TALON_MAG_ENCODER_ROTATION_UNIT, Constants.Dimensions.WHEEL_DIAMETER_INCHES);
    private static final Unit<UniversalUnitType> VELOCITY_UNIT = Units.newTalonQuadratureEncoderVelocityUnit(
            DISTANCE_UNIT);

    private final TalonSRX leftMaster = new TalonSRX(Constants.CAN.DRIVE_TALON_LEFT_MASTER);
    private final TalonSRX leftFollower = new TalonSRX(Constants.CAN.DRIVE_TALON_LEFT_FOLLOWER);
    private final TalonSRX rightMaster = new TalonSRX(Constants.CAN.DRIVE_TALON_RIGHT_MASTER);
    private final TalonSRX rightFollower = new TalonSRX(Constants.CAN.DRIVE_TALON_RIGHT_FOLLOWER);

    private Drive() {
        super("drive");

        // TODO: motor controller config is getting out of hand here... should prob make a factory class

        configTalon(leftMaster, true);
        configTalon(leftFollower, true);
        configTalon(rightMaster, false);
        configTalon(rightFollower, false);

        leftFollower.follow(leftMaster);
        rightFollower.follow(rightMaster);

        leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);

        leftMaster.config_kP(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.P_LEFT_VELOCITY);
        leftMaster.config_kI(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.I_LEFT_VELOCITY);
        leftMaster.config_kD(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.D_LEFT_VELOCITY);
        leftMaster.config_kF(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.F_LEFT_VELOCITY);
        leftMaster.config_IntegralZone(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.IZONE_LEFT_VELOCITY);

        rightMaster.config_kP(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.P_RIGHT_VELOCITY);
        rightMaster.config_kI(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.I_RIGHT_VELOCITY);
        rightMaster.config_kD(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.D_RIGHT_VELOCITY);
        rightMaster.config_kF(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.F_RIGHT_VELOCITY);
        leftMaster.config_IntegralZone(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.IZONE_RIGHT_VELOCITY);

        leftMaster.config_kP(Constants.Drive.PARAMETER_SLOT_POSITION, Constants.Drive.P_LEFT_POSITION);
        leftMaster.config_kI(Constants.Drive.PARAMETER_SLOT_POSITION, Constants.Drive.I_LEFT_POSITION);
        leftMaster.config_kD(Constants.Drive.PARAMETER_SLOT_POSITION, Constants.Drive.D_LEFT_POSITION);
        leftMaster.config_kF(Constants.Drive.PARAMETER_SLOT_POSITION, Constants.Drive.F_LEFT_POSITION);
        leftMaster.config_IntegralZone(Constants.Drive.PARAMETER_SLOT_POSITION, Constants.Drive.IZONE_LEFT_POSITION);

        rightMaster.config_kP(Constants.Drive.PARAMETER_SLOT_POSITION, Constants.Drive.P_RIGHT_POSITION);
        rightMaster.config_kI(Constants.Drive.PARAMETER_SLOT_POSITION, Constants.Drive.I_RIGHT_POSITION);
        rightMaster.config_kD(Constants.Drive.PARAMETER_SLOT_POSITION, Constants.Drive.D_RIGHT_POSITION);
        rightMaster.config_kF(Constants.Drive.PARAMETER_SLOT_POSITION, Constants.Drive.F_RIGHT_POSITION);
        leftMaster.config_IntegralZone(Constants.Drive.PARAMETER_SLOT_POSITION, Constants.Drive.IZONE_RIGHT_POSITION);

        leftMaster.configAllowableClosedloopError(Constants.Drive.PARAMETER_SLOT_VELOCITY,
                Constants.Drive.ALLOWABLE_CLOSED_LOOP_VELOCITY_ERROR);
        rightMaster.configAllowableClosedloopError(Constants.Drive.PARAMETER_SLOT_VELOCITY,
                Constants.Drive.ALLOWABLE_CLOSED_LOOP_VELOCITY_ERROR);

        leftMaster.configAllowableClosedloopError(Constants.Drive.PARAMETER_SLOT_POSITION,
                Constants.Drive.ALLOWABLE_CLOSED_LOOP_POSITION_ERROR);
        rightMaster.configAllowableClosedloopError(Constants.Drive.PARAMETER_SLOT_POSITION,
                Constants.Drive.ALLOWABLE_CLOSED_LOOP_POSITION_ERROR);
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
    protected void initDefaultCommand() {}

    public void tankDrive(double left, double right) {
        left = CalcUtil.applyBounds(left, -1.0, 1.0);
        right = CalcUtil.applyBounds(right, -1.0, 1.0);

        setLeftVelocity(percentToVelocity(left));
        setRightVelocity(percentToVelocity(right));
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
                (Math.PI * Constants.Dimensions.WHEEL_DIAMETER_INCHES);
        // The 0.1 is to convert "per second" into "per 0.1 seconds".
        // The 4 is for the quadrature encoder.
        return revolutionsPerSec * 0.1 * Constants.Drive.ENCODER_CYCLES_PER_REVOLUTION * 4;
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
                return CalcUtil.inThreshold(talonSRX.getClosedLoopError(), 0,
                        Constants.Drive.ALLOWABLE_CLOSED_LOOP_POSITION_ERROR);
            case Velocity:
                return CalcUtil.inThreshold(talonSRX.getClosedLoopError(), 0,
                        Constants.Drive.ALLOWABLE_CLOSED_LOOP_VELOCITY_ERROR);
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

    public void setVelocity(double metresPerSecond) {
        setLeftVelocity(metresPerSecond);
        setRightVelocity(metresPerSecond);
    }

    public void setLeftVelocity(double metresPerSecond) {
        leftMaster.set(ControlMode.Velocity, metresPerSecondToTalonUnits(metresPerSecond));
    }

    public void setRightVelocity(double metresPerSecond) {
        rightMaster.set(ControlMode.Velocity, metresPerSecondToTalonUnits(metresPerSecond));
    }

    public double getLeftEncoder() {
        return talonUnitsToRotations(leftMaster.getSelectedSensorPosition());
    }

    public double getRightEncoder() {
        return talonUnitsToRotations(rightMaster.getSelectedSensorPosition());
    }

    public double getLeftDistance() {
        return talonUnitsToMetres(leftMaster.getSelectedSensorPosition());
    }

    public double getRightDistance() {
        return talonUnitsToMetres(rightMaster.getSelectedSensorPosition());
    }

    public double getLeftVelocity() {
        return talonUnitsToMetresPerSecond(leftMaster.getSelectedSensorVelocity());
    }

    public double getRightVelocity() {
        return talonUnitsToMetresPerSecond(rightMaster.getSelectedSensorVelocity());
    }

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

    private static double metresToTalonUnits(double value) {
         return Units.convertAndCheck(value, Units.METRE, DISTANCE_UNIT);
    }

    private static double metresPerSecondToTalonUnits(double value) {
        return Units.convertAndCheck(value, Units.METRES_PER_SECOND, VELOCITY_UNIT);
    }

    /*
    @Override
    public String[] reportHeaders() {
        return new String[] {
                "leftEncoder",
                "rightEncoder",

                "leftVelocity",
                "rightVelocity",

                "leftClosedLoopError",
                "rightClosedLoopError",

                "leftClosedLoopTarget",
                "rightClosedLoopTarget",

                "leftFrontBusVoltage",
                "leftBackBusVoltage",
                "rightFrontBusVoltage",
                "rightBackBusVoltage",

                "leftFrontMotorVoltage",
                "leftBackMotorVoltage",
                "rightFrontMotorVoltage",
                "rightBackMotorVoltage",

                "leftFrontMotorPercentage",
                "leftBackMotorPercentage",
                "rightFrontMotorPercentage",
                "rightBackMotorPercentage",

                "leftFrontCurrent",
                "leftBackCurrent",
                "rightFrontCurrent",
                "rightBackCurrent",
        };
    }

    @Override
    public Value[] reportData() {
        return new Value[] {
                ValueFactory.newInteger(getLeftEncoderRaw()),
                ValueFactory.newInteger(getRightEncoderRaw()),

                ValueFactory.newInteger(getLeftVelocityRaw()),
                ValueFactory.newInteger(getRightVelocityRaw()),

                ValueFactory.newInteger(leftMaster.getClosedLoopError()),
                ValueFactory.newInteger(rightMaster.getClosedLoopError()),

                ValueFactory.newFloat(leftMaster.getClosedLoopTarget()),
                ValueFactory.newFloat(rightMaster.getClosedLoopTarget()),

                ValueFactory.newFloat(leftMaster.getBusVoltage()),
                ValueFactory.newFloat(leftFollower.getBusVoltage()),
                ValueFactory.newFloat(rightMaster.getBusVoltage()),
                ValueFactory.newFloat(rightFollower.getBusVoltage()),

                ValueFactory.newFloat(leftMaster.getMotorOutputVoltage()),
                ValueFactory.newFloat(leftFollower.getMotorOutputVoltage()),
                ValueFactory.newFloat(rightMaster.getMotorOutputVoltage()),
                ValueFactory.newFloat(rightFollower.getMotorOutputVoltage()),

                ValueFactory.newFloat(leftMaster.getMotorOutputPercent()),
                ValueFactory.newFloat(leftFollower.getMotorOutputPercent()),
                ValueFactory.newFloat(rightMaster.getMotorOutputPercent()),
                ValueFactory.newFloat(rightFollower.getMotorOutputPercent()),

                ValueFactory.newFloat(leftMaster.getOutputCurrent()),
                ValueFactory.newFloat(leftFollower.getOutputCurrent()),
                ValueFactory.newFloat(rightMaster.getOutputCurrent()),
                ValueFactory.newFloat(rightFollower.getOutputCurrent()),
        };
    }

    @Override
    public String reportName() {
        return "drive";
    }
    */
}
