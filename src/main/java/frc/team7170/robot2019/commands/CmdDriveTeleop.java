package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.*;
import frc.team7170.robot2019.TeleopStateMachine;
import frc.team7170.robot2019.actions.AxisActions;
import frc.team7170.robot2019.actions.ButtonActions;
import frc.team7170.robot2019.subsystems.Drive;

import java.util.logging.Logger;

// TODO: Currently, one must toggle enabled/disabled to redetermine if we should use tank or arcade mode.
public class CmdDriveTeleop extends Command {

    private static final Logger LOGGER = Logger.getLogger(CmdDriveTeleop.class.getName());
    private static final TeleopStateMachine tsm = TeleopStateMachine.getInstance();
    private static final Drive drive = Drive.getInstance();

    private static final Axis driveL = new AxisPollHelper(AxisActions.DRIVE_L, LOGGER::warning);
    private static final Axis driveR = new AxisPollHelper(AxisActions.DRIVE_R, LOGGER::warning);
    private static final Axis driveY = new AxisPollHelper(AxisActions.DRIVE_Y, LOGGER::warning);
    private static final Axis driveZ = new AxisPollHelper(AxisActions.DRIVE_Z, LOGGER::warning);
    private static final Button turtle = new ButtonPollHelper(ButtonActions.TURTLE_MODE, LOGGER::warning);
    private static final Button rabbit = new ButtonPollHelper(ButtonActions.RABBIT_MODE, LOGGER::warning);
    private static final Button turtleRabbitToggle = new ButtonPollHelper(ButtonActions.TURTLE_RABBIT_TOGGLE, LOGGER::warning);

    private boolean isTankDrive = true;

    public CmdDriveTeleop() {
        requires(Drive.getInstance());
    }

    @Override
    protected void initialize() {
        KeyMap km = KeyBindings.getInstance().getCurrKeyMap();
        if ((!km.hasBindingFor(AxisActions.DRIVE_L) || !km.hasBindingFor(AxisActions.DRIVE_R)) &&
                km.hasBindingFor(AxisActions.DRIVE_Y) && km.hasBindingFor(AxisActions.DRIVE_Z)) {
            isTankDrive = false;
        }
    }

    @Override
    protected void execute() {
        if (isTankDrive) {
            tsm.driveTrigger.execute(driveL.get(), driveR.get(), true);
        } else {
            tsm.driveTrigger.execute(driveY.get(), driveZ.get(), false);
        }
        if (turtle.getPressed()) {
            drive.toTurtleMode();
        } else if (rabbit.getPressed()) {
            drive.toRabbitMode();
        } else if (turtleRabbitToggle.getPressed()) {
            drive.toggleTurtleRabbitMode();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
