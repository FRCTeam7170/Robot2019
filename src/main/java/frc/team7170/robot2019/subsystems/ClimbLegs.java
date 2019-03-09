package frc.team7170.robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.team7170.lib.Named;
import frc.team7170.lib.CalcUtil;
import frc.team7170.robot2019.Constants;

import java.util.logging.Logger;

public class ClimbLegs extends Subsystem implements Named {

    private static final Logger LOGGER = Logger.getLogger(ClimbLegs.class.getName());

    // private static final Unit<UniversalUnitType> DISTANCE_UNIT = Constants.TALON_CIMCODER_ROTATION_UNIT
    //         .multiply(Units.METRE).multiply(Constants.ClimbLegs.DISTANCE_FACTOR);

    public static class LinearActuator extends Subsystem implements Named {

        private final TalonSRX talon;

        private final NetworkTableEntry lowerLimitSwitchEntry;
        private final NetworkTableEntry upperLimitSwitchEntry;
        private final NetworkTableEntry encoderEntry;

        private LinearActuator(String name, int id, boolean invert, boolean sensorPhase,
                               double P, double I, double D, double F, int IZONE) {
            super(name);
            talon = new TalonSRX(id);

            talon.config_kP(0, P);
            talon.config_kI(0, I);
            talon.config_kD(0, D);
            talon.config_kF(0, F);
            talon.config_IntegralZone(0, IZONE);

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

            ShuffleboardTab linearActuatorTab = Shuffleboard.getTab(name);

            linearActuatorTab.add("P", P).getEntry().addListener(
                    notification -> {talon.config_kP(0, notification.value.getDouble());
                        System.out.println("SET P");},
                    EntryListenerFlags.kUpdate
            );
            linearActuatorTab.add("I", I).getEntry().addListener(
                    notification -> {talon.config_kI(0, notification.value.getDouble());
                        System.out.println("SET I");},
                    EntryListenerFlags.kUpdate
            );
            linearActuatorTab.add("D", D).getEntry().addListener(
                    notification -> {talon.config_kD(0, notification.value.getDouble());
                        System.out.println("SET D");},
                    EntryListenerFlags.kUpdate
            );
            linearActuatorTab.add("F", F).getEntry().addListener(
                    notification -> {talon.config_kF(0, notification.value.getDouble());
                        System.out.println("SET F");},
                    EntryListenerFlags.kUpdate
            );
            linearActuatorTab.add("IZONE", IZONE).getEntry().addListener(
                    notification -> {talon.config_IntegralZone(0, (int) notification.value.getDouble());
                        System.out.println("SET IZONE");},
                    EntryListenerFlags.kUpdate
            );
            lowerLimitSwitchEntry = linearActuatorTab.add("lowerLimitSwitch", isLowerLimitSwitchTriggered()).getEntry();
            upperLimitSwitchEntry = linearActuatorTab.add("upperLimitSwitch", isUpperLimitSwitchTriggered()).getEntry();
            encoderEntry = linearActuatorTab.add("encoder", getEncoder()).getEntry();
        }

        @Override
        public void periodic() {
            lowerLimitSwitchEntry.setBoolean(isLowerLimitSwitchTriggered());
            upperLimitSwitchEntry.setBoolean(isUpperLimitSwitchTriggered());
            encoderEntry.setDouble(getEncoder());
        }

        public void setPercent(double percent) {
            talon.set(ControlMode.PercentOutput, percent);
        }

        public void setPosition(double metres) {
            talon.set(ControlMode.Position, metresToTalonUnits(-metres));
        }

        public boolean isErrorTolerable() {
            return CalcUtil.inThreshold(talon.getClosedLoopError(), 0,
                    Constants.ClimbLegs.ALLOWABLE_CLOSED_LOOP_ERROR);
        }

        public boolean isLowerLimitSwitchTriggered() {
            return !talon.getSensorCollection().isFwdLimitSwitchClosed();
        }

        public boolean isUpperLimitSwitchTriggered() {
            return !talon.getSensorCollection().isRevLimitSwitchClosed();
        }

        public void zeroEncoder() {
            talon.setSelectedSensorPosition(0);
        }

        public void killMotor() {
            setPercent(0.0);
        }

        public double getEncoder() {
            return talon.getSelectedSensorPosition();
        }

        @Override
        protected void initDefaultCommand() {}
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

    public static double metresToTalonUnits(double value) {
        // return Units.convertAndCheck(value, Units.METRE, DISTANCE_UNIT);
        return value / Constants.ClimbLegs.DISTANCE_FACTOR * (Constants.ClimbLegs.ENCODER_CPR * 4);
    }
}
