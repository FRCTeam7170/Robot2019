package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.robot.Constants;
import frc.team7170.robot.TeleopStateMachine;

public class CmdPickup extends CommandGroup {

    private static class CmdSignalPickupFinished extends Command {

        @Override
        protected void initialize() {
            TeleopStateMachine.getInstance().pickupFinishedTrigger.execute();
        }

        @Override
        protected boolean isFinished() {
            return true;
        }
    }

    public CmdPickup() {
        addSequential(new CmdRotateFrontArms(Constants.FrontArms.PICKUP_ANGLE_DEGREES, false));
        addSequential(new CmdRotateFrontArms(Constants.FrontArms.HOME_ANGLE_DEGREES, true));
        addParallel(new CmdSignalPickupFinished());
    }
}
