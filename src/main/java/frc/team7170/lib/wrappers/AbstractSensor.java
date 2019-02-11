package frc.team7170.lib.wrappers;

import edu.wpi.first.wpilibj.PIDSourceType;

abstract class AbstractSensor implements Sensor {

    private PIDSourceType pidSourceType = PIDSourceType.kDisplacement;

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        if (!isLegalPIDSourceType(pidSource)) {
            throw new IllegalArgumentException("illegal PIDSourceType '" + pidSource.name() + "'");
        }
        pidSourceType = pidSource;
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return pidSourceType;
    }

    protected abstract boolean isLegalPIDSourceType(PIDSourceType pidSourceType);
}
