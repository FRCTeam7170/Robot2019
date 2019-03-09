package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.robot2019.ElevatorLevel;
import frc.team7170.robot2019.TeleopStateMachine;
import frc.team7170.robot2019.actions.ButtonActions;
import frc.team7170.robot2019.subsystems.Elevator;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CmdElevatorTeleop extends Command {

    private static final Logger LOGGER = Logger.getLogger(CmdElevatorTeleop.class.getName());
    private static final TeleopStateMachine tsm = TeleopStateMachine.getInstance();

    private boolean warned = false;

    public CmdElevatorTeleop() {
        requires(Elevator.getInstance());
    }

    @Override
    protected void execute() {
        try {
            if (KeyBindings.getInstance().actionToButton(ButtonActions.ELEVATOR_LEVEL1).getPressed()) {
                tsm.elevateTrigger.execute(ElevatorLevel.LEVEL1);
            } else if (KeyBindings.getInstance().actionToButton(ButtonActions.ELEVATOR_LEVEL2).getPressed()) {
                tsm.elevateTrigger.execute(ElevatorLevel.LEVEL2);
            } else if (KeyBindings.getInstance().actionToButton(ButtonActions.ELEVATOR_LEVEL3).getPressed()) {
                tsm.elevateTrigger.execute(ElevatorLevel.LEVEL3);
            } else if (KeyBindings.getInstance().actionToButton(ButtonActions.LOAD).getPressed()) {
                tsm.loadTrigger.execute();
            }
        } catch (NullPointerException e) {
            if (!warned) {
                LOGGER.log(Level.WARNING, "Unbound button for elevator movement requested.", e);
                warned = true;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
