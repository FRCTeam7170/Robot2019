package frc.team7170.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Notifier;

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

    private final Map<Byte, Boolean> inputStates = new HashMap<>(8);
    private final Notifier notifier = new Notifier(this::loop);
    private final DigitalOutput select0;
    private final DigitalOutput select1;
    private final DigitalOutput select2;
    private final DigitalInput input;
    private byte currSelected = 0;

    public DIOMultiplexer(int select0Pin, int select1Pin, int select2Pin, int inputPin) {
        select0 = new DigitalOutput(select0Pin);
        select1 = new DigitalOutput(select1Pin);
        select2 = new DigitalOutput(select2Pin);
        input = new DigitalInput(inputPin);

        for (byte i = 0; i < 8; ++i) {
            inputStates.put(i, false);
        }

        select((byte) 0b000);
        notifier.startPeriodic((double) UPDATE_PERIOD_MS / 1000.0);
    }

    public boolean get(Input input) {
        return inputStates.get(input.getN());
    }

    private void select(byte n) {
        select0.set((n & SELECT0_MASK) > 0);
        select1.set((n & SELECT1_MASK) > 0);
        select2.set((n & SELECT2_MASK) > 0);
    }

    private void loop() {
        inputStates.replace(currSelected, input.get());
        if (currSelected == 7) {
            currSelected = 0;
        } else {
            ++currSelected;
        }
        select(currSelected);
    }
}
