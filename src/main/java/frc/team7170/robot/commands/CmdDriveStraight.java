package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.subsystems.Drive;

// TODO: If this is not accurate enough, we might have to bring the gyro into the mix.
public class CmdDriveStraight extends Command {

    private static final Drive drive = Drive.getInstance();

    private final double distanceMetres;

    public CmdDriveStraight(double distanceMetres) {
        this.distanceMetres = distanceMetres;
        requires(drive);
    }

    @Override
    protected void initialize() {
        drive.zeroEncoders();
        drive.setPosition(distanceMetres);
    }

    @Override
    protected void end() {
        drive.killMotors();
    }

    @Override
    protected boolean isFinished() {
        return drive.isErrorTolerable();
    }
}
