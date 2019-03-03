package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.subsystems.FrontArms;

public class CmdZeroFrontArms extends Command {

    private static final FrontArms frontArms = FrontArms.getInstance();

    public CmdZeroFrontArms() {
        requires(frontArms);
    }

    @Override
    protected void initialize() {
        frontArms.setPercent(-Constants.FrontArms.ZEROING_THROTTLE_PERCENT);
    }

    @Override
    protected void end() {
        frontArms.killMotors();
        frontArms.zeroEncoder();
    }

    @Override
    protected boolean isFinished() {
        return frontArms.isReverseLimitSwitchTriggered();
    }
}
