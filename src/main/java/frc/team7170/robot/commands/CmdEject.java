package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.lib.command.CmdRunnable;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.robot.TeleopStateMachine;
import frc.team7170.robot.actions.ButtonActions;
import frc.team7170.robot.subsystems.EndEffector;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CmdEject extends CommandGroup {

    private static final Logger LOGGER = Logger.getLogger(CmdEject.class.getName());
    private static final TeleopStateMachine tsm = TeleopStateMachine.getInstance();

    private boolean warned = false;

    public CmdEject() {
        addSequential(new CmdAlignEndEffector());
        addSequential(new CmdRunnable(EndEffector.getInstance()::eject, EndEffector.getInstance()));
        addParallel(new CmdRunnable(tsm.ejectFinishedTrigger::execute));
    }

    @Override
    protected void execute() {
        try {
            if (KeyBindings.getInstance().actionToButton(ButtonActions.EJECT).getPressed()) {
                cancel();
                tsm.ejectFinishedTrigger.execute();
            }
        } catch (NullPointerException e) {
            if (!warned) {
                LOGGER.log(Level.WARNING, "Unbound button for eject command requested.", e);
                warned = true;
            }
        }
    }
}
