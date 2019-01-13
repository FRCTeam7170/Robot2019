package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;

import java.util.HashMap;
import java.util.Map;


public final class LE3DPJoystick extends Controller {

    public final SimpleAxis A_X;
    public final SimpleAxis A_Y;
    public final SimpleAxis A_Z;
    public final SimpleAxis A_TWIST;
    public final SimpleAxis A_THROTTLE;

    public final SimpleButton B_1;
    public final SimpleButton B_2;
    public final SimpleButton B_3;
    public final SimpleButton B_4;
    public final SimpleButton B_5;
    public final SimpleButton B_6;
    public final SimpleButton B_7;
    public final SimpleButton B_8;
    public final SimpleButton B_9;
    public final SimpleButton B_10;
    public final SimpleButton B_11;
    public final SimpleButton B_12;
    public final SimpleButton B_TRIGGER;
    public final SimpleButton B_THUMB;

    private final Map<String, Axis> axes = new HashMap<>();
    private final Map<String, Button> buttons = new HashMap<>();

    public LE3DPJoystick(GenericHID hid) {
        super(hid, LE3DPJoystick.class.getSimpleName());

        A_X = new SimpleAxis(hid, 0);
        A_Y = new SimpleAxis(hid, 1);
        A_TWIST = A_Z = new SimpleAxis(hid, 2);
        A_THROTTLE = new SimpleAxis(hid, 3);

        B_TRIGGER = B_1 = new SimpleButton(hid, 1);
        B_THUMB = B_2 = new SimpleButton(hid, 2);
        B_3 = new SimpleButton(hid, 3);
        B_4 = new SimpleButton(hid, 4);
        B_5 = new SimpleButton(hid, 5);
        B_6 = new SimpleButton(hid, 6);
        B_7 = new SimpleButton(hid, 7);
        B_8 = new SimpleButton(hid, 8);
        B_9 = new SimpleButton(hid, 9);
        B_10 = new SimpleButton(hid, 10);
        B_11 = new SimpleButton(hid, 11);
        B_12 = new SimpleButton(hid, 12);

        for (Axis a : new Axis[] {A_X, A_Y, A_Z, A_THROTTLE}) {
            axes.put(a.getAxisName(), a);
        }
        for (Button b : new Button[] {B_1, B_2, B_3, B_4, B_5, B_6, B_7, B_8, B_9, B_10, B_11, B_12}) {
            buttons.put(b.getButtonName(), b);
        }
    }

    public LE3DPJoystick(GenericHID hid, boolean centerThrottleRange) {
        this(hid);
        if (centerThrottleRange) {
            centerThrottleRange();
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

    public void centerThrottleRange() {
        A_THROTTLE.setScale(2.0);
        A_THROTTLE.setOffset(-1.0);
    }

    public void resetThrottleRange() {
        A_THROTTLE.resetModifiers();
    }
}
