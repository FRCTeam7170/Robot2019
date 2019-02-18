package frc.team7170.lib.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CmdBlock extends Command {

    public CmdBlock(Subsystem... subsystems) {
        for (Subsystem subsystem : subsystems) {
            requires(subsystem);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
