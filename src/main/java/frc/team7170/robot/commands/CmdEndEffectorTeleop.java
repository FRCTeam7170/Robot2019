package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.robot.TeleopStateMachine;
import frc.team7170.robot.actions.ButtonActions;
import frc.team7170.robot.subsystems.EndEffector;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CmdEndEffectorTeleop extends Command {

    private static final Logger LOGGER = Logger.getLogger(CmdEndEffectorTeleop.class.getName());
    private static final TeleopStateMachine tsm = TeleopStateMachine.getInstance();

    private boolean warned = false;

    public CmdEndEffectorTeleop() {
        requires(EndEffector.getInstance());
    }

    @Override
    protected void execute() {
        try {
            if (KeyBindings.getInstance().actionToButton(ButtonActions.EJECT).getPressed()) {
                tsm.ejectTrigger.execute();
            } else if (KeyBindings.getInstance().actionToButton(ButtonActions.TOGGLE_PIN).getPressed()) {
                // TODO: TEMP
                EndEffector.getInstance().togglePin();
            }
        } catch (NullPointerException e) {
            if (!warned) {
                LOGGER.log(Level.WARNING, "Unbound button for end effector requested.", e);
                warned = true;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
