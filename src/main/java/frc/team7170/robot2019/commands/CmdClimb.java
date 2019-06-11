package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.robot2019.ClimbLevel;
import frc.team7170.robot2019.Constants;

public class CmdClimb extends CommandGroup {

    public CmdClimb(ClimbLevel climbLevel) {
        // Initialize the arm's and linear actuators' position.
        addSequential(new CmdDeployAppendages(
                climbLevel.getInitialAngleDegrees(),
                Constants.Dimensions.LINEAR_ACTUATOR_CONTACT_DISTANCE_METRES,
                true)
        );

        // Raise the robot with the climb legs and front arms in sync.
//        addSequential(new CmdSynchronousVertical(
//                climbLevel.getHeightMetres(),
//                climbLevel.getHeightMetres() + Constants.Climb.FINAL_HEIGHT_EXTRA_METRES,
//                Constants.Climb.DELTA_HEIGHT_METRES,
//                0.0
//        ));
//        addSequential(new CmdDeployAppendages(
//                130.0,
//                climbLevel.getHeightMetres() + Constants.Climb.FINAL_HEIGHT_EXTRA_METRES,
//                true)
//        );
        addSequential(new CmdSynchronousRaise(
                climbLevel.getHeightMetres(),
                climbLevel.getHeightMetres() + Constants.Climb.FINAL_HEIGHT_EXTRA_METRES,
                0.0,
                climbLevel.getInitialAngleDegrees()
        ));

        // Drive forward using the climb leg wheels.
        addSequential(new CmdClimbDrive());

        // Reset the position of the front arms and climb legs.
        addSequential(new CmdDeployAppendages(
                Constants.FrontArms.HOME_ANGLE_DEGREES,
                Constants.ClimbLegs.HOME_METRES,
                false)
        );
    }
}
