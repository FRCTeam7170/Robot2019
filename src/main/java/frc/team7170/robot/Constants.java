package frc.team7170.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import frc.team7170.lib.unit.Unit;
import frc.team7170.lib.unit.Units;
import frc.team7170.lib.unit.UniversalUnitType;

public final class Constants {

    // TODO: do these really belong here?
    public static final Unit<UniversalUnitType> TALON_MAG_ENCODER_ROTATION_UNIT =
            Units.newTalonQuadratureEncoderRotationUnit(1024);
    public static final Unit<UniversalUnitType> TALON_CIMCODER_ROTATION_UNIT =
            Units.newTalonQuadratureEncoderRotationUnit(20);

    public static final class OI {
        public static final int JOYSTICK_PORT = 0;
        public static final int GAMEPAD_PORT = 1;
    }

    public static final class CAN {
        public static final int PDP = 0;
        public static final int PCM = 0;

        // Drive talons
        public static final int DRIVE_TALON_LEFT_MASTER = 0;  // Front
        public static final int DRIVE_TALON_LEFT_FOLLOWER = 0;  // Back
        public static final int DRIVE_TALON_RIGHT_MASTER = 0;  // Front
        public static final int DRIVE_TALON_RIGHT_FOLLOWER = 0;  // Back

        // Front arm talons
        public static final int FRONT_ARM_TALON_MASTER = 0;  // Left
        public static final int FRONT_ARM_TALON_FOLLOWER = 0;  // Right

        // Climb leg talons
        public static final int CLIMB_LEGS_TALON_LEFT = 0;
        public static final int CLIMB_LEGS_TALON_RIGHT = 0;

        // Elevator victors
        public static final int ELEVATOR_SPARK_MAX_MASTER = 0;  // Left
        public static final int ELEVATOR_SPARK_MAX_FOLLOWER = 0;  // Right

        // Climb drive spark maxes
        public static final int CLIMB_DRIVE_VICTOR_LEFT = 0;
        public static final int CLIMB_DRIVE_VICTOR_RIGHT = 0;
    }

    public static final class DIO {
        // Note the drive, front arms, and black legs have encoders plugged directly into the talon data ports.
        // Additionally, the back legs have limit switches plugged directly into the talon data ports.

        // Elevator encoder
        public static final int ELEVATOR_ENCODER_A = 0;
        public static final int ELEVATOR_ENCODER_B = 0;

        // Elevator limit switches
        public static final int ELEVATOR_LIMIT_SWITCH_LOW = 0;
        public static final int ELEVATOR_LIMIT_SWITCH_HIGH = 0;

        // Seat motor DIOs
        public static final int SEAT_MOTOR_DIO_LEFT = 0;
        public static final int SEAT_MOTOR_DIO_RIGHT = 0;
    }

    public static final class PWM {}

    public static final class Field {
        public static final double HAB_LEVEL_1_TO_3_INCHES = 19.0;
        public static final double HAB_LEVEL_1_TO_2_INCHES = 6.0;
    }

    public static final class Dimensions {
        public static final double WHEEL_DIAMETER_INCHES = 6.0;
        public static final double FRONT_ARM_WHEEL_DIAMETER_INCHES = 2.0;
        public static final double FRONT_ARM_PIVOT_TO_WHEEL_CENTRE_METRES = 0.0;
        public static final double FRONT_ARM_PIVOT_HEIGHT_METRES = 0.0;
        public static final double LINEAR_ACTUATOR_CONTACT_DISTANCE_METRES = 0.0;
    }

    public static final class Drive {
        public static final int ENCODER_CYCLES_PER_REVOLUTION = 360;
        public static final double RAMP_TIME = 0.1;  // seconds
        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Coast;

        // Talon voltage compensation
        public static final boolean ENABLE_VOLTAGE_COMPENSATION = false;
        public static final double VOLTAGE_COMPENSATION_SATURATION = 12.0;

        // Talon current limiting
        public static final boolean ENABLE_CURRENT_LIMIT = false;
        public static final int CONTINUOUS_CURRENT_LIMIT_AMPS = 40;  // per motor
        public static final int PEAK_CURRENT_LIMIT_AMPS = 60;  // per motor
        public static final int PEAK_CURRENT_LIMIT_DURATION_MS = 100;

        // Talon inversion
        public static final boolean INVERT_LEFT = false;
        public static final boolean INVERT_RIGHT = false;
        public static final boolean SENSOR_PHASE = false;

        // Left PIDF parameters -- velocity control
        public static final double P_LEFT_VELOCITY = 0.0;  // throttle / error
        public static final double I_LEFT_VELOCITY = 0.0;  // throttle / integrated error
        public static final double D_LEFT_VELOCITY = 0.0;  // throttle / differentiated error
        public static final double F_LEFT_VELOCITY = 0.0;  // multiplied directly by setpoint
        public static final int IZONE_LEFT_VELOCITY = 0;  // max integrated error to permit I accumulation on

