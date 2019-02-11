package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.robot.ClimbLevel;
import frc.team7170.robot.Constants;
import frc.team7170.robot.subsystems.ClimbLegs;

public class CmdClimb extends CommandGroup {

    public CmdClimb(ClimbLevel climbLevel) {
        // Reverse after aligning bumpers with the desired climb level.
        addSequential(new CmdDriveStraight(-climbLevel.getBumperDistanceMetres()));

        // Raise the robot with the climb legs and front arms in sync.
        addSequential(new CmdClimbRaise(climbLevel.getHeightMetres(), climbLevel.getContactAngleDegrees()));

        // Drive forward using the climb leg wheels.
        addSequential(new CmdClimbDrive(Constants.Climb.PRE_RETRACT_DRIVE_FORWARD_METRES));

        // Reset the position of the front arms and climb legs.
        addSequential(new CmdRotateFrontArms(0.0, false));
        addParallel(new CmdExtendLinearActuator(ClimbLegs.getInstance().getRightLinearActuator(),
                0.0, false));
        addParallel(new CmdExtendLinearActuator(ClimbLegs.getInstance().getLeftLinearActuator(),
                0.0, false));

        // Drive forward using base wheels.
        addSequential(new CmdDriveStraight(Constants.Climb.POST_RETRACT_DRIVE_FORWARD_METRES));
    }
}
