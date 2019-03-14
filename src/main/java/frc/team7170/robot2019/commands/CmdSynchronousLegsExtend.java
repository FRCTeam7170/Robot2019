package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.subsystems.ClimbLegs;

// TODO: not used
public class CmdSynchronousLegsExtend extends Command {

    private static final ClimbLegs.LinearActuator leftLinearActuator = ClimbLegs.getInstance().getLeftLinearActuator();
    private static final ClimbLegs.LinearActuator rightLinearActuator = ClimbLegs.getInstance().getRightLinearActuator();

    private final double distance;
    private boolean reversed = false;
    private double speed;

    public CmdSynchronousLegsExtend(double distanceMetres) {
        distance = ClimbLegs.metresToTalonUnits(-distanceMetres);
        System.out.println(String.format("DIST %f", distance));
        requires(leftLinearActuator);
        requires(rightLinearActuator);
    }

    @Override
    protected void initialize() {
        System.out.println("INIT");
        if (distance < 0) {
            speed = Constants.ClimbLegs.SPEED;
        } else {
            speed = -Constants.ClimbLegs.SPEED;
            reversed = true;
        }
    }

    @Override
    protected void execute() {
        double diff = leftLinearActuator.getEncoder() - rightLinearActuator.getEncoder();
        if (reversed) {
            diff = -diff;
        }
        leftLinearActuator.setPercent(speed * (1 - diff/Constants.ClimbLegs.MAX_DIFFERENTIAL));
        rightLinearActuator.setPercent(speed * (1 + diff/Constants.ClimbLegs.MAX_DIFFERENTIAL));
    }

    @Override
    protected void end() {
        System.out.println("END");
        leftLinearActuator.killMotor();
        rightLinearActuator.killMotor();
    }

    @Override
    protected boolean isFinished() {
        return (Math.abs(leftLinearActuator.getEncoder()) >= Math.abs(distance)) &&
                (Math.abs(rightLinearActuator.getEncoder()) >= Math.abs(distance));
    }
}
