package frc.team7170.lib.command;

import edu.wpi.first.wpilibj.command.Command;


public class TimedRunnableCommand extends Command {

    private Runnable runnable;
    private int delayMs;
    private long time;

    public TimedRunnableCommand(Runnable runnable, int delayMs, boolean startOnInit) {
        this.runnable = runnable;
        this.delayMs = delayMs;
        if (startOnInit) start();
    }

    public TimedRunnableCommand(Runnable runnable, int delayMs) {
        this(runnable, delayMs, true);
    }

    @Override
    protected void initialize() {
        time = System.currentTimeMillis();
    }

    @Override
    protected void execute() {
        if (System.currentTimeMillis() >= time + delayMs) {
            runnable.run();
            time = System.currentTimeMillis();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
