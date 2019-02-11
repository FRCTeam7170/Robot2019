package frc.team7170.lib.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team7170.lib.TimedRunnable;

public class CmdTimedRunnable extends Command {

    private final TimedRunnable timedRunnable;

    public CmdTimedRunnable(Runnable runnable, int delayMs, boolean startOnInit, Subsystem... requirements) {
        timedRunnable = new TimedRunnable(runnable, delayMs);
        for (Subsystem requirement : requirements) {
            requires(requirement);
        }
        if (startOnInit) start();
    }

    public CmdTimedRunnable(Runnable runnable, int delayMs, Subsystem... requirements) {
        this(runnable, delayMs, true, requirements);
    }

    @Override
    protected void execute() {
        timedRunnable.run();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
