package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.oi.*;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.actions.AxisActions;
import frc.team7170.robot2019.actions.ButtonActions;
import frc.team7170.robot2019.subsystems.EndEffector;

import java.util.logging.Logger;

public class CmdManualLateralSlide extends Command {

    private static final Logger LOGGER = Logger.getLogger(CmdManualLateralSlide.class.getName());
    private static final EndEffector.LateralSlide lateralSlide = EndEffector.LateralSlide.getInstance();

    private static final Axis analogLS = new AxisPollHelper(AxisActions.LATERAL_SLIDE, LOGGER::warning);
    private static final Button leftLS = new ButtonPollHelper(ButtonActions.LATERAL_SLIDE_LEFT, LOGGER::warning);
    private static final Button rightLS = new ButtonPollHelper(ButtonActions.LATERAL_SLIDE_RIGHT, LOGGER::warning);
    private static final Button eject = new ButtonPollHelper(ButtonActions.EJECT, LOGGER::warning);

    private boolean isAnalogControl = true;
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

    // TODO: this should operate through the state machine for consistency's sake at least
    @Override
    protected void execute() {
        if (isAnalogControl) {
            lateralSlide.set(analogLS.get());
        } else {
            boolean leftTriggered = leftLS.get();
            boolean rightTriggered = rightLS.get();
            if (leftTriggered == rightTriggered) {  // leftTriggered XNOR rightTriggered
                lateralSlide.kill();
            } else if (leftTriggered) {
                lateralSlide.set(-Constants.EndEffector.ABSOLUTE_SERVO_SPEED_MANUAL);
            } else {  // rightTriggered
                lateralSlide.set(Constants.EndEffector.ABSOLUTE_SERVO_SPEED_MANUAL);
            }
        }
        if (eject.getPressed()) {
            ejectTriggered = true;
        }
    }

    @Override
    protected boolean isFinished() {
        return ejectTriggered;
    }
}
