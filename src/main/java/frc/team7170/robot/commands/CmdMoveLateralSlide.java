package frc.team7170.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.CalcUtil;
import frc.team7170.robot.Constants;
import frc.team7170.robot.subsystems.EndEffector;

public class CmdMoveLateralSlide extends Command {

    private static final EndEffector.LateralSlide lateralSlide = EndEffector.LateralSlide.getInstance();

    private final double positionMetres;
    private boolean reversed = false;

    public CmdMoveLateralSlide(double positionMetres) {
        this.positionMetres = CalcUtil.applyBounds(positionMetres,
                Constants.EndEffector.LATERAL_SLIDE_LEFT_METRES, Constants.EndEffector.LATERAL_SLIDE_RIGHT_METRES);
        requires(EndEffector.getInstance());
    }

    @Override
    protected void initialize() {
        if (positionMetres < lateralSlide.getFeedback()) {
            reversed = true;
            lateralSlide.set(-Constants.EndEffector.ABSOLUTE_SERVO_SPEED_AUTOMATIC);
        } else {
            lateralSlide.set(Constants.EndEffector.ABSOLUTE_SERVO_SPEED_AUTOMATIC);
        }
    }

    @Override
    protected void end() {
        lateralSlide.kill();
    }

    @Override
    protected boolean isFinished() {
        if (reversed) {
            return lateralSlide.getFeedback() < positionMetres;
        } else {
            return lateralSlide.getFeedback() > positionMetres;
        }
    }
}
