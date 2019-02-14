package frc.team7170.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team7170.lib.Named;
import frc.team7170.lib.SeatMotorDIO;
import frc.team7170.lib.unit.Unit;
import frc.team7170.lib.unit.Units;
import frc.team7170.lib.unit.UniversalUnitType;
import frc.team7170.robot.Constants;

import java.util.logging.Logger;

public class ClimbDrive extends Subsystem implements Named {

    private static final Logger LOGGER = Logger.getLogger(ClimbDrive.class.getName());

    // TODO: why can't this just be Units.INCH.multiply(...)
    private static final Unit<UniversalUnitType> DISTANCE_UNIT = Units.REVOLUTION
            .multiply(Units.INCH)
            .multiply(Constants.Dimensions.LINEAR_ACTUATOR_WHEEL_DIAMETER_INCHES * Math.PI /
                    Constants.ClimbDrive.SEAT_MOTOR_GEAR_RATIO);

    public static class SeatMotor extends PIDSubsystem implements Named {

        private final SeatMotorDIO dio;
        private final VictorSPX victorSPX;

        private SeatMotor(String name, int dio, int canID, boolean invert, boolean sensorInvert,
                          double kP, double kI, double kD, double kF) {
            super(name, kP, kI, kD, kF);
            this.dio = new SeatMotorDIO(dio, sensorInvert);
            victorSPX = new VictorSPX(canID);

            victorSPX.setInverted(invert);
            victorSPX.configClosedloopRamp(Constants.ClimbDrive.RAMP_TIME);
            victorSPX.setNeutralMode(Constants.ClimbDrive.NEUTRAL_MODE);
            victorSPX.enableVoltageCompensation(Constants.ClimbDrive.ENABLE_VOLTAGE_COMPENSATION);
            victorSPX.configVoltageCompSaturation(Constants.ClimbDrive.VOLTAGE_COMPENSATION_SATURATION);
        }

        @Override
        protected double returnPIDInput() {
            return dio.getDistance();
        }

        @Override
        protected void usePIDOutput(double output) {
            victorSPX.set(ControlMode.PercentOutput, output);
        }

        public void setPercent(double percent) {
            victorSPX.set(ControlMode.PercentOutput, percent);
        }

        public void setPosition(double distanceMetres) {
            victorSPX.set(ControlMode.Position, ClimbDrive.metresToDioUnits(distanceMetres));
        }

        public boolean isErrorTolerable() {
            return getPIDController().onTarget();
        }

        public void zeroDIO() {
            dio.reset();
        }

        public void killMotor() {
            setPercent(0.0);
        }

        @Override
        protected void initDefaultCommand() {}
    }

    private final SeatMotor leftSeatMotor = new SeatMotor(
            "leftSeatMotor",
            Constants.DIO.SEAT_MOTOR_DIO_LEFT,
            Constants.CAN.CLIMB_DRIVE_VICTOR_LEFT,
            Constants.ClimbDrive.INVERT_LEFT,
            Constants.ClimbDrive.DIO_INVERT_LEFT,
            Constants.ClimbDrive.P_LEFT,
            Constants.ClimbDrive.D_LEFT,
            Constants.ClimbDrive.I_LEFT,
            Constants.ClimbDrive.F_LEFT
    );

    private final SeatMotor rightSeatMotor = new SeatMotor(
            "rightSeatMotor",
            Constants.DIO.SEAT_MOTOR_DIO_RIGHT,
            Constants.CAN.CLIMB_DRIVE_VICTOR_RIGHT,
            Constants.ClimbDrive.INVERT_RIGHT,
            Constants.ClimbDrive.DIO_INVERT_RIGHT,
            Constants.ClimbDrive.P_RIGHT,
            Constants.ClimbDrive.D_RIGHT,
            Constants.ClimbDrive.I_RIGHT,
            Constants.ClimbDrive.F_RIGHT
    );

    private ClimbDrive() {
        super("climbDrive");
    }

    private static final ClimbDrive INSTANCE = new ClimbDrive();

    public static ClimbDrive getInstance() {
        return INSTANCE;
    }

    @Override
    protected void initDefaultCommand() {}

    public SeatMotor getLeftSeatMotor() {
        return leftSeatMotor;
    }

    public SeatMotor getRightSeatMotor() {
        return rightSeatMotor;
    }

    public void setPercent(double percent) {
        leftSeatMotor.setPercent(percent);
        rightSeatMotor.setPercent(percent);
    }

    public void setPosition(double distanceMetres) {
        leftSeatMotor.setPosition(distanceMetres);
        rightSeatMotor.setPosition(distanceMetres);
    }

    public boolean isErrorTolerable() {
        return leftSeatMotor.isErrorTolerable() && rightSeatMotor.isErrorTolerable();
    }

    public void zeroDIOs() {
        leftSeatMotor.zeroDIO();
        rightSeatMotor.zeroDIO();
    }

    public void killMotors() {
        leftSeatMotor.killMotor();
        rightSeatMotor.killMotor();
    }

    public static double metresToDioUnits(double value) {
        return Units.convertAndCheck(value, Units.METRE, DISTANCE_UNIT);
    }
}
