package frc.team7170.lib.multiplex;

import edu.wpi.first.wpilibj.Notifier;
import frc.team7170.lib.wrappers.DigitalOutput;

import java.util.ArrayList;
import java.util.List;

// TODO: support linking multiplexers with different numbers of select lines
public abstract class Multiplexer {

    private static final int UPDATE_PERIOD_MS = 10;  // TODO: why doesn't this work at 1 ms?

    private final Notifier notifier;
    private final DigitalOutput[] selectLines;
    final int nInputs;
    private int currSelected;
    private List<Multiplexer> children;

    // LSB first on select lines
    // TODO: take in pin numbers instead of DigitalOutputs?
    public Multiplexer(DigitalOutput... selectLines) {
        this.selectLines = selectLines;
        nInputs = 1 << selectLines.length;
        select(0);
        notifier = new Notifier(this::loop);
        notifier.startPeriodic((double) UPDATE_PERIOD_MS / 1000.0);
    }

    public Multiplexer(Multiplexer parent) {
        selectLines = null;
        notifier = null;
        nInputs = parent.nInputs;
        parent.registerChild(this);
    }

    private void registerChild(Multiplexer child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    private void select(int n) {
        int mask = 0b1;
        for (DigitalOutput selectLine : selectLines) {
            selectLine.set((n & mask) > 0);
            mask <<= 1;
        }
    }

    private void loop() {
        updateState(currSelected);
        if (children != null) {
            for (Multiplexer child : children) {
                child.updateState(currSelected);
            }
        }
        if (currSelected == (nInputs - 1)) {
            currSelected = 0;
        } else {
            ++currSelected;
        }
        select(currSelected);
    }

    void assertInputIndex(int n) {
        if (n < 0 || n >= nInputs) {
            throw new IllegalArgumentException(String.format("input index must be in range [%d, %d]", 0, nInputs - 1));
        }
    }

    abstract void updateState(int selected);
}
