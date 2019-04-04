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
import frc.team7170.lib.math.CalcUtil;
import frc.team7170.robot2019.Constants;

import java.util.logging.Logger;

public class FrontArms extends Subsystem {

    private static final Logger LOGGER = Logger.getLogger(FrontArms.class.getName());

    // private static final Unit<UniversalUnitType> ROTATION_UNIT = Constants.TALON_CIMCODER_ROTATION_UNIT
    //         .divide(Constants.FrontArms.TOTAL_REDUCTION);

    private final TalonSRX master = new TalonSRX(Constants.CAN.FRONT_ARM_TALON_MASTER);
    private final TalonSRX follower = new TalonSRX(Constants.CAN.FRONT_ARM_TALON_FOLLOWER);

    private final NetworkTableEntry limitSwitchEntry;
    private final NetworkTableEntry encoderEntry;
    private final NetworkTableEntry totalCurrentDrawEntry;
    private final NetworkTableEntry motorPowerPct;

    private double setpoint;

    private FrontArms() {
        super("frontArms");

        master.config_kP(0, Constants.FrontArms.P);
        master.config_kI(0, Constants.FrontArms.I);
        master.config_kD(0, Constants.FrontArms.D);
        master.config_kF(0, Constants.FrontArms.F);
        master.config_IntegralZone(0, Constants.FrontArms.IZONE);

        master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        master.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);
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
        master.configPeakOutputForward(Constants.FrontArms.MAX_ABSOLUTE_OUTPUT);
        master.configPeakOutputReverse(-Constants.FrontArms.MAX_ABSOLUTE_OUTPUT);

        master.setInverted(Constants.FrontArms.INVERT_LEFT);
        follower.setInverted(Constants.FrontArms.INVERT_RIGHT);

        follower.follow(master);

        ShuffleboardTab frontArmsTab = Shuffleboard.getTab("frontArms");

        frontArmsTab.add("P", Constants.FrontArms.P).getEntry().addListener(
                notification -> master.config_kP(0, notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        frontArmsTab.add("I", Constants.FrontArms.I).getEntry().addListener(
                notification -> master.config_kI(0, notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        frontArmsTab.add("D", Constants.FrontArms.D).getEntry().addListener(
                notification -> master.config_kD(0, notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        frontArmsTab.add("F", Constants.FrontArms.F).getEntry().addListener(
                notification -> master.config_kF(0, notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        frontArmsTab.add("IZONE", Constants.FrontArms.IZONE).getEntry().addListener(
                notification -> master.config_IntegralZone(0, (int) notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        limitSwitchEntry = frontArmsTab.add("limitSwitch", isReverseLimitSwitchTriggered()).getEntry();
        encoderEntry = frontArmsTab.add("encoder", getEncoder()).getEntry();
        totalCurrentDrawEntry = frontArmsTab.add("totalCurrentDraw", 0.0).getEntry();
        motorPowerPct = frontArmsTab.add("motorPowerPct", 0.0).getEntry();
    }

    private static final FrontArms INSTANCE = new FrontArms();

    public static FrontArms getInstance() {
        return INSTANCE;
    }

    @Override
    public void periodic() {
        limitSwitchEntry.setBoolean(isReverseLimitSwitchTriggered());
        encoderEntry.setDouble(getEncoder());
        totalCurrentDrawEntry.setDouble(master.getOutputCurrent() + follower.getOutputCurrent());
        motorPowerPct.setDouble(master.getMotorOutputPercent());
    }

    public void setPercent(double percent) {
        master.set(ControlMode.PercentOutput, percent);
    }

    public void setAngle(double degrees) {
        setpoint = degreesToTalonUnits(degrees);
        master.set(ControlMode.Position, setpoint);
    }

    public void killMotors() {
        setPercent(0.0);
    }

    public void zeroEncoder() {
        master.setSelectedSensorPosition(0);
    }

    public double getEncoder() {
        return master.getSelectedSensorPosition();
    }

    public double getAngle() {
        return talonUnitsToDegrees(getEncoder());
    }

    public boolean isReverseLimitSwitchTriggered() {
        return !master.getSensorCollection().isRevLimitSwitchClosed();
    }

    public boolean isErrorTolerable() {
        // TODO: why doesn't error from Talon API work? not updated quick enough after set? (probably)
        // int error = master.getClosedLoopError();
        double error = getEncoder() - setpoint;
        return CalcUtil.inThreshold(error, 0,
                Constants.FrontArms.ALLOWABLE_CLOSED_LOOP_ERROR);
    }

    @Override
    protected void initDefaultCommand() {
        // setDefaultCommand(new CmdFrontArmsTeleop());
    }

    private static double degreesToTalonUnits(double value) {
        // return Units.convertAndCheck(value, Units.DEGREES, ROTATION_UNIT);
        return value / 360.0 * Constants.FrontArms.TOTAL_REDUCTION * (Constants.FrontArms.ENCODER_CPR * 4);
    }

    private static double talonUnitsToDegrees(double value) {
        return value * 360.0 / Constants.FrontArms.TOTAL_REDUCTION / (Constants.FrontArms.ENCODER_CPR * 4);
    }
}
