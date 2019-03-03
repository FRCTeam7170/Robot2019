package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.lib.command.CmdRunnable;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.TeleopStateMachine;
import frc.team7170.robot2019.actions.ButtonActions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CmdPickup extends CommandGroup {

    private static final Logger LOGGER = Logger.getLogger(CmdPickup.class.getName());
    private static final TeleopStateMachine tsm = TeleopStateMachine.getInstance();

    private boolean warned = false;
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
            try {
                if (KeyBindings.getInstance().actionToButton(ButtonActions.PICKUP_CANCEL).getPressed()) {
                    cancel();
                    tsm.pickupCancelTrigger.execute();
                }
            } catch (NullPointerException e) {
                if (!warned) {
                    LOGGER.log(Level.WARNING, "Unbound button for pickup command requested.", e);
                    warned = true;
                }
            }
        }
    }
}
