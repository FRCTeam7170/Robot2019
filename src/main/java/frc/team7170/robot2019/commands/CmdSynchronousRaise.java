package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.math.CalcUtil;
import frc.team7170.lib.unit.Units;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.subsystems.ClimbLegs;
import frc.team7170.robot2019.subsystems.FrontArms;

public class CmdSynchronousRaise extends Command {

    private static final ClimbLegs.LinearActuator leftLinearActuator = ClimbLegs.getInstance().getLeftLinearActuator();
    private static final ClimbLegs.LinearActuator rightLinearActuator = ClimbLegs.getInstance().getRightLinearActuator();
    private static final FrontArms frontArms = FrontArms.getInstance();

    private static final double r = Units.convert(Constants.Dimensions.FRONT_ARM_WHEEL_DIAMETER_INCHES,
            Units.INCH, Units.METRE);

    private final double distanceMetres;
    private final double platformHeightMetres;
    private final double initialAngle;
    private final boolean reversed;
    private final double laSpeed;
    private final double faSpeed;

    public CmdSynchronousRaise(double platformHeightMetres, double targetHeightMetres,
                               double initialHeight, double initialAngle) {
        this.platformHeightMetres = platformHeightMetres;
        this.distanceMetres = targetHeightMetres;
        this.initialAngle = initialAngle;

        if (distanceMetres < initialHeight) {
            laSpeed = -Constants.ClimbLegs.SPEED;
            faSpeed = -Constants.Climb.TARGET_CLIMB_SPEED;
            reversed = true;
        } else {
            laSpeed = Constants.ClimbLegs.SPEED;
            faSpeed = Constants.Climb.TARGET_CLIMB_SPEED;
            reversed = false;
        }

        requires(leftLinearActuator);
        requires(rightLinearActuator);
        requires(frontArms);
    }

    @Override
    protected void execute() {
        double laDiff = rightLinearActuator.getDistance() - leftLinearActuator.getDistance();
        double targetAngle = heightToFrontArmAngle(
                (rightLinearActuator.getDistance() + leftLinearActuator.getDistance()) / 2
        );
        double faDiff = targetAngle - (frontArms.getAngle() - Constants.FrontArms.ANGLE_UNCERTAINTY_DEGREES);
        if (reversed) {
            laDiff = -laDiff;
            faDiff = -faDiff;
        }
        leftLinearActuator.setPercent(laSpeed * (1 + laDiff/Constants.ClimbLegs.MAX_DIFFERENTIAL_METRES));
        rightLinearActuator.setPercent(laSpeed * (1 - laDiff/Constants.ClimbLegs.MAX_DIFFERENTIAL_METRES));
        if ((!reversed && (targetAngle > initialAngle)) || (reversed && (targetAngle < initialAngle))) {
            frontArms.setPercent(CalcUtil.clamp(faSpeed * (1 + faDiff / 5),
                    0.0, Constants.Climb.MAXIMUM_CLIMB_SPEED));
        }
    }

    @Override
    protected void end() {
        leftLinearActuator.killMotor();
        rightLinearActuator.killMotor();
        frontArms.setAngle(frontArms.getAngle());
    }

    @Override
    protected boolean isFinished() {
        if (reversed) {
            return (leftLinearActuator.isLowerLimitSwitchTriggered() ||
                    Math.abs(leftLinearActuator.getDistance()) <= distanceMetres);
        } else {
            return (leftLinearActuator.isUpperLimitSwitchTriggered() ||
                    Math.abs(leftLinearActuator.getDistance()) >= distanceMetres);
        }
    }

    /*
     * theta = arccos(([L1 to L3] + [arm wheel radius] - [pivot height] - [target height]) /
     *     [pivot to wheel centre distance]) + [acute angle from arm resting/home position to the vertical]
     *
     * Or, in terms of the variables used below:
     *
     * theta(h) = arccos((H + r - y - h) / x) + psi
     */
    private double heightToFrontArmAngle(double h) {
        double H = platformHeightMetres;
        double y = Constants.Dimensions.FRONT_ARM_PIVOT_HEIGHT_METRES;
        double x = Constants.Dimensions.FRONT_ARM_PIVOT_TO_WHEEL_CENTRE_METRES;
        double psi = Constants.FrontArms.VERTICAL_ANGLE_DEGREES;

        return Math.toDegrees(Math.acos((H + r - y - h) / x)) + psi;
    }
}
