package frc.team7170.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team7170.lib.Named;
import frc.team7170.lib.unit.Unit;
import frc.team7170.lib.unit.Units;
import frc.team7170.lib.unit.UniversalUnitType;
import frc.team7170.lib.CalcUtil;
import frc.team7170.robot.Constants;

import java.util.logging.Logger;

public class ClimbLegs extends Subsystem implements Named {

    private static final Logger LOGGER = Logger.getLogger(ClimbLegs.class.getName());

    private static final Unit<UniversalUnitType> DISTANCE_UNIT = Constants.TALON_CIMCODER_ROTATION_UNIT
            .multiply(Units.METRE).multiply(Constants.ClimbLegs.DISTANCE_FACTOR / Constants.ClimbLegs.TOTAL_REDUCTION);

    public static class LinearActuator extends Subsystem implements Named {

        private final TalonSRX talon;

        private LinearActuator(String name, int id, boolean invert, boolean sensorPhase,
                               double kP, double kI, double kD, double kF, int kIZONE) {
            super(name);
            talon = new TalonSRX(id);

            talon.config_kP(0, kP);
            talon.config_kI(0, kI);
            talon.config_kD(0, kD);
            talon.config_kF(0, kF);
            talon.config_IntegralZone(0, kIZONE);

            talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
            talon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);
            talon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);
            talon.configClosedloopRamp(Constants.ClimbLegs.RAMP_TIME);
            talon.setNeutralMode(Constants.ClimbLegs.NEUTRAL_MODE);
            talon.enableVoltageCompensation(Constants.ClimbLegs.ENABLE_VOLTAGE_COMPENSATION);
            talon.configVoltageCompSaturation(Constants.ClimbLegs.VOLTAGE_COMPENSATION_SATURATION);
            talon.enableCurrentLimit(Constants.ClimbLegs.ENABLE_CURRENT_LIMIT);
            talon.configContinuousCurrentLimit(Constants.ClimbLegs.CONTINUOUS_CURRENT_LIMIT_AMPS);
            talon.configPeakCurrentLimit(Constants.ClimbLegs.PEAK_CURRENT_LIMIT_AMPS);
            talon.configPeakCurrentDuration(Constants.ClimbLegs.PEAK_CURRENT_LIMIT_DURATION_MS);
            talon.configAllowableClosedloopError(0, Constants.ClimbLegs.ALLOWABLE_CLOSED_LOOP_ERROR);
            talon.setInverted(invert);
            talon.setSensorPhase(sensorPhase);
        }

        public void setPercent(double percent) {
            talon.set(ControlMode.PercentOutput, percent);
        }

        public void setPosition(double metres) {
            talon.set(ControlMode.Position, metresToTalonUnits(metres));
        }

        public boolean isErrorTolerable() {
            return CalcUtil.inThreshold(talon.getClosedLoopError(), 0,
                    Constants.ClimbLegs.ALLOWABLE_CLOSED_LOOP_ERROR);
        }

        public boolean isLowerLimitSwitchTriggered() {
            return talon.getSensorCollection().isFwdLimitSwitchClosed();
        }

        public boolean isUpperLimitSwitchTriggered() {
            return talon.getSensorCollection().isRevLimitSwitchClosed();
        }

        public void zeroEncoder() {
            talon.setSelectedSensorPosition(0);
        }

        public void killMotor() {
            setPercent(0.0);
        }

        @Override
        protected void initDefaultCommand() {}

        @Override
        public void initSendable(SendableBuilder builder) {
            super.initSendable(builder);
            builder.addDoubleProperty("P", null, value -> talon.config_kP(0, value));
            builder.addDoubleProperty("I", null, value -> talon.config_kI(0, value));
            builder.addDoubleProperty("D", null, value -> talon.config_kD(0, value));
            builder.addDoubleProperty("F", null, value -> talon.config_kF(0, value));
            builder.addDoubleProperty("IZONE", null, value -> talon.config_IntegralZone(0, (int) value));
        }
    }

    private final LinearActuator leftLinearActuator = new LinearActuator(
            "leftLinearActuator",
            Constants.CAN.CLIMB_LEGS_TALON_LEFT,
            Constants.ClimbLegs.INVERT_LEFT,
            Constants.ClimbLegs.SENSOR_PHASE_LEFT,
            Constants.ClimbLegs.P_LEFT,
            Constants.ClimbLegs.I_LEFT,
            Constants.ClimbLegs.D_LEFT,
            Constants.ClimbLegs.F_LEFT,
            Constants.ClimbLegs.IZONE_LEFT
    );

    private final LinearActuator rightLinearActuator = new LinearActuator(
            "rightLinearActuator",
            Constants.CAN.CLIMB_LEGS_TALON_RIGHT,
            Constants.ClimbLegs.INVERT_RIGHT,
            Constants.ClimbLegs.SENSOR_PHASE_RIGHT,
            Constants.ClimbLegs.P_RIGHT,
            Constants.ClimbLegs.I_RIGHT,
            Constants.ClimbLegs.D_RIGHT,
            Constants.ClimbLegs.F_RIGHT,
            Constants.ClimbLegs.IZONE_RIGHT
    );

    private ClimbLegs() {
        super("climbLegs");
    }

    private static final ClimbLegs INSTANCE = new ClimbLegs();

    public static ClimbLegs getInstance() {
        return INSTANCE;
    }

    @Override
    protected void initDefaultCommand() {}

    public LinearActuator getLeftLinearActuator() {
        return leftLinearActuator;
    }

    public LinearActuator getRightLinearActuator() {
        return rightLinearActuator;
    }

    // Merged operations on both LAs?

    private static double metresToTalonUnits(double value) {
        return Units.convertAndCheck(value, Units.METRE, DISTANCE_UNIT);
    }
}
