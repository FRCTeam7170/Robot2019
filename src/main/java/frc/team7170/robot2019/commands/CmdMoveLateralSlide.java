package frc.team7170.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team7170.lib.math.CalcUtil;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.subsystems.EndEffector;

public class CmdMoveLateralSlide extends Command {

    private static final EndEffector.LateralSlide lateralSlide = EndEffector.LateralSlide.getInstance();

    private final double positionMetres;
    private boolean reversed = false;

    public CmdMoveLateralSlide(double positionMetres) {
        this.positionMetres = CalcUtil.clamp(positionMetres,
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
