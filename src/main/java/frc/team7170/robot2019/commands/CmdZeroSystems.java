package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.robot2019.subsystems.ClimbLegs;

public class CmdZeroSystems extends CommandGroup {

    public CmdZeroSystems() {
        addParallel(new CmdZeroLinearActuator(ClimbLegs.getInstance().getLeftLinearActuator()));
        addParallel(new CmdZeroLinearActuator(ClimbLegs.getInstance().getRightLinearActuator()));
        addParallel(new CmdZeroFrontArms());
        addParallel(new CmdZeroElevator());
        // addParallel(new CmdZeroLateralSlide());
    }
}
