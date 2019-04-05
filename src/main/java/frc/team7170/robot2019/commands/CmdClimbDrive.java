package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.subsystems.ClimbDrive;

public class CmdClimbDrive extends Command {

    private static final ClimbDrive climbDrive = ClimbDrive.getInstance();

    public CmdClimbDrive() {
        requires(climbDrive);
    }

    @Override
    protected void initialize() {
        climbDrive.zeroDIOs();
        climbDrive.setPercent(Constants.ClimbDrive.SPEED);
    }

    @Override
    protected void end() {
        climbDrive.killMotors();
    }

    @Override
    protected boolean isFinished() {
        return climbDrive.getSonicDistance() <= Constants.ClimbDrive.SONIC_DISTANCE_THRESHOLD_MM;
    }
}
