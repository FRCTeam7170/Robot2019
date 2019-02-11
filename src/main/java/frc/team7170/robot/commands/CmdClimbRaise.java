package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.unit.Units;
import frc.team7170.robot.Constants;
import frc.team7170.robot.subsystems.ClimbLegs;

public class CmdClimbRaise extends Command {

    private static final double r = Units.convert(Constants.Dimensions.FRONT_ARM_WHEEL_DIAMETER_INCHES,
            Units.INCH, Units.METRE);
    private static final ClimbLegs.LinearActuator leftLA = ClimbLegs.getInstance().getLeftLinearActuator();
    private static final ClimbLegs.LinearActuator rightLA = ClimbLegs.getInstance().getRightLinearActuator();
    private static final double laOffsetMetres = Constants.Climb.LINEAR_ACTUATOR_CONTACT_DISTANCE_METRES;

    private final double platformHeightMetres;
    private final double targetHeightMetres;
    private final double contactAngleDegrees;

    private double currHeightMetres = 0.0;
    private Command currFrontArmsCommand;
    private Command currLeftLinearActuatorCommand;
    private Command currRightLinearActuatorCommand;

    public CmdClimbRaise(double targetHeightMetres, double contactAngleDegrees) {
        this.platformHeightMetres = targetHeightMetres;
        this.targetHeightMetres = targetHeightMetres + Constants.Climb.FINAL_HEIGHT_EXTRA_METRES;
        this.contactAngleDegrees = contactAngleDegrees;
    }

    @Override
    protected void initialize() {
        currFrontArmsCommand = new CmdRotateFrontArms(contactAngleDegrees, false);
        currLeftLinearActuatorCommand = new CmdExtendLinearActuator(leftLA, laOffsetMetres, false);
        currRightLinearActuatorCommand = new CmdExtendLinearActuator(rightLA, laOffsetMetres, false);
        startAllCommands();
    }

    @Override
    protected void execute() {
        if (allCommandsFinished()) {
            if (!targetHeightReached()) {
                currHeightMetres += Constants.Climb.DELTA_HEIGHT_METRES;
                currFrontArmsCommand = new CmdRotateFrontArms(calcNextTheta(currHeightMetres), true);
                double nextLAHeight = currHeightMetres + laOffsetMetres;
                currLeftLinearActuatorCommand = new CmdExtendLinearActuator(leftLA, nextLAHeight, true);
                currRightLinearActuatorCommand = new CmdExtendLinearActuator(rightLA, nextLAHeight, true);
                startAllCommands();
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return targetHeightReached() && allCommandsFinished();
    }

    private void startAllCommands() {
        currFrontArmsCommand.start();
        currLeftLinearActuatorCommand.start();
        currRightLinearActuatorCommand.start();
    }

    private boolean allCommandsFinished() {
        return currFrontArmsCommand.isCompleted() &&
                currLeftLinearActuatorCommand.isCompleted() &&
                currRightLinearActuatorCommand.isCompleted();
    }

    private boolean targetHeightReached() {
        return currHeightMetres >= targetHeightMetres;
    }

    /*
     * theta = arccos(([L1 to L3] + [arm wheel radius] - [pivot height] - [target height]) /
     *     [pivot to wheel centre distance]) + [acute angle from arm resting/home position to the vertical]
     *
     * Or, in terms of the variables used below:
     *
     * theta(h) = arccos((H + r - y - h) / x) + psi
     */
    private double calcNextTheta(double h) {
        double H = platformHeightMetres;
        double y = Constants.Dimensions.FRONT_ARM_PIVOT_HEIGHT_METRES;
        double x = Constants.Dimensions.FRONT_ARM_PIVOT_TO_WHEEL_CENTRE_METRES;
        double psi = Constants.FrontArms.VERTICAL_ANGLE;

        return Math.acos((H + r - y - h) / x) + psi;
    }
}
