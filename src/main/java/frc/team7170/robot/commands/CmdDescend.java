package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team7170.robot.Constants;
import frc.team7170.robot.subsystems.ClimbLegs;

// TODO: we might be able to drive and swing front arms at same time... requires testing.
public class CmdDescend extends CommandGroup {

    public CmdDescend() {
        // Move to the edge of the hab.
        addSequential(new CmdDriveStraight(Constants.Descent.PRE_FRONT_ARM_DEPLOY_DRIVE_FORWARD_METRES));

        // Rotate the front arms into position to support the front of the bot.
        addSequential(new CmdRotateFrontArms(Constants.Descent.ARM_ANGLE_DEGREES, true));

        // Drive forward until the linear actuators are clear of the edge of the hab.
        addSequential(new CmdDriveStraight(Constants.Descent.PRE_EXTEND_DRIVE_FORWARD_METRES));

        // Extend both linear actuators to support the back of the bot.
        addSequential(new CmdExtendLinearActuator(ClimbLegs.getInstance().getLeftLinearActuator(),
                Constants.Descent.LINEAR_ACTUATOR_EXTENSION_METRES, true));
        addParallel(new CmdExtendLinearActuator(ClimbLegs.getInstance().getRightLinearActuator(),
                Constants.Descent.LINEAR_ACTUATOR_EXTENSION_METRES, true));

        // Drive forward until the rear bumper is clear of the edge of the hab.
        addSequential(new CmdClimbDrive(Constants.Descent.POST_EXTEND_DRIVE_FORWARD_METRES));

        if (!Constants.Descent.HARD_DROP) {
            // Descend nice and synchronously if hard drop is disabled.
            addSequential(new CmdSynchronousVertical(
                    0.0,
                    0.0,
                    Constants.Descent.DELTA_HEIGHT_METRES,
                    Constants.Descent.LINEAR_ACTUATOR_EXTENSION_METRES
            ));
        }

        // Reset the position of the front arms and climb legs.
        addSequential(new CmdDeployAppendages(Constants.FrontArms.HOME_ANGLE_DEGREES,
                Constants.ClimbLegs.HOME_METRES, false));
    }
}
