package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.Button;
import frc.team7170.lib.oi.ButtonPollHelper;
import frc.team7170.robot2019.TeleopStateMachine;
import frc.team7170.robot2019.actions.ButtonActions;
import frc.team7170.robot2019.subsystems.EndEffector;

import java.util.logging.Logger;

public class CmdEndEffectorTeleop extends Command {

    private static final Logger LOGGER = Logger.getLogger(CmdEndEffectorTeleop.class.getName());
    private static final TeleopStateMachine tsm = TeleopStateMachine.getInstance();

    private static final Button eject = new ButtonPollHelper(ButtonActions.EJECT, LOGGER::warning);
    private static final Button togglePin = new ButtonPollHelper(ButtonActions.TOGGLE_PIN, LOGGER::warning);  // TODO: TEMP?

    public CmdEndEffectorTeleop() {
        requires(EndEffector.getInstance());
    }

    @Override
    protected void execute() {
        if (eject.getPressed()) {
            // tsm.ejectTrigger.execute();  // TODO :TEMP
            EndEffector.getInstance().eject();
        } else if (togglePin.getPressed()) {
            EndEffector.getInstance().togglePin();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
