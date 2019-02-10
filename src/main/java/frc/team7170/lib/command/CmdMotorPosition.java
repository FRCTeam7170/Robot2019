package frc.team7170.lib.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team7170.lib.wrappers.PIDMotor;

public class CmdMotorPosition extends Command {

    private final PIDMotor motor;
    private final double position;

    public CmdMotorPosition(PIDMotor motor, double position, Subsystem... requirements) {
        this.motor = motor;
        this.position = position;
        for (Subsystem requirement : requirements) {
            requires(requirement);
        }
    }

    @Override
    protected void initialize() {
        motor.setPositionSetpoint(position);
    }

    @Override
    protected void end() {
        motor.killMotor();
    }

    @Override
    protected boolean isFinished() {
        return motor.onTarget();
    }
}
