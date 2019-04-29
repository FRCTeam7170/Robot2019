package frc.team7170.robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.team7170.lib.Name;
import frc.team7170.lib.Named;
import frc.team7170.lib.SeatMotorDIO;
import frc.team7170.lib.unit.Units;
import frc.team7170.robot2019.Constants;

import java.util.logging.Logger;

// TODO: no longer using seat motor dios
public class ClimbDrive extends Subsystem implements Named {

    private static final Logger LOGGER = Logger.getLogger(ClimbDrive.class.getName());

    private final Ultrasonic sonic = new Ultrasonic(
            Constants.DIO.CLIMB_ULTRASONIC_TRIG,
            Constants.DIO.CLIMB_ULTRASONIC_ECHO
    );

    public static class SeatMotor implements Named {

        private final Name name;
        private final SeatMotorDIO dio;
        private final VictorSPX victorSPX;

        private final NetworkTableEntry dioEntry;
        private final NetworkTableEntry dioDistanceEntry;

        private SeatMotor(Name name, int dio, int canID, boolean invert, boolean sensorInvert) {
            this.name = name;
            this.dio = new SeatMotorDIO(dio, sensorInvert);
            victorSPX = new VictorSPX(canID);

            this.dio.setDistancePerPulse(
                    Units.convert(Constants.Dimensions.LINEAR_ACTUATOR_WHEEL_DIAMETER_INCHES, Units.INCH, Units.METRE)
                            * Math.PI / Constants.ClimbDrive.SEAT_MOTOR_GEAR_RATIO
            );
            victorSPX.setInverted(invert);
            victorSPX.configClosedloopRamp(Constants.ClimbDrive.RAMP_TIME);
            victorSPX.setNeutralMode(Constants.ClimbDrive.NEUTRAL_MODE);
            victorSPX.enableVoltageCompensation(Constants.ClimbDrive.ENABLE_VOLTAGE_COMPENSATION);
            victorSPX.configVoltageCompSaturation(Constants.ClimbDrive.VOLTAGE_COMPENSATION_SATURATION);

            ShuffleboardTab seatMotorTab = Shuffleboard.getTab(name.getName());
            dioEntry = seatMotorTab.add("dio", 0.0).getEntry();
            dioDistanceEntry = seatMotorTab.add("dioDistance", 0.0).getEntry();
        }

        // TODO: TEMP
        private void updateEntries() {
            dioEntry.setDouble(dio.get());
            dioDistanceEntry.setDouble(getDistanceMetres());
        }

        public void setPercent(double percent) {
            victorSPX.set(ControlMode.PercentOutput, percent);
            dio.motorSetpoint(percent);
        }

        public void zeroDIO() {
            dio.reset();
        }

        public void killMotor() {
            setPercent(0.0);
        }

        public double getDistanceMetres() {
            return dio.getDistance();
        }

        @Override
        public String getName() {
            return name.getName();
        }

        @Override
        public Name getNameObject() {
            return name;
        }
    }

    private final SeatMotor leftSeatMotor = new SeatMotor(
            new Name("leftSeatMotor"),
            Constants.DIO.SEAT_MOTOR_DIO_LEFT,
            Constants.CAN.CLIMB_DRIVE_VICTOR_LEFT,
            Constants.ClimbDrive.INVERT_LEFT,
            Constants.ClimbDrive.DIO_INVERT_LEFT
    );

    private final SeatMotor rightSeatMotor = new SeatMotor(
            new Name("rightSeatMotor"),
            Constants.DIO.SEAT_MOTOR_DIO_RIGHT,
            Constants.CAN.CLIMB_DRIVE_VICTOR_RIGHT,
            Constants.ClimbDrive.INVERT_RIGHT,
            Constants.ClimbDrive.DIO_INVERT_RIGHT
    );

    private ClimbDrive() {
        super("climbDrive");
        sonic.setAutomaticMode(true);
    }

    private static final ClimbDrive INSTANCE = new ClimbDrive();

    public static ClimbDrive getInstance() {
        return INSTANCE;
    }

    @Override
    public void periodic() {
        leftSeatMotor.updateEntries();
        rightSeatMotor.updateEntries();
    }

    @Override
    protected void initDefaultCommand() {}

    public SeatMotor getLeftSeatMotor() {
        return leftSeatMotor;
    }

    public SeatMotor getRightSeatMotor() {
        return rightSeatMotor;
    }

    public double getSonicDistance() {
        return sonic.getRangeMM();
    }

    public void setPercent(double percent) {
        leftSeatMotor.setPercent(percent);
        rightSeatMotor.setPercent(percent);
    }

    public void zeroDIOs() {
        leftSeatMotor.zeroDIO();
        rightSeatMotor.zeroDIO();
    }

    public void killMotors() {
        leftSeatMotor.killMotor();
        rightSeatMotor.killMotor();
    }

    public double getAvgDistanceMetres() {
        return (leftSeatMotor.getDistanceMetres() + rightSeatMotor.getDistanceMetres()) / 2;
    }
}
