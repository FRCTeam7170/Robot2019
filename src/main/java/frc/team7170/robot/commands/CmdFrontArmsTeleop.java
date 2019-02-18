package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.robot.Constants;
import frc.team7170.robot.actions.ButtonActions;
import frc.team7170.robot.subsystems.FrontArms;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CmdFrontArmsTeleop extends Command {

    private static final Logger LOGGER = Logger.getLogger(CmdFrontArmsTeleop.class.getName());
    private static final FrontArms frontArms = FrontArms.getInstance();

    private boolean warned = false;

    @Override
    protected void execute() {
        try {
            if (KeyBindings.getInstance().actionToButton(ButtonActions.PICKUP_PREPARE).getPressed()) {
                new CmdRotateFrontArms(Constants.FrontArms.HORIZONTAL_ANGLE_DEGREES, true).start();
            } else if (KeyBindings.getInstance().actionToButton(ButtonActions.PICKUP).getPressed()) {
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
