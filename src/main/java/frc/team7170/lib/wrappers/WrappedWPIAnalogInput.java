package frc.team7170.lib.wrappers;

class WrappedWPIAnalogInput implements AnalogInput{

    private final edu.wpi.first.wpilibj.AnalogInput wpiAnalogInput;

    WrappedWPIAnalogInput(edu.wpi.first.wpilibj.AnalogInput wpiAnalogInput) {
        this.wpiAnalogInput = wpiAnalogInput;
    }

    @Override
    public double getVoltage() {
        return wpiAnalogInput.getAverageVoltage();
    }
}
