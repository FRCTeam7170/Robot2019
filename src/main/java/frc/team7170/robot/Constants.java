package frc.team7170.robot;


import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Constants {

    public static class OI {
        public static final int JOYSTICK_PORT = 0;
        public static final int GAMEPAD_PORT = 1;
    }

    public static class CAN {
        public static final int PDP = 0;
        public static final int PCM = 0;

        // Front arm talons
        public static final int FRONT_ARM_TALON_MASTER = 0;  // Left
        public static final int FRONT_ARM_TALON_FOLLOWER = 0;  // Right

        // Back leg talons
        public static final int BACK_LEG_TALON_MASTER = 0;  // Left
        public static final int BACK_LEG_TALON_FOLLOWER = 0;  // Right

        // Drive talons
        public static final int DRIVE_TALON_LEFT_MASTER = 0;  // Front
        public static final int DRIVE_TALON_LEFT_FOLLOWER = 0;  // Back
        public static final int DRIVE_TALON_RIGHT_MASTER = 0;  // Front
        public static final int DRIVE_TALON_RIGHT_FOLLOWER = 0;  // Back
    }

    public static class DIO {}

    public static class PWM {}

    public static class Dimensions {
        public static class Climb {
            public static final double BACK_ARM_LENGTH = 20.0;  // inches
            public static final double FRONT_ARM_LENGTH = 20.0;  // inches
        }

        public static class Field {
            public static final double HAB_LEVEL_1_TO_3 = 19.0;  // inches
            public static final double HAB_LEVEL_1_TO_2 = 6.0;  // inches
        }

        public static final double WHEEL_DIAMETER = 6.0;  // inches
    }

    public static class Drive {
        public static final int ENCODER_CYCLES_PER_REVOLUTION = 360;
        public static final double RAMP_TIME = 0.1;  // seconds
        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Coast;
        public static final int ALLOWABLE_CLOSED_LOOP_ERROR = 0;  // enc_ticks/0.1s

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

        // PIDF parameters
        public static final double P_LEFT = 0.0;  // throttle / error
        public static final double I_LEFT = 0.0;  // throttle / integrated error
        public static final double D_LEFT = 0.0;  // throttle / differentiated error
        public static final double F_LEFT = 0.0;  // multiplied directly by setpoint
        public static final double P_RIGHT = 0.0;  // throttle / error
        public static final double I_RIGHT = 0.0;  // throttle / integrated error
        public static final double D_RIGHT = 0.0;  // throttle / differentiated error
        public static final double F_RIGHT = 0.0;  // multiplied directly by setpoint

        // Characterized -- TODO
        public static final double ABSOLUTE_MAX_VELOCITY = 5.0;  // ft/s
        public static final double MAX_VELOCITY = 0.9 * ABSOLUTE_MAX_VELOCITY;  // ft/s
        public static final double VOLTAGE_DEADBAND = 1.0;  // V
    }
}
