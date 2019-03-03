package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.robot2019.TeleopStateMachine;
import frc.team7170.robot2019.actions.ButtonActions;
import frc.team7170.robot2019.subsystems.FrontArms;

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
            if (KeyBindings.getInstance().actionToButton(ButtonActions.PICKUP).getPressed()) {
                tsm.pickupTrigger.execute();
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