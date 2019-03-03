package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.robot2019.subsystems.ClimbLegs;

public class CmdDeployAppendages extends CommandGroup {

    public CmdDeployAppendages(double armDegrees, double laHeight, boolean hold) {
        addSequential(new CmdRotateFrontArms(armDegrees, hold));
        addParallel(new CmdExtendLinearActuator(ClimbLegs.getInstance().getLeftLinearActuator(), laHeight, hold));
        addParallel(new CmdExtendLinearActuator(ClimbLegs.getInstance().getRightLinearActuator(), laHeight, hold));
    }
}
