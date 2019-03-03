package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.subsystems.ClimbLegs;

public class CmdExtendLinearActuator extends Command {

    private final ClimbLegs.LinearActuator linearActuator;
    private final double distanceMetres;
    private final boolean hold;

    public CmdExtendLinearActuator(ClimbLegs.LinearActuator linearActuator, double distanceMetres, boolean hold) {
        this.linearActuator = linearActuator;
        this.distanceMetres = distanceMetres;
        this.hold = hold;
    }

    @Override
    protected void initialize() {
        linearActuator.setPosition(distanceMetres);
    }

    @Override
    protected void end() {
        if (!hold) {
            linearActuator.killMotor();
        }
    }

    @Override
    protected boolean isFinished() {
        return linearActuator.isErrorTolerable();
    }
}
