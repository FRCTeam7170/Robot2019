package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.subsystems.ClimbLegs;
import frc.team7170.robot.subsystems.FrontArms;

public class CmdClimbRaise extends Command {

    private final double targetHeightInches;
    private final double contactAngleDegrees;

    public CmdClimbRaise(double targetHeightInches, double contactAngleDegrees) {
        this.targetHeightInches = targetHeightInches;
        this.contactAngleDegrees = contactAngleDegrees;

        requires(ClimbLegs.getInstance());  // TODO: is this necessary?
        requires(ClimbLegs.getInstance().getLeftLinearActuator());
        requires(ClimbLegs.getInstance().getRightLinearActuator());
        requires(FrontArms.getInstance());
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void execute() {
        super.execute();
    }

    @Override
    protected void end() {
        super.end();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
