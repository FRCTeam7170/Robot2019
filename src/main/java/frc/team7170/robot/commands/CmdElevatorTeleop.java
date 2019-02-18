package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.robot.Constants;
import frc.team7170.robot.actions.ButtonActions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CmdElevatorTeleop extends Command {

    private static final Logger LOGGER = Logger.getLogger(CmdElevatorTeleop.class.getName());

    private boolean warned = false;

    @Override
    protected void execute() {
        try {
            if (KeyBindings.getInstance().actionToButton(ButtonActions.ELEVATOR_LEVEL1).getPressed()) {
                new CmdMoveElevator(Constants.Elevator.LEVEL1_METRES, true).start();
            } else if (KeyBindings.getInstance().actionToButton(ButtonActions.ELEVATOR_LEVEL2).getPressed()) {
                new CmdMoveElevator(Constants.Elevator.LEVEL2_METRES, true).start();
            } else if (KeyBindings.getInstance().actionToButton(ButtonActions.ELEVATOR_LEVEL3).getPressed()) {
                new CmdMoveElevator(Constants.Elevator.LEVEL3_METRES, true).start();
            }
        } catch (NullPointerException e) {
            if (!warned) {
                LOGGER.log(Level.WARNING, "Unbound button for elevator movement requested");
                warned = true;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
