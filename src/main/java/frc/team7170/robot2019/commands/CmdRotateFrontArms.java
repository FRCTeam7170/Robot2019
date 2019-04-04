package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.subsystems.FrontArms;

public class CmdRotateFrontArms extends Command {

    private static final FrontArms frontArms = FrontArms.getInstance();

    private final double angleDegrees;
    private final boolean hold;

    // TODO: TEMP -- gross thingy
    public CmdRotateFrontArms(double angleDegrees, boolean hold, boolean withRequire) {
        this.angleDegrees = angleDegrees + Constants.FrontArms.ANGLE_UNCERTAINTY_DEGREES;
        this.hold = hold;
        if (withRequire) {
            requires(frontArms);
        }
    }

    public CmdRotateFrontArms(double angleDegrees, boolean hold) {
        this(angleDegrees, hold, true);
    }

    @Override
    protected void initialize() {
        frontArms.setAngle(angleDegrees);
    }

    @Override
    protected void end() {
        if (!hold) {
            frontArms.killMotors();
        }
    }

    @Override
    protected boolean isFinished() {
        return frontArms.isErrorTolerable();
    }
}
