package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.subsystems.Drive;

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
        System.out.println("START DriveStraight");  // TODO: TEMP
        drive.zeroEncoders();
        drive.setPosition(distanceMetres);
    }

    @Override
    protected void end() {
        System.out.println("ENDED DriveStraight");  // TODO: TEMP
        drive.killMotors();
    }

    @Override
    protected boolean isFinished() {
        return drive.isErrorTolerable();
    }
}
