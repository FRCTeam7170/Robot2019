package frc.team7170.robot2019;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import frc.team7170.robot2019.subsystems.ClimbLegs;

public final class Constants {

    // public static final Unit<UniversalUnitType> TALON_MAG_ENCODER_ROTATION_UNIT =
    //         Units.newTalonQuadratureEncoderRotationUnit(1024);
    // public static final Unit<UniversalUnitType> TALON_CIMCODER_ROTATION_UNIT =
    //         Units.newTalonQuadratureEncoderRotationUnit(20);

    public static final class Shuffleboard {
        public static final String MAIN_TAB = "main";
    }

    public static final class OI {
        public static final int JOYSTICK_PORT = 0;
        public static final int GAMEPAD_PORT = 1;
    }

    public static final class CAN {
        public static final int PDP = 5;
        public static final int PCM = 6;

        // Drive talons
        public static final int DRIVE_TALON_LEFT_MASTER = 11;  // Front
        public static final int DRIVE_TALON_LEFT_FOLLOWER = 12;  // Back
        public static final int DRIVE_TALON_RIGHT_MASTER = 15;  // Front
        public static final int DRIVE_TALON_RIGHT_FOLLOWER = 16;  // Back

        // Front arm talons
        public static final int FRONT_ARM_TALON_MASTER = 14;  // Right
        public static final int FRONT_ARM_TALON_FOLLOWER = 10;  // Left

        // Climb leg talons
        public static final int CLIMB_LEGS_TALON_LEFT = 13;
        public static final int CLIMB_LEGS_TALON_RIGHT = 17;

        // Elevator victors
        public static final int ELEVATOR_SPARK_MAX_MASTER = 20;  // Left
        public static final int ELEVATOR_SPARK_MAX_FOLLOWER = 21;  // Right

        // Climb drive spark maxes
        public static final int CLIMB_DRIVE_VICTOR_LEFT = 18;
        public static final int CLIMB_DRIVE_VICTOR_RIGHT = 19;
    }

    public static final class DIO {
        // Elevator encoder
        public static final int ELEVATOR_ENCODER_A = 3;
        public static final int ELEVATOR_ENCODER_B = 4;

        // Seat motor DIOs
        // TODO: NOT USED
        public static final int SEAT_MOTOR_DIO_LEFT = 0;
        public static final int SEAT_MOTOR_DIO_RIGHT = 1;

        // Lateral slide
        public static final int LATERAL_SLIDE_LIMIT_SWITCH = 7;

        // Reflectance sensor array
        public static final int REFLECTANCE_SENSOR_ARRAY_SELECT_0 = 8;
        public static final int REFLECTANCE_SENSOR_ARRAY_SELECT_1 = 9;

        public static final int CLIMB_ULTRASONIC_TRIG = 5;
        public static final int CLIMB_ULTRASONIC_ECHO = 6;
    }

    public static final class PWM {
        public static final int LATERAL_SLIDE_SERVO = 9;
    }

    public static final class AIN {
        public static final int REFLECTANCE_SENSOR_ARRAY_0 = 0;
        public static final int REFLECTANCE_SENSOR_ARRAY_1 = 1;
        public static final int REFLECTANCE_SENSOR_ARRAY_2 = 2;
        public static final int LATERAL_SLIDE_SERVO_FEEDBACK = 3;
    }

    public static final class PCM {
        public static final int EJECT_DEPLOY_SOLENOID = 2;
        public static final int EJECT_RETRACT_SOLENOID = 0;
        public static final int PIN_DEPLOY_SOLENOID = 1;
        public static final int PIN_RETRACT_SOLENOID = 3;
    }

    public static final class Camera {
        public static final String CAMERA0_NAME = "Camera 0";
        public static final String CAMERA1_NAME = "Camera 1";
    }

    public static final class Field {
        public static final double HAB_LEVEL_1_TO_3_METRES = 0.4826;
        public static final double HAB_LEVEL_1_TO_2_METRES = 0.1524;
    }

