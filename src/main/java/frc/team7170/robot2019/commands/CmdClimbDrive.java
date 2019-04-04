package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.subsystems.ClimbDrive;

public class CmdClimbDrive extends Command {

    private static final ClimbDrive climbDrive = ClimbDrive.getInstance();
//    private static final ClimbDrive.SeatMotor leftSeatMotor = climbDrive.getLeftSeatMotor();
//    private static final ClimbDrive.SeatMotor rightSeatMotor = climbDrive.getRightSeatMotor();

    private final double distanceMetres;  // TODO: this is unused with sonic sensor
//    private boolean leftStopped = false;
//    private boolean rightStopped = false;

    public CmdClimbDrive(double distanceMetres) {
        this.distanceMetres = distanceMetres;
        requires(climbDrive);
    }

    @Override
    protected void initialize() {
        climbDrive.zeroDIOs();
        if (distanceMetres < 0) {
            climbDrive.setPercent(-Constants.ClimbDrive.SPEED);
        } else {
            climbDrive.setPercent(Constants.ClimbDrive.SPEED);
        }
    }

    @Override
    protected void execute() {
//        if (!leftStopped && (Math.abs(leftSeatMotor.getDistanceMetres()) >= Math.abs(distanceMetres))) {
//            leftSeatMotor.killMotor();
//            leftStopped = true;
//        }
//        // TODO: TEMP doubled distance on right
//        if (!rightStopped && (Math.abs(rightSeatMotor.getDistanceMetres()) >= Math.abs(distanceMetres) * 1.6)) {
//            rightSeatMotor.killMotor();
//            rightStopped = true;
//        }
    }

    @Override
    protected void end() {
        climbDrive.killMotors();
    }

    @Override
    protected boolean isFinished() {
        // return leftStopped && rightStopped;
        return climbDrive.getSonicDistance() <= Constants.ClimbDrive.SONIC_DISTANCE_THRESHOLD_MM;
    }
}
