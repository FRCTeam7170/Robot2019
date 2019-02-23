package frc.team7170.robot;

import edu.wpi.first.wpilibj.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO: put in spooky-lib?
public class DIOMultiplexer {

    private static final int UPDATE_PERIOD_MS = 1;
    private static final byte SELECT0_MASK = 0b001;
    private static final byte SELECT1_MASK = 0b010;
    private static final byte SELECT2_MASK = 0b100;

    public enum Input {
        I0((byte) 0),
        I1((byte) 1),
        I2((byte) 2),
        I3((byte) 3),
        I4((byte) 4),
        I5((byte) 5),
        I6((byte) 6),
        I7((byte) 7);

        private final byte n;

        Input(byte n) {
            this.n = n;
        }

        private byte getN() {
            return n;
        }
    }

    // TODO: make this an EnumMap?
    private final Map<Byte, Boolean> inputStates = new HashMap<>(8);
    {
        for (byte i = 0; i < 8; ++i) {
            inputStates.put(i, false);
        }
    }
    private final Notifier notifier;
    private final DigitalOutput select0, select1, select2;
    private final DigitalInput input;
    private ArrayList<DIOMultiplexer> children;
    private byte currSelected = 0;

    public DIOMultiplexer(int select0Pin, int select1Pin, int select2Pin, int inputPin) {
        select0 = new DigitalOutput(select0Pin);
        select1 = new DigitalOutput(select1Pin);
        select2 = new DigitalOutput(select2Pin);
        input = new DigitalInput(inputPin);

        select((byte) 0b000);
        notifier = new Notifier(this::loop);
        notifier.startPeriodic((double) UPDATE_PERIOD_MS / 1000.0);
    }

    public DIOMultiplexer(DIOMultiplexer dioMultiplexer, int inputPin) {
        select0 = select1 = select2 = null;
        notifier = null;
        input = new DigitalInput(inputPin);
        dioMultiplexer.registerChild(this);
    }

    private void registerChild(DIOMultiplexer dioMultiplexer) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(dioMultiplexer);
    }

    public boolean get(Input input) {
        return inputStates.get(input.getN());
    }

    private void select(byte n) {
        select0.set((n & SELECT0_MASK) > 0);
        select1.set((n & SELECT1_MASK) > 0);
        select2.set((n & SELECT2_MASK) > 0);
    }

    private void updateState(byte n) {
        inputStates.replace(currSelected, input.get());
        if (children != null) {
            for (DIOMultiplexer child : children) {
                child.updateState(n);
            }
        }
    }

    private void loop() {
        updateState(currSelected);
        if (currSelected == 7) {
            currSelected = 0;
        } else {
            ++currSelected;
        }
        select(currSelected);
    }
}