    public static final class Dimensions {
        public static final double WHEEL_DIAMETER_INCHES = 6.0;
        public static final double FRONT_ARM_WHEEL_DIAMETER_INCHES = 2.0;
        public static final double FRONT_ARM_PIVOT_TO_WHEEL_CENTRE_METRES = 0.473;  // 18 5/8 in.
        public static final double FRONT_ARM_PIVOT_HEIGHT_METRES = 0.216;  // 8 1/2 in.
        public static final double LINEAR_ACTUATOR_CONTACT_DISTANCE_METRES = 0.005;
        public static final double LINEAR_ACTUATOR_WHEEL_DIAMETER_INCHES = 4.0;
        public static final double END_EFFECTOR_INNER_WIDTH_METRES = 0.218;
        public static final double PIN_CYCLINDER_DIAMETER_METRES = 0.01559;
    }

    public static final class Drive {
        public static final double RAMP_TIME = 0.1;  // seconds
        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake;
        public static final int ENCODER_CPR = 1024;  // Talon Mag.
        public static final int POSITION_PROFILE = 0;
        public static final int ALLOWABLE_CLOSED_LOOP_POSITION_ERROR = 217;  // raw units (enc_ticks); +-1 in. on 6 in. wheels
        public static final boolean SQUARE_INPUTS = true;

        // Smart tank drive
        public static final boolean ENABLE_TANK_FORWARD_ASSIST = false;
        public static final double TANK_FORWARD_ASSIST_THRESHOLD = 0.05;

        // Talon voltage compensation
        public static final boolean ENABLE_VOLTAGE_COMPENSATION = false;
        public static final double VOLTAGE_COMPENSATION_SATURATION = 12.0;

        // Talon current limiting
        public static final boolean ENABLE_CURRENT_LIMIT = false;
        public static final int CONTINUOUS_CURRENT_LIMIT_AMPS = 40;  // per motor
        public static final int PEAK_CURRENT_LIMIT_AMPS = 60;  // per motor
        public static final int PEAK_CURRENT_LIMIT_DURATION_MS = 100;

        // Talon inversion
        public static final boolean INVERT_LEFT = true;
        public static final boolean INVERT_RIGHT = false;
        public static final boolean SENSOR_PHASE = true;

        // Left PIDF parameters -- position control
        public static final double P_LEFT = 0.1;  // throttle / error
        public static final double I_LEFT = 0.0001;  // throttle / integrated error
        public static final double D_LEFT = 200.0;  // throttle / differentiated error
        public static final double F_LEFT = 0.0;  // multiplied directly by setpoint
        public static final int IZONE_LEFT = 4096;

        // Right PIDF parameters -- position control
        public static final double P_RIGHT = 0.1;  // throttle / error
        public static final double I_RIGHT = 0.0001;  // throttle / integrated error
        public static final double D_RIGHT = 200.0;  // throttle / differentiated error
        public static final double F_RIGHT = 0.0;  // multiplied directly by setpoint
        public static final int IZONE_RIGHT = 4096;

        // Drive multipliers
        public static final double HOME_MULTIPLIER = 1.0;
        public static final double LEVEL1_MULTIPLIER = 1.0;
        public static final double LEVEL2_MULTIPLIER = 0.75;
        public static final double LEVEL3_MULTIPLIER = 0.5;
        public static final double PICKUP_PREPARE_MULTIPLIER = 0.5;
        public static final double LOAD_PREPARE_MULTIPLIER = 1.0;
        public static final double RABBIT_MULTIPLIER = 1.0;
        public static final double TURTLE_MULTIPLIER = 0.5;
    }

