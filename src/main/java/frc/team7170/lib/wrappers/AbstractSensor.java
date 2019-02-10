package frc.team7170.lib.wrappers;

abstract class AbstractSensor implements Sensor {

    private MotorMode motorMode = MotorMode.POSITION;

    @Override
    public void setPIDMotorMode(MotorMode motorMode) {
        this.motorMode = motorMode;
    }

    @Override
    public MotorMode getPIDMotorMode() {
        return motorMode;
    }
}
