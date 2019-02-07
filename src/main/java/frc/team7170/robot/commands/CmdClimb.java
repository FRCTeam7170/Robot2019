package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.robot.ClimbLevel;

public class CmdClimb extends CommandGroup {

    public CmdClimb(ClimbLevel climbLevel) {
        addSequential(new CmdDriveStraight(climbLevel.getBumperDistanceInches()));
        addSequential(new CmdClimbRaise(climbLevel.getHeightInches(), climbLevel.getContactAngleDegrees()));
        addSequential(new CmdClimbDrive(climbLevel.getBumperDistanceInches()));
    }
}
