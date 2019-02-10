package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.robot.ClimbLevel;

public class CmdClimb extends CommandGroup {

    public CmdClimb(ClimbLevel climbLevel) {
        addSequential(new CmdDriveStraight(-climbLevel.getBumperDistanceMetres()));
        addSequential(new CmdClimbRaise(climbLevel.getHeightMetres(), climbLevel.getContactAngleDegrees()));
        addSequential(new CmdClimbDrive(climbLevel.getBumperDistanceMetres()));
    }
}
