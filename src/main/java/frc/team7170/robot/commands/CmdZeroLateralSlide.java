package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.Constants;
import frc.team7170.robot.subsystems.EndEffector;

public class CmdZeroLateralSlide extends Command {

    private static final EndEffector.LateralSlide lateralSlide = EndEffector.LateralSlide.getInstance();

    public CmdZeroLateralSlide() {
        requires(EndEffector.getInstance());
    }

    @Override
    protected void initialize() {
        // TODO: this is only correct if the LS is on the left.
        lateralSlide.set(-Constants.EndEffector.ZEROING_THROTTLE_PERCENT);
    }

    @Override
    protected void end() {
        lateralSlide.kill();
        lateralSlide.resetFeedback();
    }

    @Override
    protected boolean isFinished() {
        return lateralSlide.isLimitSwitchTriggered();
    }
}
