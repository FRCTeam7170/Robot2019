package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.Constants;
import frc.team7170.robot.subsystems.ClimbLegs;

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
        linearActuator.setPercent(0.0);
        linearActuator.zeroEncoder();
    }

    @Override
    protected boolean isFinished() {
        return linearActuator.isUpperLimitSwitchTriggered();
    }
}
