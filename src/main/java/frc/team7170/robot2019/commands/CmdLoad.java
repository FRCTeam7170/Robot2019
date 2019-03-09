package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.lib.command.CmdRunnable;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.subsystems.EndEffector;

public class CmdLoad extends CommandGroup {

    public CmdLoad() {
        addSequential(new CmdRunnable(EndEffector.getInstance()::retractPin));
        addSequential(new CmdMoveElevator(Constants.Elevator.LOAD_MOVE_UP_METRES, true));
    }
}
