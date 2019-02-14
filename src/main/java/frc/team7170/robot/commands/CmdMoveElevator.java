package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot.subsystems.Elevator;

public class CmdMoveElevator extends Command {

    private static final Elevator elevator = Elevator.getInstance();

    private final double heightMetres;
    private final boolean hold;

    public CmdMoveElevator(double heightMetres, boolean hold) {
        this.heightMetres = heightMetres;
        this.hold = hold;
        requires(elevator);
    }

    @Override
    protected void initialize() {
        elevator.setPosition(heightMetres);
    }

    @Override
    protected void end() {
        if (!hold) {
            elevator.killMotor();
        }
    }

    @Override
    protected boolean isFinished() {
        return elevator.isErrorTolerable();
    }
}
