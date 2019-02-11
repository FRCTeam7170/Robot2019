package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.unit.Units;
import frc.team7170.robot.Constants;
import frc.team7170.robot.subsystems.ClimbLegs;

public class CmdSynchronousVertical extends Command {

    private static final double r = Units.convert(Constants.Dimensions.FRONT_ARM_WHEEL_DIAMETER_INCHES,
            Units.INCH, Units.METRE);
    private static final double laOffsetMetres = Constants.Dimensions.LINEAR_ACTUATOR_CONTACT_DISTANCE_METRES;

    private final double platformHeightMetres;
    private final double targetHeightMetres;
    private final double deltaHeightMetres;
    private final boolean raising;

    private double currHeightMetres;
    private Command currCommand;

    public CmdSynchronousVertical(double platformHeightMetres, double targetHeightMetres,
                                  double deltaHeightMetres, double initialHeight) {
        this.platformHeightMetres = platformHeightMetres;
        this.targetHeightMetres = targetHeightMetres;
        this.deltaHeightMetres = deltaHeightMetres;
        this.raising = targetHeightMetres > initialHeight;
        this.currHeightMetres = initialHeight;
    }

    @Override
    protected void execute() {
        if (currCommand == null || currCommand.isCompleted()) {
            if (!targetHeightReached()) {
                currHeightMetres += deltaHeightMetres;
                currCommand = new CmdDeployAppendages(calcNextTheta(currHeightMetres),
                        currHeightMetres + laOffsetMetres, true);
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return targetHeightReached() && currCommand.isCompleted();
    }

    private boolean targetHeightReached() {
        if (raising) {
            return currHeightMetres >= targetHeightMetres;
        }
        // else descending
        return currHeightMetres <= targetHeightMetres;
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
