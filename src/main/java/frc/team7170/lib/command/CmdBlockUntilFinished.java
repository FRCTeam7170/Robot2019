package frc.team7170.lib.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CmdBlockUntilFinished extends Command {

    private final Command command;

    public CmdBlockUntilFinished(Command command, Subsystem... requirements) {
        this.command = command;
        for (Subsystem requirement : requirements) {
            requires(requirement);
        }
    }

    @Override
    protected boolean isFinished() {
        return command.isCompleted();
    }
}
