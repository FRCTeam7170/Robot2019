package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.subsystems.ClimbLegs;

public class CmdSynchronousExtendLinearActuators extends Command {

    private static final ClimbLegs.LinearActuator leftLinearActuator = ClimbLegs.getInstance().getLeftLinearActuator();
    private static final ClimbLegs.LinearActuator rightLinearActuator = ClimbLegs.getInstance().getRightLinearActuator();

    private final double distance;
    private boolean reversed = false;
    private double speed;

    // TODO: TEMP -- gross thingy
    public CmdSynchronousExtendLinearActuators(double distanceMetres, boolean withRequire) {
        distance = ClimbLegs.metresToTalonUnits(distanceMetres);
        if (withRequire) {
            requires(leftLinearActuator);
            requires(rightLinearActuator);
        }
    }

    public CmdSynchronousExtendLinearActuators(double distanceMetres) {
        this(distanceMetres, true);
    }

    @Override
    protected void initialize() {
        if (distance < Math.abs(leftLinearActuator.getEncoder())) {
            speed = -Constants.ClimbLegs.SPEED;
            reversed = true;
        } else {
            speed = Constants.ClimbLegs.SPEED;
        }
    }

    @Override
    protected void execute() {
        double diff = rightLinearActuator.getEncoder() - leftLinearActuator.getEncoder();
        if (reversed) {
            diff = -diff;
        }
        leftLinearActuator.setPercent(speed * (1 - diff/Constants.ClimbLegs.MAX_DIFFERENTIAL));
        rightLinearActuator.setPercent(speed * (1 + diff/Constants.ClimbLegs.MAX_DIFFERENTIAL));
    }

    @Override
    protected void end() {
        leftLinearActuator.killMotor();
        rightLinearActuator.killMotor();
    }

    @Override
    protected boolean isFinished() {
        if (reversed) {
            return (leftLinearActuator.isLowerLimitSwitchTriggered() ||
                    Math.abs(leftLinearActuator.getEncoder()) <= distance) &&
                    (rightLinearActuator.isLowerLimitSwitchTriggered() ||
                    Math.abs(rightLinearActuator.getEncoder()) <= distance);
        } else {
            return (leftLinearActuator.isUpperLimitSwitchTriggered() ||
                    Math.abs(leftLinearActuator.getEncoder()) >= distance) &&
                    (rightLinearActuator.isUpperLimitSwitchTriggered() ||
                    Math.abs(rightLinearActuator.getEncoder()) >= distance);
        }
    }
}
