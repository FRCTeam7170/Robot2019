package frc.team7170.robot2019.subsystems;

import com.revrobotics.*;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.team7170.lib.Named;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.commands.CmdElevatorTeleop;

// TODO: make this a PIDSubsystem?
public class  Elevator extends Subsystem implements Named {

    //private final CANSparkMax master = new CANSparkMax(Constants.CAN.ELEVATOR_SPARK_MAX_MASTER,
    //        CANSparkMaxLowLevel.MotorType.kBrushed);
    //private final CANSparkMax follower = new CANSparkMax(Constants.CAN.ELEVATOR_SPARK_MAX_FOLLOWER,
    //        CANSparkMaxLowLevel.MotorType.kBrushed);
    private final Spark master = new Spark(0);
    private final Spark follower = new Spark(1);
    private final SpeedControllerGroup mcuGroup = new SpeedControllerGroup(master, follower);
    // TODO: TEMP -- limit switches commented out
//    private final CANDigitalInput lowerLimitSwitch = master.getReverseLimitSwitch(
//            CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);
//    private final CANDigitalInput upperLimitSwitch = master.getForwardLimitSwitch(
//            CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);

    private final Encoder encoder = new Encoder(
            Constants.DIO.ELEVATOR_ENCODER_A,
            Constants.DIO.ELEVATOR_ENCODER_B,
            Constants.Elevator.INVERT_ENCODER
    );

    private final PIDController pidControllerUp = new PIDController(
            Constants.Elevator.P_UP,
            Constants.Elevator.I_UP,
            Constants.Elevator.D_UP,
            Constants.Elevator.F_UP,
            encoder,
            mcuGroup
    );

    private final PIDController pidControllerDown = new PIDController(
            Constants.Elevator.P_DOWN,
            Constants.Elevator.I_DOWN,
            Constants.Elevator.D_DOWN,
            Constants.Elevator.F_DOWN,
            encoder,
            mcuGroup
    );

    // private final DigitalInput lowerLimitSwitch = new DigitalInput(Constants.DIO.ELEVATOR_LIMIT_SWITCH_LOW);
    // private final DigitalInput higherLimitSwitch = new DigitalInput(Constants.DIO.ELEVATOR_LIMIT_SWITCH_HIGH);

//    private final NetworkTableEntry lowerLimitSwitchEntry;
//    private final NetworkTableEntry upperLimitSwitchEntry;
    private final NetworkTableEntry encoderEntry;

    private Elevator() {
        super("elevator");

        //configSparkMax(master);
        //master.setInverted(Constants.Elevator.INVERT_LEFT);

        //configSparkMax(follower);
        //follower.setInverted(Constants.Elevator.INVERT_RIGHT);
        //follower.follow(master);

        master.setInverted(Constants.Elevator.INVERT_RIGHT);
        follower.setInverted(Constants.Elevator.INVERT_LEFT);

//        lowerLimitSwitch.enableLimitSwitch(true);
//        upperLimitSwitch.enableLimitSwitch(true);

        encoder.setDistancePerPulse(Constants.Elevator.DISTANCE_FACTOR);
        pidControllerUp.disable();
        pidControllerDown.disable();
        pidControllerUp.setAbsoluteTolerance(Constants.Elevator.TOLERANCE_METRES);
        pidControllerDown.setAbsoluteTolerance(Constants.Elevator.TOLERANCE_METRES);

        ShuffleboardTab elevatorTab = Shuffleboard.getTab("elevator");

        elevatorTab.add("P_UP", Constants.Elevator.P_UP).getEntry().addListener(
                notification -> pidControllerUp.setP(notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        elevatorTab.add("I_UP", Constants.Elevator.I_UP).getEntry().addListener(
                notification -> pidControllerUp.setI(notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        elevatorTab.add("D_UP", Constants.Elevator.D_UP).getEntry().addListener(
                notification -> pidControllerUp.setD(notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        elevatorTab.add("F_UP", Constants.Elevator.F_UP).getEntry().addListener(
                notification -> pidControllerUp.setF(notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );

        elevatorTab.add("P_DOWN", Constants.Elevator.P_DOWN).getEntry().addListener(
                notification -> pidControllerDown.setP(notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        elevatorTab.add("I_DOWN", Constants.Elevator.I_DOWN).getEntry().addListener(
                notification -> pidControllerDown.setI(notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        elevatorTab.add("D_DOWN", Constants.Elevator.D_DOWN).getEntry().addListener(
                notification -> pidControllerDown.setD(notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );
        elevatorTab.add("F_DOWN", Constants.Elevator.F_DOWN).getEntry().addListener(
                notification -> pidControllerDown.setF(notification.value.getDouble()),
                EntryListenerFlags.kUpdate
        );

//        lowerLimitSwitchEntry = elevatorTab.add("lowerLimitSwitch", isLowerLimitSwitchTriggered()).getEntry();
//        upperLimitSwitchEntry = elevatorTab.add("upperLimitSwitch", isUpperLimitSwitchTriggered()).getEntry();
        encoderEntry = elevatorTab.add("encoder", getEncoder()).getEntry();
    }

    private static final Elevator INSTANCE = new Elevator();

    public static Elevator getInstance() {
        return INSTANCE;
    }

    private static void configSparkMax(CANSparkMax max) {
        max.setOpenLoopRampRate(Constants.Elevator.RAMP_TIME);
        max.setIdleMode(Constants.Elevator.IDLE_MODE);
        max.setSecondaryCurrentLimit(Constants.Elevator.CURRENT_LIMIT_AMPS);
    }

    @Override
    public void periodic() {
        // lowerLimitSwitchEntry.setBoolean(isLowerLimitSwitchTriggered());
        // upperLimitSwitchEntry.setBoolean(isUpperLimitSwitchTriggered());
        encoderEntry.setDouble(getEncoder());
    }

    public void setPercent(double percent) {
        pidControllerUp.disable();
        pidControllerDown.disable();
        mcuGroup.set(percent);
    }

    public void setPosition(double metres) {
        if ((metres - getEncoder()) < 0) {
            pidControllerUp.disable();
            pidControllerDown.setSetpoint(metres);
            pidControllerDown.enable();
        } else {
            pidControllerDown.disable();
            pidControllerUp.setSetpoint(metres);
            pidControllerUp.enable();
        }
    }

    public boolean isErrorTolerable() {
        if (pidControllerDown.isEnabled()) {
            return pidControllerDown.onTarget();
        }
        return pidControllerUp.onTarget();
    }

    public void zeroEncoder() {
        encoder.reset();
    }

    public void killMotor() {
        setPercent(0.0);
    }

    public double getEncoder() {
        return encoder.getDistance();
    }

//    public boolean isLowerLimitSwitchTriggered() {
//        return lowerLimitSwitch.get();
//    }
//
//    public boolean isUpperLimitSwitchTriggered() {
//        return upperLimitSwitch.get();
//    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new CmdElevatorTeleop());
    }
}