        // Right PIDF parameters -- velocity control
        public static final double P_RIGHT_VELOCITY = 0.0;  // throttle / error
        public static final double I_RIGHT_VELOCITY = 0.0;  // throttle / integrated error
        public static final double D_RIGHT_VELOCITY = 0.0;  // throttle / differentiated error
        public static final double F_RIGHT_VELOCITY = 0.0;  // multiplied directly by setpoint
        public static final int IZONE_RIGHT_VELOCITY = 0;  // max integrated error to permit I accumulation on

        // Left PIDF parameters -- position control
        public static final double P_LEFT_POSITION = 0.0;  // throttle / error
        public static final double I_LEFT_POSITION = 0.0;  // throttle / integrated error
        public static final double D_LEFT_POSITION = 0.0;  // throttle / differentiated error
        public static final double F_LEFT_POSITION = 0.0;  // multiplied directly by setpoint
        public static final int IZONE_LEFT_POSITION = 0;  // max integrated error to permit I accumulation on

        // Right PIDF parameters -- position control
        public static final double P_RIGHT_POSITION = 0.0;  // throttle / error
        public static final double I_RIGHT_POSITION = 0.0;  // throttle / integrated error
        public static final double D_RIGHT_POSITION = 0.0;  // throttle / differentiated error
        public static final double F_RIGHT_POSITION = 0.0;  // multiplied directly by setpoint
        public static final int IZONE_RIGHT_POSITION = 0;  // max integrated error to permit I accumulation on

        // Talon parameter slots
        public static final int PARAMETER_SLOT_VELOCITY = 0;
        public static final int PARAMETER_SLOT_POSITION = 1;

        // Talon allowable error
        public static final int ALLOWABLE_CLOSED_LOOP_VELOCITY_ERROR = 0;  // raw units (enc_ticks/0.1s)
        public static final int ALLOWABLE_CLOSED_LOOP_POSITION_ERROR = 0;  // raw units (enc_ticks)

        // Characterized -- TODO
        public static final double ABSOLUTE_MAX_VELOCITY = 5.0;  // m/s
        public static final double MAX_VELOCITY = 0.9 * ABSOLUTE_MAX_VELOCITY;  // m/s
        public static final double VOLTAGE_DEADBAND = 1.0;  // V
    }

    public static final class FrontArms {
        public static final double TOTAL_REDUCTION = 128.0;  // 64 (gearbox) * 2 (chain/sprocket)
        public static final double ZEROING_THROTTLE_PERCENT = 0.25;  // Non-negative.
        public static final double RAMP_TIME = 0.1;  // seconds
        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake;
        public static final int ALLOWABLE_CLOSED_LOOP_ERROR = 0;  // enc_ticks/0.1s

        // Preset positions (degrees)
        public static final double VERTICAL_ANGLE = 0.0;

        // Voltage compensation
        public static final boolean ENABLE_VOLTAGE_COMPENSATION = false;
        public static final double VOLTAGE_COMPENSATION_SATURATION = 12.0;

        // Current limiting
        public static final boolean ENABLE_CURRENT_LIMIT = false;
        public static final int CONTINUOUS_CURRENT_LIMIT_AMPS = 40;  // per motor
        public static final int PEAK_CURRENT_LIMIT_AMPS = 60;  // per motor
        public static final int PEAK_CURRENT_LIMIT_DURATION_MS = 100;

        // Inversion
        public static final boolean INVERT_LEFT = false;
        public static final boolean INVERT_RIGHT = false;
        public static final boolean SENSOR_PHASE = false;

        // Left PIDF parameters
        public static final double P = 0.0;  // throttle / error
        public static final double I = 0.0;  // throttle / integrated error
        public static final double D = 0.0;  // throttle / differentiated error
        public static final double F = 0.0;  // multiplied directly by setpoint
        public static final int IZONE = 0;  // max integrated error to permit I accumulation on
    }

    public static final class ClimbLegs {
        public static final double TOTAL_REDUCTION = 64.0;  // gearbox reduction
        public static final double DISTANCE_FACTOR = 0.0;  // metres/rotation
        public static final double ZEROING_THROTTLE_PERCENT = 0.25;  // Non-negative.
        public static final double RAMP_TIME = 0.1;  // seconds
        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake;
        public static final int ALLOWABLE_CLOSED_LOOP_ERROR = 0;  // enc_ticks/0.1s

        // Preset positions (raw ticks)
        public static final int CLIMB_L2_TICKS = 0;
        public static final int CLIMB_L3_TICKS = 0;

        // Voltage compensation
        public static final boolean ENABLE_VOLTAGE_COMPENSATION = false;
        public static final double VOLTAGE_COMPENSATION_SATURATION = 12.0;

