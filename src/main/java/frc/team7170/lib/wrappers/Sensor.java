package frc.team7170.lib.wrappers;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public interface Sensor extends PIDSource {

    void setPIDMotorMode(MotorMode motorMode);

    @Override
    default void setPIDSourceType(PIDSourceType pidSource) {
        setPIDMotorMode(MotorMode.fromPIDSourceType(pidSource));
    }

    // This should only ever return MotorMode.POSITION or MotorMode.VELOCITY!
    MotorMode getPIDMotorMode();

    @Override
    default PIDSourceType getPIDSourceType() {
        return getPIDMotorMode().toPIDSourceType();
    }

    // TODO: asType method(s)?
}
