package frc.team7170.lib.command;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CmdSleep extends Command {

    private final double sleepSec;
    private double time;

    public CmdSleep(int sleepMs, Subsystem... requirements) {
        sleepSec = (double) sleepMs / 1000;
        for (Subsystem requirement : requirements) {
            requires(requirement);
        }
    }

    @Override
    protected void initialize() {
        time = Timer.getFPGATimestamp();
    }

    @Override
    protected boolean isFinished() {
        return Timer.getFPGATimestamp() >= time + sleepSec;
    }
}
