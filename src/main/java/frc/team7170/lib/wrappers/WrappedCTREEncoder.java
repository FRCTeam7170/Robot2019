package frc.team7170.lib.wrappers;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

class WrappedCTREEncoder extends AbstractEncoder {

    private final BaseMotorController ctreMotorController;
    private boolean phaseSensor;

    WrappedCTREEncoder(BaseMotorController ctreMotorController, int cyclesPerRevolution, double distancePerRotation) {
        this.ctreMotorController = ctreMotorController;

        setTicksPerRotation(cyclesPerRevolution * 4);
        setDistancePerRotation(distancePerRotation);
        setEncoderInverted(false);
    }

    @Override
    int getEncoderRaw() {
        return ctreMotorController.getSelectedSensorPosition();
    }

    @Override
    double getEncoderRawRate() {
        return ctreMotorController.getSelectedSensorVelocity();
    }

    @Override
    public void setEncoderInverted(boolean inverted) {
        phaseSensor = inverted;
        ctreMotorController.setSensorPhase(inverted);
    }

    @Override
    public boolean isEncoderInverted() {
        return phaseSensor;
    }

    @Override
    public void resetEncoder() {
        ctreMotorController.setSelectedSensorPosition(0);
    }
}
