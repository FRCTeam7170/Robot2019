package frc.team7170.robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team7170.lib.Named;
import frc.team7170.lib.unit.*;
import frc.team7170.lib.CalcUtil;
import frc.team7170.robot2019.Constants;

import java.util.logging.Logger;

public class Drive extends Subsystem implements Named {

    private static final Logger LOGGER = Logger.getLogger(Drive.class.getName());

    private static final Unit<UniversalUnitType> DISTANCE_UNIT = Units.newTalonQuadratureEncoderDistanceUnit(
            Constants.TALON_MAG_ENCODER_ROTATION_UNIT, Constants.Dimensions.WHEEL_DIAMETER_INCHES);
    // private static final Unit<UniversalUnitType> VELOCITY_UNIT = Units.newTalonQuadratureEncoderVelocityUnit(
    //         DISTANCE_UNIT);

    private final TalonSRX leftMaster = new TalonSRX(Constants.CAN.DRIVE_TALON_LEFT_MASTER);
    private final TalonSRX leftFollower = new TalonSRX(Constants.CAN.DRIVE_TALON_LEFT_FOLLOWER);
    private final TalonSRX rightMaster = new TalonSRX(Constants.CAN.DRIVE_TALON_RIGHT_MASTER);
    private final TalonSRX rightFollower = new TalonSRX(Constants.CAN.DRIVE_TALON_RIGHT_FOLLOWER);

    private double leftPositionP = Constants.Drive.P_LEFT_POSITION;
    private double leftPositionI = Constants.Drive.I_LEFT_POSITION;
    private double leftPositionD = Constants.Drive.D_LEFT_POSITION;
    private double leftPositionF = Constants.Drive.F_LEFT_POSITION;
    private int leftPositionIZONE = Constants.Drive.IZONE_LEFT_POSITION;

    private double rightPositionP = Constants.Drive.P_RIGHT_POSITION;
    private double rightPositionI = Constants.Drive.I_RIGHT_POSITION;
    private double rightPositionD = Constants.Drive.D_RIGHT_POSITION;
    private double rightPositionF = Constants.Drive.F_RIGHT_POSITION;
    private int rightPositionIZONE = Constants.Drive.IZONE_RIGHT_POSITION;

    // private double leftVelocityP = Constants.Drive.P_LEFT_VELOCITY;
    // private double leftVelocityI = Constants.Drive.I_LEFT_VELOCITY;
    // private double leftVelocityD = Constants.Drive.D_LEFT_VELOCITY;
    // private double leftVelocityF = Constants.Drive.F_LEFT_VELOCITY;
    // private double leftVelocityIZONE = Constants.Drive.IZONE_LEFT_VELOCITY;

    // private double rightVelocityP = Constants.Drive.P_RIGHT_VELOCITY;
    // private double rightVelocityI = Constants.Drive.I_RIGHT_VELOCITY;
    // private double rightVelocityD = Constants.Drive.D_RIGHT_VELOCITY;
    // private double rightVelocityF = Constants.Drive.F_RIGHT_VELOCITY;
    // private double rightVelocityIZONE = Constants.Drive.IZONE_RIGHT_VELOCITY;

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

        leftMaster.config_kP(Constants.Drive.PARAMETER_SLOT_POSITION, leftPositionP);
        leftMaster.config_kI(Constants.Drive.PARAMETER_SLOT_POSITION, leftPositionI);
        leftMaster.config_kD(Constants.Drive.PARAMETER_SLOT_POSITION, leftPositionD);
        leftMaster.config_kF(Constants.Drive.PARAMETER_SLOT_POSITION, leftPositionF);
        leftMaster.config_IntegralZone(Constants.Drive.PARAMETER_SLOT_POSITION, leftPositionIZONE);

        rightMaster.config_kP(Constants.Drive.PARAMETER_SLOT_POSITION, rightPositionP);
        rightMaster.config_kI(Constants.Drive.PARAMETER_SLOT_POSITION, rightPositionI);
        rightMaster.config_kD(Constants.Drive.PARAMETER_SLOT_POSITION, rightPositionD);
        rightMaster.config_kF(Constants.Drive.PARAMETER_SLOT_POSITION, rightPositionF);
        leftMaster.config_IntegralZone(Constants.Drive.PARAMETER_SLOT_POSITION, rightPositionIZONE);

