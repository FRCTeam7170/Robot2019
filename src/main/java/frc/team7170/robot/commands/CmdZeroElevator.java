package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.Constants;
import frc.team7170.robot.subsystems.Elevator;

public class CmdZeroElevator extends Command {

    private static final Elevator elevator = Elevator.getInstance();

    public CmdZeroElevator() {
        requires(elevator);
    }

    @Override
    protected void initialize() {
        elevator.setPercent(-Constants.Elevator.ZEROING_THROTTLE_PERCENT);
    }

    @Override
    protected void end() {
        elevator.killMotor();
        elevator.zeroEncoder();
    }

    @Override
    protected boolean isFinished() {
        return elevator.isLowerLimitSwitchTriggered();
    }
}
