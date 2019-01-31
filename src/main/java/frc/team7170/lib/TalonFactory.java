package frc.team7170.lib;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * More or less a copy of Poof's talon factory class.
 * TODO: not currently used...
 */
public class TalonFactory {

    public static class Config {
        // TODO: add more parameters in here
        public NeutralMode NEUTRAL_MODE = NeutralMode.Coast;
        // This is factory default.
        public double NEUTRAL_DEADBAND = 0.04;

        public boolean ENABLE_CURRENT_LIMIT = false;
        public int PEAK_CURRENT_LIMIT_AMPS = 60;
        public int PEAK_CURRENT_LIMIT_DURATION_MS = 100;
        public int CONTINUOUS_CURRENT_LIMIT_AMPS = 40;

        public boolean ENABLE_SOFT_LIMIT = false;
        public int FORWARD_SOFT_LIMIT = 0;
        public int REVERSE_SOFT_LIMIT = 0;

        public boolean ENABLE_LIMIT_SWITCH = false;

        public double OPEN_LOOP_RAMP_RATE = 0.0;
        public double CLOSED_LOOP_RAMP_RATE = 0.0;

        public boolean INVERTED = false;
        public boolean SENSOR_PHASE = false;

        public VelocityMeasPeriod VELOCITY_MEASUREMENT_PERIOD = VelocityMeasPeriod.Period_100Ms;
        public int VELOCITY_MEASUREMENT_ROLLING_AVERAGE_WINDOW = 64;

        /*  Poofs changed these, we'll leave at default for now...
        public int CONTROL_FRAME_PERIOD_MS = 5;
        public int MOTION_CONTROL_FRAME_PERIOD_MS = 100;
        public int GENERAL_STATUS_FRAME_RATE_MS = 5;
        public int FEEDBACK_STATUS_FRAME_RATE_MS = 100;
        public int QUAD_ENCODER_STATUS_FRAME_RATE_MS = 100;
        public int ANALOG_TEMP_VBAT_STATUS_FRAME_RATE_MS = 100;
        public int PULSE_WIDTH_STATUS_FRAME_RATE_MS = 100;
        */
    }

    // TODO: some default configs?

    public static TalonSRX newTalon(int id, Config config) {
        TalonSRX talon = new TalonSRX(id);

        talon.setNeutralMode(config.NEUTRAL_MODE);
        talon.configNeutralDeadband(config.NEUTRAL_DEADBAND);

        talon.configContinuousCurrentLimit(config.CONTINUOUS_CURRENT_LIMIT_AMPS);
        talon.configPeakCurrentLimit(config.PEAK_CURRENT_LIMIT_AMPS);
        talon.configPeakCurrentDuration(config.PEAK_CURRENT_LIMIT_DURATION_MS);
        talon.enableCurrentLimit(config.ENABLE_CURRENT_LIMIT);

        talon.configForwardSoftLimitThreshold(config.FORWARD_SOFT_LIMIT);
        talon.configReverseSoftLimitThreshold(config.REVERSE_SOFT_LIMIT);
        talon.overrideSoftLimitsEnable(config.ENABLE_SOFT_LIMIT);

        talon.overrideLimitSwitchesEnable(config.ENABLE_LIMIT_SWITCH);

        talon.configOpenloopRamp(config.OPEN_LOOP_RAMP_RATE);
        talon.configClosedloopRamp(config.CLOSED_LOOP_RAMP_RATE);

        talon.setInverted(config.INVERTED);
        talon.setSensorPhase(config.SENSOR_PHASE);

        talon.configVelocityMeasurementPeriod(config.VELOCITY_MEASUREMENT_PERIOD);
        talon.configVelocityMeasurementWindow(config.VELOCITY_MEASUREMENT_ROLLING_AVERAGE_WINDOW);

        return talon;
    }
}
