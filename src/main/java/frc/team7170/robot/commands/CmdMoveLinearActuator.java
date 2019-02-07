package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.subsystems.ClimbLegs;

public class CmdMoveLinearActuator extends Command {

    private final ClimbLegs.LinearActuator linearActuator;

    public CmdMoveLinearActuator(ClimbLegs.LinearActuator linearActuator, ) {
        this.linearActuator = linearActuator;
    }

    @Override
    protected void initialize() {
        linearActuator.setSetpoint();
    }

    @Override
    protected void execute() {
        super.execute();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
