package frc.team7170.lib.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team7170.lib.wrappers.PIDFMotor;

public class CmdMotorVelocity extends Command {

    private final PIDFMotor motor;
    private final double velocity;

    public CmdMotorVelocity(PIDFMotor motor, double velocity, Subsystem... requirements) {
        this.motor = motor;
        this.velocity = velocity;
        for (Subsystem requirement : requirements) {
            requires(requirement);
        }
    }

    @Override
    protected void initialize() {
        motor.setVelocitySetpoint(velocity);
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
