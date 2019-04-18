package frc.team7170.lib.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team7170.lib.PeriodicRunnable;

// TODO: this and PeriodicRunnable named similar but don't behave the same...
public class CmdTimedRunnable extends Command {

    private final PeriodicRunnable periodicRunnable;

    public CmdTimedRunnable(Runnable runnable, int delayMs, Subsystem... requirements) {
        periodicRunnable = new PeriodicRunnable(runnable, delayMs);
        for (Subsystem requirement : requirements) {
            requires(requirement);
        }
    }

    @Override
    protected void execute() {
        periodicRunnable.run();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