        // Current limiting
        public static final boolean ENABLE_CURRENT_LIMIT = false;
        public static final int CONTINUOUS_CURRENT_LIMIT_AMPS = 40;  // per motor
        public static final int PEAK_CURRENT_LIMIT_AMPS = 60;  // per motor
        public static final int PEAK_CURRENT_LIMIT_DURATION_MS = 100;

        // Inversion
        public static final boolean INVERT_LEFT = false;
        public static final boolean INVERT_RIGHT = false;
        public static final boolean SENSOR_PHASE_LEFT = false;
        public static final boolean SENSOR_PHASE_RIGHT = false;

        // Left PIDF parameters
        public static final double P_LEFT = 0.0;  // throttle / error
        public static final double I_LEFT = 0.0;  // throttle / integrated error
        public static final double D_LEFT = 0.0;  // throttle / differentiated error
        public static final double F_LEFT = 0.0;  // multiplied directly by setpoint
        public static final int IZONE_LEFT = 0;  // max integrated error to permit I accumulation on

        // Right PIDF parameters
        public static final double P_RIGHT = 0.0;  // throttle / error
        public static final double I_RIGHT = 0.0;  // throttle / integrated error
        public static final double D_RIGHT = 0.0;  // throttle / differentiated error
        public static final double F_RIGHT = 0.0;  // multiplied directly by setpoint
        public static final int IZONE_RIGHT = 0;  // max integrated error to permit I accumulation on
    }

    public static final class ClimbDrive {
        public static final double SEAT_MOTOR_GEAR_RATIO = 174.9;
        public static final double RAMP_TIME = 0.1;  // seconds
        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake;

        // Voltage compensation
        public static final boolean ENABLE_VOLTAGE_COMPENSATION = false;
        public static final double VOLTAGE_COMPENSATION_SATURATION = 12.0;

        // Inversion
        public static final boolean INVERT_LEFT = false;
        public static final boolean INVERT_RIGHT = false;
        public static final boolean DIO_INVERT_LEFT = false;
        public static final boolean DIO_INVERT_RIGHT = false;

        // Left PIDF parameters
        public static final double P_LEFT = 0.0;
        public static final double I_LEFT = 0.0;
        public static final double D_LEFT = 0.0;
        public static final double F_LEFT = 0.0;
        // public static final int IZONE_LEFT = 0;

        // Right PIDF parameters
        public static final double P_RIGHT = 0.0;
        public static final double I_RIGHT = 0.0;
        public static final double D_RIGHT = 0.0;
        public static final double F_RIGHT = 0.0;
        // public static final int IZONE_RIGHT = 0;
    }

    public static final class Elevator {
        public static final double TOTAL_REDUCTION = 0.0;  // TODO: consider: gearbox, chain/sprocket, drum diameter
        public static final double RAMP_TIME = 0.1;  // seconds
        public static final CANSparkMax.IdleMode IDLE_MODE = CANSparkMax.IdleMode.kBrake;
        public static final int CURRENT_LIMIT_AMPS = 40;  // per motor

        // Inversion
        public static final boolean INVERT_LEFT = false;
        public static final boolean INVERT_RIGHT = false;
        public static final boolean INVERT_ENCODER = false;

        // PIDF parameters
        public static final double P = 0.0;
        public static final double I = 0.0;
        public static final double D = 0.0;
        public static final double F = 0.0;
        // public static final double IZONE = 0;
    }

    public static final class Climb {
        public static final double L2_BUMPER_DISTANCE_METRES = 0.0;
        public static final double L3_BUMPER_DISTANCE_METRES = 0.0;
        public static final double L2_CONTACT_ANGLE_DEGREES = 0.0;
        public static final double L3_CONTACT_ANGLE_DEGREES = 0.0;
        public static final double DELTA_HEIGHT_METRES = 0.0;
        public static final double FINAL_HEIGHT_EXTRA_METRES = 0.0;
        public static final double PRE_RETRACT_DRIVE_FORWARD_METRES = 0.0;
        public static final double POST_RETRACT_DRIVE_FORWARD_METRES = 0.0;
    }

    public static final class Descent {
        public static final double PRE_FRONT_ARM_DEPLOY_DRIVE_FORWARD_METRES = 0.0;
        public static final double ARM_ANGLE_DEGREES = 0.0;
        public static final double PRE_EXTEND_DRIVE_FORWARD_METRES = 0.0;
        public static final double POST_EXTEND_DRIVE_FORWARD_METRES = 0.0;
        public static final double LINEAR_ACTUATOR_EXTENSION_METRES = 0.0;
        public static final boolean HARD_DROP = false;
        public static final double DELTA_HEIGHT_METRES = 0.0;
    }
}