        // leftMaster.config_kP(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.P_LEFT_VELOCITY);
        // leftMaster.config_kI(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.I_LEFT_VELOCITY);
        // leftMaster.config_kD(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.D_LEFT_VELOCITY);
        // leftMaster.config_kF(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.F_LEFT_VELOCITY);
        // leftMaster.config_IntegralZone(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.IZONE_LEFT_VELOCITY);

        // rightMaster.config_kP(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.P_RIGHT_VELOCITY);
        // rightMaster.config_kI(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.I_RIGHT_VELOCITY);
        // rightMaster.config_kD(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.D_RIGHT_VELOCITY);
        // rightMaster.config_kF(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.F_RIGHT_VELOCITY);
        // leftMaster.config_IntegralZone(Constants.Drive.PARAMETER_SLOT_VELOCITY, Constants.Drive.IZONE_RIGHT_VELOCITY);

        leftMaster.configAllowableClosedloopError(Constants.Drive.PARAMETER_SLOT_POSITION,
                Constants.Drive.ALLOWABLE_CLOSED_LOOP_POSITION_ERROR);
        rightMaster.configAllowableClosedloopError(Constants.Drive.PARAMETER_SLOT_POSITION,
                Constants.Drive.ALLOWABLE_CLOSED_LOOP_POSITION_ERROR);

        // leftMaster.configAllowableClosedloopError(Constants.Drive.PARAMETER_SLOT_VELOCITY,
        //         Constants.Drive.ALLOWABLE_CLOSED_LOOP_VELOCITY_ERROR);
        // rightMaster.configAllowableClosedloopError(Constants.Drive.PARAMETER_SLOT_VELOCITY,
        //         Constants.Drive.ALLOWABLE_CLOSED_LOOP_VELOCITY_ERROR);
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

    public void tankDrive(double left, double right) {
        left = CalcUtil.applyBounds(left, -1.0, 1.0);
        right = CalcUtil.applyBounds(right, -1.0, 1.0);

        // setLeftVelocity(metresPerSecondToTalonUnits(left * Constants.Drive.MAX_VELOCITY));
        // setRightVelocity(metresPerSecondToTalonUnits(right * Constants.Drive.MAX_VELOCITY));
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
                return CalcUtil.inThreshold(talonSRX.getClosedLoopError(), 0,
                        Constants.Drive.ALLOWABLE_CLOSED_LOOP_POSITION_ERROR);
            /*
            case Velocity:
                return CalcUtil.inThreshold(talonSRX.getClosedLoopError(), 0,
                        Constants.Drive.ALLOWABLE_CLOSED_LOOP_VELOCITY_ERROR);
             */
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

    /*
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
    */

    /*
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
    */

    @Override
    protected void initDefaultCommand() {}

    private static double talonUnitsToRotations(double value) {
        return Units.convertAndCheck(value, Constants.TALON_MAG_ENCODER_ROTATION_UNIT, Units.REVOLUTION);
    }

    private static double talonUnitsToMetres(double value) {
         return Units.convertAndCheck(value, DISTANCE_UNIT, Units.METRE);
    }

    /*
    private static double talonUnitsToMetresPerSecond(double value) {
        return Units.convertAndCheck(value, VELOCITY_UNIT, Units.METRES_PER_SECOND);
    }
    */

    private static double rotationsToTalonUnits(double value) {
        return Units.convertAndCheck(value, Units.REVOLUTION, Constants.TALON_MAG_ENCODER_ROTATION_UNIT);
    }

    private static double metresToTalonUnits(double value) {
         return Units.convertAndCheck(value, Units.METRE, DISTANCE_UNIT);
    }

    /*
    private static double metresPerSecondToTalonUnits(double value) {
        return Units.convertAndCheck(value, Units.METRES_PER_SECOND, VELOCITY_UNIT);
    }
    */

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);

