package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.logging.LoggerManager;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.lib.oi.KeyMap;
import frc.team7170.robot.actions.AxisActions;
import frc.team7170.robot.subsystems.Drive;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CmdDriveTeleop extends Command {

    private static final Logger LOGGER = LoggerManager.INSTANCE.getLogger(CmdDriveTeleop.class.getName());

    private static final Drive drive = Drive.INSTANCE;
    private boolean isTankDrive = true;
    private boolean warned = false;

    public CmdDriveTeleop() {
        requires(drive);
    }

    @Override
    protected void initialize() {
        KeyMap km = KeyBindings.getCurrKeyMap();
        if (!km.hasBindingFor(AxisActions.DRIVE_L) && !km.hasBindingFor(AxisActions.DRIVE_R) &&
                km.hasBindingFor(AxisActions.DRIVE_Y) && km.hasBindingFor(AxisActions.DRIVE_Z)) {
            isTankDrive = true;
        }
    }

    @Override
    protected void execute() {
        try {
            if (isTankDrive) {
                drive.tankDrive(
                        KeyBindings.actionToAxis(AxisActions.DRIVE_L).get(),
                        KeyBindings.actionToAxis(AxisActions.DRIVE_R).get()
                );
            } else {
                drive.arcadeDrive(
                        KeyBindings.actionToAxis(AxisActions.DRIVE_Y).get(),
                        KeyBindings.actionToAxis(AxisActions.DRIVE_Z).get()
                );
            }
        } catch (NullPointerException e) {
            if (!warned) {
                String mode = isTankDrive ? "tank" : "arcade";
                LOGGER.log(Level.WARNING, "unbound axis for driving requested in " + mode + " drive mode", e);
                warned = true;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
