package frc.team7170.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team7170.lib.unit.Unit;
import frc.team7170.lib.unit.Units;
import frc.team7170.lib.CalcUtil;
import frc.team7170.lib.unit.UniversalUnitType;
import frc.team7170.robot.Constants;

import java.util.logging.Logger;

public class FrontArms extends Subsystem {

    private static final Logger LOGGER = Logger.getLogger(FrontArms.class.getName());

    private static final Unit<UniversalUnitType> ROTATION_UNIT = Constants.TALON_CIMCODER_ROTATION_UNIT
            .divide(Constants.FrontArms.TOTAL_REDUCTION);

    private final TalonSRX master = new TalonSRX(Constants.CAN.FRONT_ARM_TALON_MASTER);
    private final TalonSRX follower = new TalonSRX(Constants.CAN.FRONT_ARM_TALON_FOLLOWER);

    private FrontArms() {
        super("frontArms");

        master.config_kP(0, Constants.FrontArms.P);
        master.config_kI(0, Constants.FrontArms.I);
        master.config_kD(0, Constants.FrontArms.D);
        master.config_kF(0, Constants.FrontArms.F);
        master.config_IntegralZone(0, Constants.FrontArms.IZONE);

        master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        master.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        master.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        master.configClosedloopRamp(Constants.FrontArms.RAMP_TIME);
        master.setNeutralMode(Constants.FrontArms.NEUTRAL_MODE);
        master.enableVoltageCompensation(Constants.FrontArms.ENABLE_VOLTAGE_COMPENSATION);
        master.configVoltageCompSaturation(Constants.FrontArms.VOLTAGE_COMPENSATION_SATURATION);
        master.enableCurrentLimit(Constants.FrontArms.ENABLE_CURRENT_LIMIT);
        master.configContinuousCurrentLimit(Constants.FrontArms.CONTINUOUS_CURRENT_LIMIT_AMPS);
        master.configPeakCurrentLimit(Constants.FrontArms.PEAK_CURRENT_LIMIT_AMPS);
        master.configPeakCurrentDuration(Constants.FrontArms.PEAK_CURRENT_LIMIT_DURATION_MS);
        master.configAllowableClosedloopError(0, Constants.FrontArms.ALLOWABLE_CLOSED_LOOP_ERROR);
        master.setSensorPhase(Constants.FrontArms.SENSOR_PHASE);

        master.setInverted(Constants.FrontArms.INVERT_LEFT);
        follower.setInverted(Constants.FrontArms.INVERT_RIGHT);

        follower.follow(master);
    }

    private static final FrontArms INSTANCE = new FrontArms();

    public static FrontArms getInstance() {
        return INSTANCE;
    }

    public void setPercent(double percent) {
        master.set(ControlMode.PercentOutput, percent);
    }

    public void setAngle(double degrees) {
        master.set(ControlMode.Position, degreesToTalonUnits(degrees));
    }

    public void killMotors() {
        setPercent(0.0);
    }

    public void zeroEncoder() {
        master.setSelectedSensorPosition(0);
    }

    public boolean isForwardLimitSwitchTriggered() {
        return master.getSensorCollection().isFwdLimitSwitchClosed();
    }

    public boolean isReverseLimitSwitchTriggered() {
        return master.getSensorCollection().isRevLimitSwitchClosed();
    }

    public boolean isErrorTolerable() {
        return CalcUtil.inThreshold(master.getClosedLoopError(), 0,
                Constants.FrontArms.ALLOWABLE_CLOSED_LOOP_ERROR);
    }

    @Override
    protected void initDefaultCommand() {}

    private static double degreesToTalonUnits(double value) {
        return Units.convertAndCheck(value, Units.DEGREES, ROTATION_UNIT);
    }
}