    public static final class FrontArms {
        public static final double TOTAL_REDUCTION = 128.0;  // 64 (gearbox) * 2 (chain/sprocket)
        public static final double ZEROING_THROTTLE_PERCENT = 0.25;  // Non-negative.
        public static final double RAMP_TIME = 0.1;  // seconds
        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake;
        public static final int ALLOWABLE_CLOSED_LOOP_ERROR = 25;  // 1.5 degrees
        public static final int ENCODER_CPR = 20;  // CIMCoder
        public static final double MAX_ABSOLUTE_OUTPUT = 1.0;
        public static final double ANGLE_UNCERTAINTY_DEGREES = 5.0;

        // Preset positions
        public static final double HOME_ANGLE_DEGREES = 0.0;
        public static final double VERTICAL_ANGLE_DEGREES = 13.7;
        public static final double HORIZONTAL_ANGLE_DEGREES = VERTICAL_ANGLE_DEGREES + 90.0;
        public static final double PICKUP_ANGLE_DEGREES = 125.0;

        // Voltage compensation
        public static final boolean ENABLE_VOLTAGE_COMPENSATION = false;
        public static final double VOLTAGE_COMPENSATION_SATURATION = 12.0;

        // Current limiting
        public static final boolean ENABLE_CURRENT_LIMIT = false;
        public static final int CONTINUOUS_CURRENT_LIMIT_AMPS = 40;  // per motor
        public static final int PEAK_CURRENT_LIMIT_AMPS = 60;  // per motor
        public static final int PEAK_CURRENT_LIMIT_DURATION_MS = 100;

        // Inversion
        public static final boolean INVERT_LEFT = true;
        public static final boolean INVERT_RIGHT = true;
        public static final boolean SENSOR_PHASE = false;

        // Left PIDF parameters
        public static final double P = 0.5;  // throttle / error
        public static final double I = 0.01;  // throttle / integrated error
        public static final double D = 8;  // throttle / differentiated error
        public static final double F = 0.0;  // multiplied directly by setpoint
        public static final int IZONE = 1000;  // max integrated error to permit I accumulation on
    }

    public static final class ClimbLegs {
        public static final double DISTANCE_FACTOR = 0.00158;  // metres/output_shaft_rotation
        public static final double ZEROING_THROTTLE_PERCENT = 0.5;  // Non-negative.
        public static final double RAMP_TIME = 0.1;  // seconds
        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake;
        public static final int ALLOWABLE_CLOSED_LOOP_ERROR = 100;  // 2 mm
        public static final int ENCODER_CPR = 20;  // CIMCoder
        public static final double SPEED = 0.9;
        public static final double MAX_DIFFERENTIAL_METRES = 0.01;

        // Preset positions
        public static final double HOME_METRES = 0.0;

        // Voltage compensation
        public static final boolean ENABLE_VOLTAGE_COMPENSATION = false;
        public static final double VOLTAGE_COMPENSATION_SATURATION = 12.0;

        // Current limiting
        public static final boolean ENABLE_CURRENT_LIMIT = false;
        public static final int CONTINUOUS_CURRENT_LIMIT_AMPS = 40;  // per motor
        public static final int PEAK_CURRENT_LIMIT_AMPS = 60;  // per motor
        public static final int PEAK_CURRENT_LIMIT_DURATION_MS = 100;

        // Inversion
        public static final boolean INVERT_LEFT = true;
        public static final boolean INVERT_RIGHT = false;
        public static final boolean SENSOR_PHASE_LEFT = false;
        public static final boolean SENSOR_PHASE_RIGHT = false;

        // Left PIDF parameters
        public static final double P_LEFT = 5.0;  // throttle / error
        public static final double I_LEFT = 0.001;  // throttle / integrated error
        public static final double D_LEFT = 50.0;  // throttle / differentiated error
        public static final double F_LEFT = 0.0;  // multiplied directly by setpoint
        public static final int IZONE_LEFT = 500;  // max integrated error to permit I accumulation on

