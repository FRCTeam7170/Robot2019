package frc.team7170.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team7170.lib.Named;
import frc.team7170.lib.SeatMotorDIO;
import frc.team7170.robot.Constants;

import java.util.logging.Logger;

public class ClimbDrive extends Subsystem implements Named {

    private static final Logger LOGGER = Logger.getLogger(ClimbDrive.class.getName());

    public class SeatMotor extends PIDSubsystem implements Named {

        private final SeatMotorDIO dio;
        private final VictorSPX victor;

        private SeatMotor(String name, int dio, int canID, boolean invert, boolean sensorInvert,
                          double kP, double kI, double kD, double kF) {
            super(name, kP, kI, kD, kF);
            this.dio = new SeatMotorDIO(dio, sensorInvert);
            victor = new VictorSPX(canID);

            victor.setInverted(invert);
            victor.configClosedloopRamp(Constants.ClimbDrive.RAMP_TIME);
            victor.setNeutralMode(Constants.ClimbDrive.NEUTRAL_MODE);
            victor.enableVoltageCompensation(Constants.ClimbDrive.ENABLE_VOLTAGE_COMPENSATION);
            victor.configVoltageCompSaturation(Constants.ClimbDrive.VOLTAGE_COMPENSATION_SATURATION);
        }

        @Override
        protected double returnPIDInput() {
            return dio.getDistance();
        }

        @Override
        protected void usePIDOutput(double output) {
            victor.set(ControlMode.PercentOutput, output);
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
}
