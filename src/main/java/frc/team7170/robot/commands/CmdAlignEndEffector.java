package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.subsystems.EndEffector;

public class CmdAlignEndEffector extends Command {

    // TODO

    public CmdAlignEndEffector() {
        requires(EndEffector.getInstance());
    }

    @Override
    protected void initialize() {
        System.out.println("DO SOME STUFF WITH LATERAL SLIDE AND SENSOR ARRAY TO ALIGN");
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
