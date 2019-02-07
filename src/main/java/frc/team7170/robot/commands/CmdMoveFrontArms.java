package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class CmdMoveFrontArms extends Command {

    private final double angleDegrees;

    public CmdMoveFrontArms(double angleDegrees) {
        this.angleDegrees = angleDegrees;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
