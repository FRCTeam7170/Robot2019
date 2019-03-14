package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.lib.command.CmdRunnable;
import frc.team7170.lib.oi.Button;
import frc.team7170.lib.oi.ButtonPollHelper;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.TeleopStateMachine;
import frc.team7170.robot2019.actions.ButtonActions;

import java.util.logging.Logger;

public class CmdPickup extends CommandGroup {

    private static final Logger LOGGER = Logger.getLogger(CmdPickup.class.getName());
    private static final TeleopStateMachine tsm = TeleopStateMachine.getInstance();

    private static final Button pickupCancel = new ButtonPollHelper(ButtonActions.PICKUP_CANCEL, LOGGER::warning);

    private boolean pollCancel = true;

    public CmdPickup() {
        addSequential(new CmdRotateFrontArms(Constants.FrontArms.PICKUP_ANGLE_DEGREES, false));
        addSequential(new CmdRotateFrontArms(Constants.FrontArms.HOME_ANGLE_DEGREES, true));
        addParallel(new CmdRunnable(() -> {
            pollCancel = false;
            tsm.pickupFinishedTrigger.execute();
        }));
    }

    @Override
    protected void execute() {
        if (pollCancel) {
            if (pickupCancel.getPressed()) {
                cancel();
                tsm.pickupCancelTrigger.execute();
            }
        }
    }
}
