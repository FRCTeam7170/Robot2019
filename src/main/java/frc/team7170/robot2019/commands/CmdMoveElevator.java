package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.robot2019.subsystems.Elevator;

public class CmdMoveElevator extends Command {

    private static final Elevator elevator = Elevator.getInstance();

    private final double heightMetres;
    private final boolean hold;

    public CmdMoveElevator(double heightMetres, boolean hold) {
        System.out.println("INIT ELEVATOR");
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
        System.out.println("END ELEVATOR");
        if (!hold) {
            elevator.killMotor();
        }
    }

    @Override
    protected boolean isFinished() {
        return elevator.isErrorTolerable();
    }
}
