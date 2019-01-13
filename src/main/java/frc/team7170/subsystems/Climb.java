package frc.team7170.subsystems;


import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team7170.robot.Constants;

public class Climb {

    private static final Climb INSTANCE = new Climb();

    private final TalonSRX mBackTalonMaster = new TalonSRX(Constants.CAN.BACK_CLIMB_TALON_1);
    private final TalonSRX mBackTalonFollower = new TalonSRX(Constants.CAN.BACK_CLIMB_TALON_2);
    private final TalonSRX mFrontTalonMaster = new TalonSRX(Constants.CAN.FRONT_CLIMB_TALON_1);
    private final TalonSRX mFrontTalonFollower = new TalonSRX(Constants.CAN.FRONT_CLIMB_TALON_2);

    private Climb() {
        mBackTalonFollower.follow(mBackTalonMaster);
        mFrontTalonFollower.follow(mFrontTalonMaster);
    }

    public static Climb getInstance() {
        return INSTANCE;
    }
}
