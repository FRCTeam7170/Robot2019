package frc.team7170.lib.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CmdRunnable extends Command {

    private final Runnable runnable;

    public CmdRunnable(Runnable runnable, Subsystem... requirements) {
        this.runnable = runnable;
        for (Subsystem requirement : requirements) {
            requires(requirement);
        }
    }

    @Override
    protected void initialize() {
        runnable.run();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
