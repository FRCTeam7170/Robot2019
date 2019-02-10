package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.subsystems.ClimbDrive;

public class CmdClimbDrive extends Command {

    private static final ClimbDrive climbDrive = ClimbDrive.getInstance();

    private final double distanceMetres;

    public CmdClimbDrive(double distanceMetres) {
        this.distanceMetres = distanceMetres;
        requires(climbDrive);
    }

    @Override
    protected void initialize() {
        climbDrive.zeroDIOs();
        climbDrive.setPosition(distanceMetres);
    }

    @Override
    protected void end() {
        climbDrive.killMotors();
    }

    @Override
    protected boolean isFinished() {
        return climbDrive.isErrorTolerable();
    }
}
