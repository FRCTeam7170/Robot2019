package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.lib.command.CmdRunnable;
import frc.team7170.lib.oi.Button;
import frc.team7170.lib.oi.ButtonPollHelper;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.TeleopStateMachine;
import frc.team7170.robot2019.actions.ButtonActions;
import frc.team7170.robot2019.subsystems.EndEffector;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CmdEject extends CommandGroup {

    private static final Logger LOGGER = Logger.getLogger(CmdEject.class.getName());
    private static final TeleopStateMachine tsm = TeleopStateMachine.getInstance();

    private static final Button ejectCancel = new ButtonPollHelper(ButtonActions.EJECT_CANCEL, LOGGER::warning);

    public CmdEject() {
        EndEffector.ReflectanceSensorArray.LineDeviation lineDeviation =
                EndEffector.ReflectanceSensorArray.getInstance().getDeviationFromLine();
        if (lineDeviation.isError()) {
            LOGGER.warning("Error while calculating line deviation; switching to manual control.");
            addSequential(new CmdManualLateralSlide());
        } else {
            // TODO: we always remove ARRAY_LENGTH_M/2 and then add it back...
            addSequential(new CmdMoveLateralSlide(lineDeviation.getValue() +
                    Constants.ReflectanceSensorArray.ARRAY_LENGTH_M / 2));
        }
        addSequential(new CmdRunnable(EndEffector.getInstance()::eject, EndEffector.getInstance()));
        addParallel(new CmdRunnable(tsm.ejectFinishedTrigger::execute));
    }

    @Override
    protected void execute() {
        if (ejectCancel.getPressed()) {
            cancel();
            tsm.ejectFinishedTrigger.execute();
        }
    }
}
