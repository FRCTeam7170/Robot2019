package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.Button;
import frc.team7170.lib.oi.ButtonPollHelper;
import frc.team7170.robot2019.TeleopStateMachine;
import frc.team7170.robot2019.actions.ButtonActions;
import frc.team7170.robot2019.subsystems.FrontArms;

import java.util.logging.Logger;

public class CmdFrontArmsTeleop extends Command {

    private static final Logger LOGGER = Logger.getLogger(CmdFrontArmsTeleop.class.getName());
    private static final TeleopStateMachine tsm = TeleopStateMachine.getInstance();

    private static final Button pickup = new ButtonPollHelper(ButtonActions.PICKUP, LOGGER::warning);

    public CmdFrontArmsTeleop() {
        requires(FrontArms.getInstance());
    }

    @Override
    protected void execute() {
        if (pickup.getPressed()) {
            tsm.pickupTrigger.execute();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
