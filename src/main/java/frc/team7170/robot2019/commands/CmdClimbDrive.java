package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.subsystems.ClimbDrive;

public class CmdClimbDrive extends Command {

    private static final ClimbDrive climbDrive = ClimbDrive.getInstance();

    private final double distanceMetres;

    public CmdClimbDrive(double distanceMetres) {
        this.distanceMetres = distanceMetres;
        requires(climbDrive);
    }

    @Override
    protected void initialize() {
        System.out.println("INIT CLIMB DRIVE");
        climbDrive.zeroDIOs();
        if (distanceMetres < 0) {
            climbDrive.setPercent(-Constants.ClimbDrive.SPEED);
        } else {
            climbDrive.setPercent(Constants.ClimbDrive.SPEED);
        }
    }

    @Override
    protected void end() {
        System.out.println("END CLIMB DRIVE");
        climbDrive.killMotors();
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(climbDrive.getAvgDistanceMetres()) >= Math.abs(distanceMetres);
    }
}
