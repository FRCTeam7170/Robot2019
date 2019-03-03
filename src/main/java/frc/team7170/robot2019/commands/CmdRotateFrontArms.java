package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.subsystems.FrontArms;

public class CmdRotateFrontArms extends Command {

    private static final FrontArms frontArms = FrontArms.getInstance();

    private final double angleDegrees;
    private final boolean hold;

    public CmdRotateFrontArms(double angleDegrees, boolean hold) {
        this.angleDegrees = angleDegrees;
        this.hold = hold;
        requires(frontArms);
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