        /*
        builder.addDoubleProperty(
                "P_LEFT_VELOCITY",
                null,
                P -> leftMaster.config_kP(Constants.Drive.PARAMETER_SLOT_VELOCITY, P)
        );
        builder.addDoubleProperty(
                "I_LEFT_VELOCITY",
                null,
                I -> leftMaster.config_kI(Constants.Drive.PARAMETER_SLOT_VELOCITY, I)
        );
        builder.addDoubleProperty(
                "D_LEFT_VELOCITY",
                null,
                D -> leftMaster.config_kD(Constants.Drive.PARAMETER_SLOT_VELOCITY, D)
        );
        builder.addDoubleProperty(
                "F_LEFT_VELOCITY",
                null,
                F -> leftMaster.config_kP(Constants.Drive.PARAMETER_SLOT_VELOCITY, F)
        );
        builder.addDoubleProperty(
                "IZONE_LEFT_VELOCITY",
                null,
                IZONE -> leftMaster.config_IntegralZone(Constants.Drive.PARAMETER_SLOT_VELOCITY, (int) IZONE)
        );

        builder.addDoubleProperty(
                "P_RIGHT_VELOCITY",
                null,
                P -> rightMaster.config_kP(Constants.Drive.PARAMETER_SLOT_VELOCITY, P)
        );
        builder.addDoubleProperty(
                "I_RIGHT_VELOCITY",
                null,
                I -> rightMaster.config_kI(Constants.Drive.PARAMETER_SLOT_VELOCITY, I)
        );
        builder.addDoubleProperty(
                "D_RIGHT_VELOCITY",
                null,
                D -> rightMaster.config_kD(Constants.Drive.PARAMETER_SLOT_VELOCITY, D)
        );
        builder.addDoubleProperty(
                "F_RIGHT_VELOCITY",
                null,
                F -> rightMaster.config_kP(Constants.Drive.PARAMETER_SLOT_VELOCITY, F)
        );
        builder.addDoubleProperty(
                "IZONE_RIGHT_VELOCITY",
                null,
                IZONE -> rightMaster.config_IntegralZone(Constants.Drive.PARAMETER_SLOT_VELOCITY, (int) IZONE)
        );
        */

        // LEFT POSITION PROPERTIES
        builder.addDoubleProperty(
                "P_LEFT_POSITION",
                () -> leftPositionP,
                P -> {
                    leftPositionP = P;
                    leftMaster.config_kP(Constants.Drive.PARAMETER_SLOT_POSITION, P);
                }
        );
        builder.addDoubleProperty(
                "I_LEFT_POSITION",
                () -> leftPositionI,
                I -> {
                    leftPositionI = I;
                    leftMaster.config_kI(Constants.Drive.PARAMETER_SLOT_POSITION, I);
                }
        );
        builder.addDoubleProperty(
                "D_LEFT_POSITION",
                () -> leftPositionD,
                D -> {
                    leftPositionD = D;
                    leftMaster.config_kD(Constants.Drive.PARAMETER_SLOT_POSITION, D);
                }
        );
        builder.addDoubleProperty(
                "F_LEFT_POSITION",
                () -> leftPositionF,
                F -> {
                    leftPositionF = F;
                    leftMaster.config_kF(Constants.Drive.PARAMETER_SLOT_POSITION, F);
                }
        );
        builder.addDoubleProperty(
                "IZONE_LEFT_POSITION",
                () -> leftPositionIZONE,
                IZONE -> {
                    leftPositionIZONE = (int) IZONE;
                    leftMaster.config_IntegralZone(Constants.Drive.PARAMETER_SLOT_POSITION, (int) IZONE);
                }
        );

        // RIGHT POSITION PROPERTIES
        builder.addDoubleProperty(
                "P_RIGHT_POSITION",
                () -> rightPositionP,
                P -> {
                    rightPositionP = P;
                    rightMaster.config_kP(Constants.Drive.PARAMETER_SLOT_POSITION, P);
                }
        );
        builder.addDoubleProperty(
                "I_RIGHT_POSITION",
                () -> rightPositionI,
                I -> {
                    rightPositionI = I;
                    rightMaster.config_kI(Constants.Drive.PARAMETER_SLOT_POSITION, I);
                }
        );
        builder.addDoubleProperty(
                "D_RIGHT_POSITION",
                () -> rightPositionD,
                D -> {
                    rightPositionD = D;
                    rightMaster.config_kD(Constants.Drive.PARAMETER_SLOT_POSITION, D);
                }
        );
        builder.addDoubleProperty(
                "F_RIGHT_POSITION",
                () -> rightPositionF,
                F -> {
                    rightPositionF = F;
                    rightMaster.config_kF(Constants.Drive.PARAMETER_SLOT_POSITION, F);
                }
        );
        builder.addDoubleProperty(
                "IZONE_RIGHT_POSITION",
                () -> rightPositionIZONE,
                IZONE -> {
                    rightPositionIZONE = (int) IZONE;
                    rightMaster.config_IntegralZone(Constants.Drive.PARAMETER_SLOT_POSITION, (int) IZONE);
                }
        );
    }
}
