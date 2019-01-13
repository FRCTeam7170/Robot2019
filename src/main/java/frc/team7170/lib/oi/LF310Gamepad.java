package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;

import java.util.HashMap;
import java.util.Map;


public final class LF310Gamepad extends Controller {

    public final SimpleAxis A_LX;
    public final SimpleAxis A_LY;
    public final SimpleAxis A_RX;
    public final SimpleAxis A_RY;
    public final SimpleAxis A_LTRIGGER;
    public final SimpleAxis A_RTRIGGER;

    public final SimpleButton B_A;
    public final SimpleButton B_B;
    public final SimpleButton B_X;
    public final SimpleButton B_Y;
    public final SimpleButton B_LBUMPER;
    public final SimpleButton B_RBUMPER;
    public final SimpleButton B_BACK;
    public final SimpleButton B_START;
    public final SimpleButton B_LJOY;
    public final SimpleButton B_RJOY;

    private final Map<String, Axis> axes = new HashMap<>();
    private final Map<String, Button> buttons = new HashMap<>();

    public LF310Gamepad(GenericHID hid) {
        super(hid, LF310Gamepad.class.getSimpleName());

        A_LX = new SimpleAxis(hid, 0);
        A_LY = new SimpleAxis(hid, 1);
        A_RX = new SimpleAxis(hid, 4);
        A_RY = new SimpleAxis(hid, 5);
        A_LTRIGGER = new SimpleAxis(hid, 2);
        A_RTRIGGER = new SimpleAxis(hid, 3);

        B_A = new SimpleButton(hid, 1);
        B_B = new SimpleButton(hid, 2);
        B_X = new SimpleButton(hid, 3);
        B_Y = new SimpleButton(hid, 4);
        B_LBUMPER = new SimpleButton(hid, 5);
        B_RBUMPER = new SimpleButton(hid, 6);
        B_BACK = new SimpleButton(hid, 7);
        B_START = new SimpleButton(hid, 8);
        B_LJOY = new SimpleButton(hid, 9);
        B_RJOY = new SimpleButton(hid, 10);

        for (Axis a : new Axis[] {A_LX, A_LY, A_RX, A_RY, A_LTRIGGER, A_RTRIGGER}) {
            axes.put(a.getAxisName(), a);
        }
        for (Button b : new Button[] {B_A, B_B, B_X, B_Y, B_LBUMPER, B_RBUMPER, B_BACK, B_START, B_LJOY, B_RJOY}) {
            buttons.put(b.getButtonName(), b);
        }
    }

    @Override
    Map<String, Axis> getAxesNamesMap() {
        return axes;
    }

    @Override
    Map<String, Button> getButtonsNamesMap() {
        return buttons;
    }
}
