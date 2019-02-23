package frc.team7170.robot.subsystems;

import com.revrobotics.*;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team7170.robot.Constants;

public class  Elevator extends Subsystem {

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
        pidController.disable();  // TODO: is this necessary?
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

    public void setPercent(double percent) {
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

    public boolean isLowerLimitSwitchTriggered() {
        return lowerLimitSwitch.get();
    }

    public boolean isUpperLimitSwitchTriggered() {
        return upperLimitSwitch.get();
    }

    @Override
    protected void initDefaultCommand() {}
}
