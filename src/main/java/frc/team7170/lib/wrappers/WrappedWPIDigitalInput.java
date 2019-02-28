package frc.team7170.lib.wrappers;

class WrappedWPIDigitalInput implements DigitalInput {

    private final edu.wpi.first.wpilibj.DigitalInput wpiDigitalInput;

    WrappedWPIDigitalInput(edu.wpi.first.wpilibj.DigitalInput wpiDigitalInput) {
        this.wpiDigitalInput = wpiDigitalInput;
    }

    @Override
    public boolean get() {
        return wpiDigitalInput.get();
    }
}
