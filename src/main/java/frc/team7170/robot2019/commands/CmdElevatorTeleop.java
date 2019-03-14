package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.Button;
import frc.team7170.lib.oi.ButtonPollHelper;
import frc.team7170.robot2019.ElevatorLevel;
import frc.team7170.robot2019.TeleopStateMachine;
import frc.team7170.robot2019.actions.ButtonActions;
import frc.team7170.robot2019.subsystems.Elevator;

import java.util.logging.Logger;

public class CmdElevatorTeleop extends Command {

    private static final Logger LOGGER = Logger.getLogger(CmdElevatorTeleop.class.getName());
    private static final TeleopStateMachine tsm = TeleopStateMachine.getInstance();

    private static final Button level1 = new ButtonPollHelper(ButtonActions.ELEVATOR_LEVEL1, LOGGER::warning);
    private static final Button level2 = new ButtonPollHelper(ButtonActions.ELEVATOR_LEVEL2, LOGGER::warning);
    private static final Button level3 = new ButtonPollHelper(ButtonActions.ELEVATOR_LEVEL3, LOGGER::warning);
    private static final Button load = new ButtonPollHelper(ButtonActions.LOAD, LOGGER::warning);

    public CmdElevatorTeleop() {
        requires(Elevator.getInstance());
    }

    @Override
    protected void execute() {
        if (level1.getPressed()) {
            tsm.elevateTrigger.execute(ElevatorLevel.LEVEL1);
        } else if (level2.getPressed()) {
            tsm.elevateTrigger.execute(ElevatorLevel.LEVEL2);
        } else if (level3.getPressed()) {
            tsm.elevateTrigger.execute(ElevatorLevel.LEVEL3);
        } else if (load.getPressed()) {
            tsm.loadTrigger.execute();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
