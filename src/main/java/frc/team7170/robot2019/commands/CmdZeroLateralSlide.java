package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.subsystems.EndEffector;

public class CmdZeroLateralSlide extends Command {

    private static final EndEffector.LateralSlide lateralSlide = EndEffector.LateralSlide.getInstance();

    public CmdZeroLateralSlide() {
        requires(EndEffector.getInstance());
    }

    @Override
    protected void initialize() {
        System.out.println("Setting servo speed for zeroing...");
        lateralSlide.set(-Constants.EndEffector.ZEROING_THROTTLE_PERCENT);
    }

    @Override
    protected void end() {
        System.out.println("DONE servo zeroing.");
        lateralSlide.kill();
        lateralSlide.resetFeedback();
    }

    @Override
    protected boolean isFinished() {
        return lateralSlide.isLimitSwitchTriggered();
    }
}
