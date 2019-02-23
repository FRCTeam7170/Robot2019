package frc.team7170.lib.multiplex;

import frc.team7170.lib.wrappers.AnalogInput;
import frc.team7170.lib.wrappers.DigitalOutput;

public class AnalogMultiplexer extends Multiplexer {

    private final AnalogInput input;
    private final double[] inputState;

    public AnalogMultiplexer(AnalogInput input, DigitalOutput... selectLines) {
        super(selectLines);
        this.input = input;
        inputState = new double[nInputs];
    }

    public AnalogMultiplexer(AnalogInput input, Multiplexer parent) {
        super(parent);
        this.input = input;
        inputState = new double[nInputs];
    }

    @Override
    void updateState(int selected) {
        inputState[selected] = input.getVoltage();
    }

    public double get(int n) {
        assertInputIndex(n);
        return inputState[n];
    }

    public AnalogInput getAnalogInputFor(int n) {
        assertInputIndex(n);
        return () -> get(n);
    }
}
