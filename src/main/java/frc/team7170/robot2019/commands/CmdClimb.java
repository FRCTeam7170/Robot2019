package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.robot2019.ClimbLevel;
import frc.team7170.robot2019.Constants;

public class CmdClimb extends CommandGroup {

    public CmdClimb(ClimbLevel climbLevel) {
        // Reverse after aligning bumpers with the desired climb level.
        addSequential(new CmdDriveStraight(-climbLevel.getBumperDistanceMetres()));

        // Initialize the arm's and linear actuators' position.
        addSequential(new CmdDeployAppendages(climbLevel.getContactAngleDegrees(),
                Constants.Dimensions.LINEAR_ACTUATOR_CONTACT_DISTANCE_METRES, false));

        // Raise the robot with the climb legs and front arms in sync.
        addSequential(new CmdSynchronousVertical(
                climbLevel.getHeightMetres(),
                climbLevel.getHeightMetres() + Constants.Climb.FINAL_HEIGHT_EXTRA_METRES,
                Constants.Climb.DELTA_HEIGHT_METRES,
                0.0
        ));

        // Drive forward using the climb leg wheels.
        addSequential(new CmdClimbDrive(Constants.Climb.PRE_RETRACT_DRIVE_FORWARD_METRES));

        // Reset the position of the front arms and climb legs.
        addSequential(new CmdDeployAppendages(Constants.FrontArms.HOME_ANGLE_DEGREES,
                Constants.ClimbLegs.HOME_METRES, false));

        // Drive forward using base wheels.
        addSequential(new CmdDriveStraight(Constants.Climb.POST_RETRACT_DRIVE_FORWARD_METRES));
    }
}
