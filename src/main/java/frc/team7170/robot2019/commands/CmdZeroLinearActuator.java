package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.subsystems.ClimbLegs;

public class CmdZeroLinearActuator extends Command {

    private final ClimbLegs.LinearActuator linearActuator;

    public CmdZeroLinearActuator(ClimbLegs.LinearActuator linearActuator) {
        this.linearActuator = linearActuator;
        requires(linearActuator);
    }

    @Override
    protected void initialize() {
        linearActuator.setPercent(-Constants.ClimbLegs.ZEROING_THROTTLE_PERCENT);
    }

    @Override
    protected void end() {
        linearActuator.killMotor();
        linearActuator.zeroEncoder();
    }

    @Override
    protected boolean isFinished() {
        return linearActuator.isLowerLimitSwitchTriggered();
    }
}
