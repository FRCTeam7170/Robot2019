package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.robot.TeleopStateMachine;
import frc.team7170.robot.actions.ButtonActions;
import frc.team7170.robot.subsystems.FrontArms;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CmdFrontArmsTeleop extends Command {

    private static final Logger LOGGER = Logger.getLogger(CmdFrontArmsTeleop.class.getName());
    private static final TeleopStateMachine tsm = TeleopStateMachine.getInstance();

    private boolean warned = false;

    public CmdFrontArmsTeleop() {
        requires(FrontArms.getInstance());
    }

    @Override
    protected void execute() {
        try {
            // TODO: Make pickup prepare and pickup the same button
            if (KeyBindings.getInstance().actionToButton(ButtonActions.PICKUP_PREPARE).getPressed()) {
                tsm.pickupPrepareTrigger.execute();
            } else if (KeyBindings.getInstance().actionToButton(ButtonActions.PICKUP).getPressed()) {
                tsm.pickupTrigger.execute();
            } else if (KeyBindings.getInstance().actionToButton(ButtonActions.PICKUP_CANCEL).getPressed()) {
                tsm.pickupCancelTrigger.execute();
            }
        } catch (NullPointerException e) {
            if (!warned) {
                LOGGER.log(Level.WARNING, "Unbound button for front arms requested.", e);
                warned = true;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
