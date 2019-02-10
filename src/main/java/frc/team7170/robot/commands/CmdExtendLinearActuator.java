package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.subsystems.ClimbLegs;

public class CmdExtendLinearActuator extends Command {

    private final ClimbLegs.LinearActuator linearActuator;
    private final double distanceMetres;

    public CmdExtendLinearActuator(ClimbLegs.LinearActuator linearActuator, double distanceMetres) {
        this.linearActuator = linearActuator;
        this.distanceMetres = distanceMetres;
    }

    @Override
    protected void initialize() {
        linearActuator.setPosition(distanceMetres);
    }

    @Override
    protected void end() {
        linearActuator.killMotor();
    }

    @Override
    protected boolean isFinished() {
        return linearActuator.isErrorTolerable();
    }
}
