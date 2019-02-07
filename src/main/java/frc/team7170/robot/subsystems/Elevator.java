package frc.team7170.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team7170.robot.Constants;

public class Elevator extends Subsystem {

    private final CANSparkMax master;
    private final CANSparkMax follower;

    private final Encoder encoder = new Encoder(
            Constants.DIO.ELEVATOR_ENCODER_A,
            Constants.DIO.ELEVATOR_ENCODER_B,
            Constants.Elevator.INVERT_ENCODER
    );

    private final DigitalInput lowLimitSwitch = new DigitalInput(Constants.DIO.ELEVATOR_LIMIT_SWITCH_LOW);
    private final DigitalInput highLimitSwitch = new DigitalInput(Constants.DIO.ELEVATOR_LIMIT_SWITCH_HIGH);

    private Elevator() {
        master = new CANSparkMax(Constants.CAN.ELEVATOR_SPARK_MAX_MASTER, CANSparkMaxLowLevel.MotorType.kBrushed);
        follower = new CANSparkMax(Constants.CAN.ELEVATOR_SPARK_MAX_FOLLOWER, CANSparkMaxLowLevel.MotorType.kBrushed);

        configSparkMax(master);
        master.setInverted(Constants.Elevator.INVERT_LEFT);

        configSparkMax(follower);
        follower.setInverted(Constants.Elevator.INVERT_RIGHT);
        follower.follow(master);
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
    protected void initDefaultCommand() {}

    public int getEncoder() {
        return encoder.get();
    }

    public double getDistance() {
        return encoder.getDistance();
    }

    public double getVelocity() {
        return encoder.getRate();
    }

    public boolean getLowLimitSwitch() {
        return lowLimitSwitch.get();
    }

    public boolean getHighLimitSwitch() {
        return highLimitSwitch.get();
    }
}
