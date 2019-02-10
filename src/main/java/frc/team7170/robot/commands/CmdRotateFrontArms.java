package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.subsystems.FrontArms;

public class CmdRotateFrontArms extends Command {

    private static final FrontArms frontArms = FrontArms.getInstance();

    private final double angleDegrees;

    public CmdRotateFrontArms(double angleDegrees) {
        this.angleDegrees = angleDegrees;
        requires(frontArms);
    }

    @Override
    protected void initialize() {
        frontArms.setAngle(angleDegrees);
    }

    @Override
    protected void end() {
        frontArms.killMotors();
    }

    @Override
    protected boolean isFinished() {
        return frontArms.isErrorTolerable();
    }
}