        // Right PIDF parameters
        public static final double P_RIGHT = 5.0;  // throttle / error
        public static final double I_RIGHT = 0.001;  // throttle / integrated error
        public static final double D_RIGHT = 50.0;  // throttle / differentiated error
        public static final double F_RIGHT = 0.0;  // multiplied directly by setpoint
        public static final int IZONE_RIGHT = 500;  // max integrated error to permit I accumulation on
    }

    public static final class ClimbDrive {
        public static final double SEAT_MOTOR_GEAR_RATIO = 174.9;
        public static final double RAMP_TIME = 0.1;  // seconds
        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake;
        public static final double SPEED = 1.0;
        public static final double SONIC_DISTANCE_THRESHOLD_MM = 300.0;

        // Voltage compensation
        public static final boolean ENABLE_VOLTAGE_COMPENSATION = false;
        public static final double VOLTAGE_COMPENSATION_SATURATION = 12.0;

        // Inversion
        public static final boolean INVERT_LEFT = true;
        public static final boolean INVERT_RIGHT = false;
        public static final boolean DIO_INVERT_LEFT = false;
        public static final boolean DIO_INVERT_RIGHT = false;

        // Left PIDF parameters
        // public static final double P_LEFT = 0.0;
        // public static final double I_LEFT = 0.0;
        // public static final double D_LEFT = 0.0;
        // public static final double F_LEFT = 0.0;
        // public static final int IZONE_LEFT = 0;

        // Right PIDF parameters
        // public static final double P_RIGHT = 0.0;
        // public static final double I_RIGHT = 0.0;
        // public static final double D_RIGHT = 0.0;
        // public static final double F_RIGHT = 0.0;
        // public static final int IZONE_RIGHT = 0;
    }

    public static final class Elevator {
        public static final double DISTANCE_FACTOR = 3.224e-4;  // metres/pulse
        public static final double ZEROING_THROTTLE_PERCENT = 0.25;  // Non-negative.
        public static final double RAMP_TIME = 0.1;  // seconds
        public static final CANSparkMax.IdleMode IDLE_MODE = CANSparkMax.IdleMode.kBrake;
        public static final int CURRENT_LIMIT_AMPS = 40;  // per motor
        public static final double TOLERANCE_METRES = 0.01;
        public static final double MANUAL_THRESH = 0.05;

        // Preset positions
        public static final double HOME_METRES = 0.0;
        public static final double RECEIVE_HATCH_PANEL_METRES = 0.0;
        public static final double LEVEL1_METRES = HOME_METRES;
        public static final double LEVEL2_METRES = 0.65;
        public static final double LEVEL3_METRES = 0.0;
        public static final double LOAD_INIT_METRES = 0.0;
        public static final double LOAD_MOVE_UP_METRES = 0.07;

        // Inversion
        public static final boolean INVERT_LEFT = false;
        public static final boolean INVERT_RIGHT = false;
        public static final boolean INVERT_ENCODER = false;

        // PIDF parameters
        public static final double P = 5.0;
        public static final double I = 0.02;
        public static final double D = 0.0;
        public static final double F = 0.0;
        // public static final double IZONE = 0.0;
    }

    public static final class EndEffector {
        public static final double ZEROING_THROTTLE_PERCENT = 1.0;  // Non-negative.

        // Pneumatics timing
        public static final double EJECT_PULSE_DURATION_SECONDS = 0.1;
        public static final double EJECT_SOLENOID_PULSE_DURATION_SECONDS = 0.05;
        public static final double PIN_SOLENOID_PULSE_DURATION_SECONDS = 0.1;

        // Inversion
        public static final boolean INVERT_SERVO = true;
        public static final boolean INVERT_SERVO_FEEDBACK = false;

        // Servo specs
        public static final int SERVO_MAX_US = 1720;
        public static final int SERVO_DEADBAND_MAX_US = 1520;
        public static final int SERVO_CENTRE_US = 1500;
        public static final int SERVO_DEADBAND_MIN_US = 1480;
        public static final int SERVO_MIN_US = 1280;

