package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.Constants;
import frc.team7170.robot.subsystems.Drive;

// TODO: If this is not accurate enough, we might have to bring the gyro into the mix.
public class CmdDriveStraight extends Command {

    private final double distanceFeet;

    public CmdDriveStraight(double distanceFeet) {
        requires(Drive.getInstance());
        this.distanceFeet = distanceFeet;
    }

    @Override
    protected void initialize() {
        Drive.getInstance().zeroEncoders();
        Drive.getInstance().setPosition(distanceFeet);
    }

    @Override
    protected void end() {
        Drive.getInstance().killMotors();
    }

    @Override
    protected boolean isFinished() {
        return Drive.talonUnitsToFeet(Drive.getInstance().getLeftErrorRaw()) <= Constants.Drive.ALLOWABLE_CLOSED_LOOP_POSITION_ERROR;
    }
}
