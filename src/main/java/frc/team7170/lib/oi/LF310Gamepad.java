package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;
import frc.team7170.lib.Name;

import java.util.HashMap;
import java.util.Map;

public final class LF310Gamepad extends RumbleController {

    public final HIDAxis A_LX;
    public final HIDAxis A_LY;
    public final HIDAxis A_RX;
    public final HIDAxis A_RY;
    public final HIDAxis A_LTRIGGER;
    public final HIDAxis A_RTRIGGER;

    public final HIDButton B_A;
    public final HIDButton B_B;
    public final HIDButton B_X;
    public final HIDButton B_Y;
    public final HIDButton B_LBUMPER;
    public final HIDButton B_RBUMPER;
    public final HIDButton B_BACK;
    public final HIDButton B_START;
    public final HIDButton B_LJOY;
    public final HIDButton B_RJOY;

    private final Map<String, Axis> axes = new HashMap<>();
    private final Map<String, Button> buttons = new HashMap<>();

    public LF310Gamepad(GenericHID hid) {
        super(hid, new Name("LF310Gamepad"));

        A_LX = new HIDAxis(hid, 0);
        A_LY = new HIDAxis(hid, 1);
        A_RX = new HIDAxis(hid, 4);
        A_RY = new HIDAxis(hid, 5);
        A_LTRIGGER = new HIDAxis(hid, 2);
        A_RTRIGGER = new HIDAxis(hid, 3);

        B_A = new HIDButton(hid, 1);
        B_B = new HIDButton(hid, 2);
        B_X = new HIDButton(hid, 3);
        B_Y = new HIDButton(hid, 4);
        B_LBUMPER = new HIDButton(hid, 5);
        B_RBUMPER = new HIDButton(hid, 6);
        B_BACK = new HIDButton(hid, 7);
        B_START = new HIDButton(hid, 8);
        B_LJOY = new HIDButton(hid, 9);
        B_RJOY = new HIDButton(hid, 10);

        for (Axis a : new Axis[] {A_LX, A_LY, A_RX, A_RY, A_LTRIGGER, A_RTRIGGER}) {
            axes.put(a.getName(), a);
        }
        for (Button b : new Button[] {B_A, B_B, B_X, B_Y, B_LBUMPER, B_RBUMPER, B_BACK, B_START, B_LJOY, B_RJOY}) {
            buttons.put(b.getName(), b);
        }
    }

    @Override
    public Map<String, Axis> getAxesNamesMap() {
        return new HashMap<>(axes);
    }

    @Override
    public Map<String, Button> getButtonsNamesMap() {
        return new HashMap<>(buttons);
    }
}
