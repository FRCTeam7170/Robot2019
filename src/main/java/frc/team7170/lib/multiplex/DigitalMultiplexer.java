package frc.team7170.lib.multiplex;

import frc.team7170.lib.wrappers.DigitalInput;
import frc.team7170.lib.wrappers.DigitalOutput;

public class DigitalMultiplexer extends Multiplexer {

    private final DigitalInput input;
    private final boolean[] inputState;

    public DigitalMultiplexer(DigitalInput input, DigitalOutput... selectLines) {
        super(selectLines);
        this.input = input;
        inputState = new boolean[nInputs];
    }

    public DigitalMultiplexer(DigitalInput input, Multiplexer parent) {
        super(parent);
        this.input = input;
        inputState = new boolean[nInputs];
    }

    @Override
    void updateState(int selected) {
        inputState[selected] = input.get();
    }

    public boolean get(int n) {
        assertInputIndex(n);
        return inputState[n];
    }

    public DigitalInput getDigitalInputFor(int n) {
        assertInputIndex(n);
        return () -> get(n);
    }
}
