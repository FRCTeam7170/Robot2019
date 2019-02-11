package frc.team7170.lib.wrappers;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public interface Sensor extends PIDSource {

    default MotorMode getSensorMotorMode() {
        return MotorMode.fromPIDSourceType(getPIDSourceType());
    }

    default void setSensorMotorMode(MotorMode motorMode) {
        PIDSourceType pidSourceType = motorMode.toPIDSourceType();
        if (pidSourceType == null) {
            throw new IllegalArgumentException("Sensor MotorMode must be position or velocity");
        }
        setPIDSourceType(pidSourceType);
    }
}
