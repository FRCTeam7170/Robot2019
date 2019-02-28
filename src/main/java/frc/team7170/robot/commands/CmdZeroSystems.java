package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.robot.subsystems.ClimbLegs;

public class CmdZeroSystems extends CommandGroup {

    public CmdZeroSystems() {
        addSequential(new CmdZeroLinearActuator(ClimbLegs.getInstance().getLeftLinearActuator()));
        addParallel(new CmdZeroLinearActuator(ClimbLegs.getInstance().getRightLinearActuator()));
        addParallel(new CmdZeroFrontArms());
        addParallel(new CmdZeroElevator());
        addParallel(new CmdZeroLateralSlide());
    }
}
