package frc.team7170.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team7170.robot.Constants;

public class Elevator extends Subsystem {

    public static final Elevator INSTANCE = new Elevator();

    private final VictorSPX master;
    private final VictorSPX follower;

    private final Encoder encoder = new Encoder(
            Constants.DIO.ELEVATOR_ENCODER_A,
            Constants.DIO.ELEVATOR_ENCODER_B,
            Constants.Elevator.INVERT_ENCODER);

    private final DigitalInput lowLimitSwitch = new DigitalInput(Constants.DIO.ELEVATOR_LIMIT_SWITCH_LOW);
    private final DigitalInput highLimitSwitch = new DigitalInput(Constants.DIO.ELEVATOR_LIMIT_SWITCH_HIGH);

    private Elevator() {
        master = new VictorSPX(Constants.CAN.ELEVATOR_VICTOR_MASTER);
        follower = new VictorSPX(Constants.CAN.ELEVATOR_VICTOR_FOLLOWER);

        configVictor(master);
        configVictor(follower);

        follower.follow(master);

        master.setInverted(Constants.Elevator.INVERT_LEFT);
        follower.setInverted(Constants.Elevator.INVERT_RIGHT);
    }

    private void configVictor(VictorSPX victor) {
        victor.configClosedloopRamp(Constants.Elevator.RAMP_TIME);
        victor.setNeutralMode(Constants.Elevator.NEUTRAL_MODE);
        victor.enableVoltageCompensation(Constants.Elevator.ENABLE_VOLTAGE_COMPENSATION);
        victor.configVoltageCompSaturation(Constants.Elevator.VOLTAGE_COMPENSATION_SATURATION);
        victor.configAllowableClosedloopError(0, Constants.Elevator.ALLOWABLE_CLOSED_LOOP_ERROR);
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
