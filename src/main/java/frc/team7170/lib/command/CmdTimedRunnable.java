package frc.team7170.lib.command;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CmdTimedRunnable extends Command {

    private final Runnable runnable;
    private final double delaySec;
    private double time;

    public CmdTimedRunnable(Runnable runnable, int delayMs, boolean startOnInit, Subsystem... requirements) {
        this.runnable = runnable;
        delaySec = (double) delayMs / 1000;
        for (Subsystem requirement : requirements) {
            requires(requirement);
        }
        if (startOnInit) start();
    }

    public CmdTimedRunnable(Runnable runnable, int delayMs, Subsystem... requirements) {
        this(runnable, delayMs, true, requirements);
    }

    @Override
    protected void initialize() {
        time = Timer.getFPGATimestamp();
    }

    @Override
    protected void execute() {
        if (Timer.getFPGATimestamp() >= time + delaySec) {
            runnable.run();
            time = Timer.getFPGATimestamp();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