        // Other servo parameters
        public static final int SERVO_FEEDBACK_POLL_PERIOD_MS = 1;
        public static final int SERVO_FEEDBACK_MAX_POTENTIAL_mV = 3098;  // empirically determined
        public static final int SERVO_FEEDBACK_MIN_POTENTIAL_mV = 104;  // empirically determined
        public static final int SERVO_FEEDBACK_POTENTIAL_RANGE_mV =
                SERVO_FEEDBACK_MAX_POTENTIAL_mV - SERVO_FEEDBACK_MIN_POTENTIAL_mV;
        public static final double SERVO_FEEDBACK_MULTIPLIER =
                (Dimensions.END_EFFECTOR_INNER_WIDTH_METRES - Dimensions.PIN_CYCLINDER_DIAMETER_METRES) / 13760.0;
        public static final double ABSOLUTE_SERVO_SPEED_AUTOMATIC = 1.0;
        public static final double ABSOLUTE_SERVO_SPEED_MANUAL = 1.0;
        public static final double SERVO_FEEDBACK_NEAR_EXTREME_THRESHOLD = 500.0;  // mV
        public static final double SERVO_FEEDBACK_MAX_NOISE_DEVIATION = 3.0;  // standard deviations
        public static final double SERVO_FEEDBACK_STD_DEVIATION = 15.0;  // characterized (sorta)
        public static final double SERVO_FEEDBACK_MEAN = 12.0;  // characterized (sorta)
        public static final int SERVO_FEEDBACK_AVERAGE_BITS = 0;

        // Preset lateral slide positions
        public static final double LATERAL_SLIDE_CENTRE_METRES =
                (Dimensions.END_EFFECTOR_INNER_WIDTH_METRES - Dimensions.PIN_CYCLINDER_DIAMETER_METRES) / 2;
        public static final double LATERAL_SLIDE_LEFT_METRES = 0.0;
        public static final double LATERAL_SLIDE_RIGHT_METRES =
                Dimensions.END_EFFECTOR_INNER_WIDTH_METRES - Dimensions.PIN_CYCLINDER_DIAMETER_METRES;
    }

    public static final class ReflectanceSensorArray {
        // public static final double MIN_DEVIATION_FROM_AVG_VOLTS = 1.5;
        public static final double SENSOR_TRIGGER_THRESHOLD = 1.5;
        public static final double SENSOR_SPACING_M = 0.02009902;
        public static final double SENSOR_WIDTH_M = 0.0075;
        public static final double ARRAY_LENGTH_M = 0.20955;
        // Physically impossible for more than 3 to be triggered on 2 in. tape and at least 2 must be triggered.
        public static final int MAX_TRIGGERED_SENSORS = 3;
        public static final int MIN_TRIGGERED_SENSORS = 2;  // (Unless line is partly out of view)
    }

    public static final class Climb {
        public static final double L2_INITIAL_ANGLE_DEGREES = 113.0;
        public static final double L3_INITIAL_ANGLE_DEGREES = 55.2;  // TODO: increased 10 degrees
//        public static final double DELTA_HEIGHT_METRES = 0.05;
        public static final double FINAL_HEIGHT_EXTRA_METRES = 0.02;
        public static final double TARGET_CLIMB_SPEED = 0.5;
        public static final double MAXIMUM_CLIMB_SPEED = 0.6;
    }

    public static final class Descent {
        public static final double PRE_FRONT_ARM_DEPLOY_DRIVE_FORWARD_METRES = 0.2413;
        public static final double ARM_ANGLE_DEGREES = 143.0;
        public static final double PRE_EXTEND_DRIVE_FORWARD_METRES = 0.860425;
        public static final double LINEAR_ACTUATOR_EXTENSION_METRES = 0.1778;
        public static final boolean HARD_DROP = false;
        public static final double DELTA_HEIGHT_METRES = 0.01;
    }
}
