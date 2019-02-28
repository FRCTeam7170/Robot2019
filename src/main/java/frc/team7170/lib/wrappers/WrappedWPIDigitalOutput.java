package frc.team7170.lib.wrappers;

class WrappedWPIDigitalOutput implements DigitalOutput {

    private final edu.wpi.first.wpilibj.DigitalOutput wpiDigitalOutput;

    WrappedWPIDigitalOutput(edu.wpi.first.wpilibj.DigitalOutput wpiDigitalOutput) {
        this.wpiDigitalOutput = wpiDigitalOutput;
    }

    @Override
    public void set(boolean value) {
        wpiDigitalOutput.set(value);
    }

    @Override
    public boolean get() {
        return wpiDigitalOutput.get();
    }
}
