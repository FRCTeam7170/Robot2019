package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CmdDeployAppendages extends CommandGroup {

    // TODO: TEMP -- gross thingy
    public CmdDeployAppendages(double armDegrees, double laHeight, boolean hold, boolean withRequire) {
        addParallel(new CmdRotateFrontArms(armDegrees, hold, withRequire));
//        addParallel(new CmdExtendLinearActuator(ClimbLegs.getInstance().getLeftLinearActuator(), laHeight, hold));
//        addParallel(new CmdExtendLinearActuator(ClimbLegs.getInstance().getRightLinearActuator(), laHeight, hold));
        addParallel(new CmdSynchronousExtendLinearActuators(laHeight, withRequire));
    }

    public CmdDeployAppendages(double armDegrees, double laHeight, boolean hold) {
        this(armDegrees, laHeight, hold, true);
    }
}
