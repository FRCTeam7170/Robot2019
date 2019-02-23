package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.subsystems.EndEffector;

public class CmdZeroEndEffector extends Command {

    private static final EndEffector endEffector = EndEffector.getInstance();

    public CmdZeroEndEffector() {
        requires(endEffector);
    }

    @Override
    protected void initialize() {
        // TODO: set servo to zeroing percent
    }

    @Override
    protected void end() {
        // TODO: record zero position and kill servo
    }

    @Override
    protected boolean isFinished() {
        // TODO: check limit switch
        return true;
    }
}
