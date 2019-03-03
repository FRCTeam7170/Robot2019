package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.lib.oi.KeyMap;
import frc.team7170.robot2019.TeleopStateMachine;
import frc.team7170.robot2019.actions.AxisActions;
import frc.team7170.robot2019.subsystems.Drive;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CmdDriveTeleop extends Command {

    private static final Logger LOGGER = Logger.getLogger(CmdDriveTeleop.class.getName());
    private static final TeleopStateMachine tsm = TeleopStateMachine.getInstance();

    private boolean isTankDrive = true;
    private boolean warned = false;

    public CmdDriveTeleop() {
        requires(Drive.getInstance());
    }

    @Override
    protected void initialize() {
        KeyMap km = KeyBindings.getInstance().getCurrKeyMap();
        if (!km.hasBindingFor(AxisActions.DRIVE_L) && !km.hasBindingFor(AxisActions.DRIVE_R) &&
                km.hasBindingFor(AxisActions.DRIVE_Y) && km.hasBindingFor(AxisActions.DRIVE_Z)) {
            isTankDrive = false;
        }
    }

    @Override
    protected void execute() {
        try {
            if (isTankDrive) {
                tsm.driveTrigger.execute(
                        KeyBindings.getInstance().actionToAxis(AxisActions.DRIVE_L).get(),
                        KeyBindings.getInstance().actionToAxis(AxisActions.DRIVE_R).get(),
                        true
                );
            } else {
                tsm.driveTrigger.execute(
                        KeyBindings.getInstance().actionToAxis(AxisActions.DRIVE_Y).get(),
                        KeyBindings.getInstance().actionToAxis(AxisActions.DRIVE_Z).get(),
                        false
                );
            }
        } catch (NullPointerException e) {
            if (!warned) {
                String mode = isTankDrive ? "tank" : "arcade";
                LOGGER.log(Level.WARNING, "Unbound axis for driving requested in " + mode + " drive mode.", e);
                warned = true;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
