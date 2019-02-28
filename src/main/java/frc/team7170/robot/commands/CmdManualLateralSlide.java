package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.KeyBindings;
import frc.team7170.lib.oi.KeyMap;
import frc.team7170.robot.Constants;
import frc.team7170.robot.actions.AxisActions;
import frc.team7170.robot.actions.ButtonActions;
import frc.team7170.robot.subsystems.EndEffector;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CmdManualLateralSlide extends Command {

    private static final Logger LOGGER = Logger.getLogger(CmdManualLateralSlide.class.getName());
    private static final EndEffector.LateralSlide lateralSlide = EndEffector.LateralSlide.getInstance();

    private boolean isAnalogControl = true;
    private boolean warned = false;
    private boolean ejectTriggered = false;

    public CmdManualLateralSlide() {
        requires(EndEffector.getInstance());
    }

    @Override
    protected void initialize() {
        KeyMap km = KeyBindings.getInstance().getCurrKeyMap();
        if (!km.hasBindingFor(AxisActions.LATERAL_SLIDE) &&
                km.hasBindingFor(ButtonActions.LATERAL_SLIDE_LEFT) &&
                km.hasBindingFor(ButtonActions.LATERAL_SLIDE_RIGHT)) {
            isAnalogControl = false;
        }
    }

    @Override
    protected void execute() {
        try {
            if (isAnalogControl) {
                lateralSlide.set(KeyBindings.getInstance().actionToAxis(AxisActions.LATERAL_SLIDE).get());
            } else {
                boolean leftTriggered = KeyBindings.getInstance().actionToButton(ButtonActions.LATERAL_SLIDE_LEFT).get();
                boolean rightTriggered = KeyBindings.getInstance().actionToButton(ButtonActions.LATERAL_SLIDE_RIGHT).get();
                if (leftTriggered == rightTriggered) {  // leftTriggered XNOR rightTriggered
                    lateralSlide.kill();
                } else if (leftTriggered) {
                    lateralSlide.set(-Constants.EndEffector.ABSOLUTE_SERVO_SPEED_MANUAL);
                } else {  // rightTriggered
                    lateralSlide.set(Constants.EndEffector.ABSOLUTE_SERVO_SPEED_MANUAL);
                }
            }
            if (KeyBindings.getInstance().actionToButton(ButtonActions.EJECT).getPressed()) {
                ejectTriggered = true;
            }
        } catch (NullPointerException e) {
            if (!warned) {
                String mode = isAnalogControl ? "analog" : "bang-bang";
                LOGGER.log(Level.WARNING, "Unbound axis for manual lateral slide control requested in " + mode + " mode.", e);
                warned = true;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return ejectTriggered;
    }
}
