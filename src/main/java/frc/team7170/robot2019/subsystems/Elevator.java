package frc.team7170.robot2019.subsystems;

import com.revrobotics.*;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.team7170.lib.Named;
import frc.team7170.robot2019.Constants;

// TODO: make this a PIDSubsystem?
public class  Elevator extends Subsystem implements Named {

    private final CANSparkMax master = new CANSparkMax(Constants.CAN.ELEVATOR_SPARK_MAX_MASTER,
            CANSparkMaxLowLevel.MotorType.kBrushed);
    private final CANSparkMax follower = new CANSparkMax(Constants.CAN.ELEVATOR_SPARK_MAX_FOLLOWER,
            CANSparkMaxLowLevel.MotorType.kBrushed);

    private final CANDigitalInput lowerLimitSwitch = master.getReverseLimitSwitch(
            CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);
    private final CANDigitalInput upperLimitSwitch = master.getForwardLimitSwitch(
            CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);

    private final Encoder encoder = new Encoder(
            Constants.DIO.ELEVATOR_ENCODER_A,
            Constants.DIO.ELEVATOR_ENCODER_B,
            Constants.Elevator.INVERT_ENCODER
    );

    private final PIDController pidController = new PIDController(
            Constants.Elevator.P,
            Constants.Elevator.I,
            Constants.Elevator.D,
            Constants.Elevator.F,
            encoder,
            master
    );

    // private final DigitalInput lowerLimitSwitch = new DigitalInput(Constants.DIO.ELEVATOR_LIMIT_SWITCH_LOW);
    // private final DigitalInput higherLimitSwitch = new DigitalInput(Constants.DIO.ELEVATOR_LIMIT_SWITCH_HIGH);

    private final NetworkTableEntry lowerLimitSwitchEntry;
    private final NetworkTableEntry upperLimitSwitchEntry;
    private final NetworkTableEntry encoderEntry;

    private Elevator() {
        super("elevator");

        configSparkMax(master);
        master.setInverted(Constants.Elevator.INVERT_LEFT);

        configSparkMax(follower);
        follower.setInverted(Constants.Elevator.INVERT_RIGHT);
        follower.follow(master);

        lowerLimitSwitch.enableLimitSwitch(true);
        upperLimitSwitch.enableLimitSwitch(true);

        encoder.setDistancePerPulse(Constants.Elevator.DISTANCE_FACTOR);
        pidController.disable();

        ShuffleboardTab elevatorTab = Shuffleboard.getTab("elevator");

        elevatorTab.add("P", Constants.Elevator.P).getEntry().addListener(
                notification -> pidController.setP(notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        elevatorTab.add("I", Constants.Elevator.I).getEntry().addListener(
                notification -> pidController.setI(notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        elevatorTab.add("D", Constants.Elevator.D).getEntry().addListener(
                notification -> pidController.setD(notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        elevatorTab.add("F", Constants.Elevator.F).getEntry().addListener(
                notification -> pidController.setF(notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        lowerLimitSwitchEntry = elevatorTab.add("lowerLimitSwitch", isLowerLimitSwitchTriggered()).getEntry();
        upperLimitSwitchEntry = elevatorTab.add("upperLimitSwitch", isUpperLimitSwitchTriggered()).getEntry();
        encoderEntry = elevatorTab.add("encoder", getEncoder()).getEntry();
    }

    private static final Elevator INSTANCE = new Elevator();

    public static Elevator getInstance() {
        return INSTANCE;
    }

    private static void configSparkMax(CANSparkMax max) {
        max.setRampRate(Constants.Elevator.RAMP_TIME);
        max.setIdleMode(Constants.Elevator.IDLE_MODE);
        max.setSecondaryCurrentLimit(Constants.Elevator.CURRENT_LIMIT_AMPS);
    }

    @Override
    public void periodic() {
        lowerLimitSwitchEntry.setBoolean(isLowerLimitSwitchTriggered());
        upperLimitSwitchEntry.setBoolean(isUpperLimitSwitchTriggered());
        encoderEntry.setDouble(getEncoder());
    }

    public void setPercent(double percent) {
        pidController.disable();
        master.set(percent);
    }

    public void setPosition(double metres) {
        pidController.setSetpoint(metres);
        pidController.enable();
    }

    public boolean isErrorTolerable() {
        return pidController.onTarget();
    }

    public void zeroEncoder() {
        encoder.reset();
    }

    public void killMotor() {
        pidController.disable();
        setPercent(0.0);
    }

    public double getEncoder() {
        return encoder.getDistance();
    }

    public boolean isLowerLimitSwitchTriggered() {
        return lowerLimitSwitch.get();
    }

    public boolean isUpperLimitSwitchTriggered() {
        return upperLimitSwitch.get();
    }

    @Override
    protected void initDefaultCommand() {}
}
