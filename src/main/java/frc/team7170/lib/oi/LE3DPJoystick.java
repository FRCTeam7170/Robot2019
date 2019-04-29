package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;
import frc.team7170.lib.Name;

import java.util.HashMap;
import java.util.Map;

// TODO: make name unique depending on port number so multiple of these can be used at once (do the same with gamepad)
public final class LE3DPJoystick extends HIDController {

    public final HIDAxis A_X;
    public final HIDAxis A_Y;
    public final HIDAxis A_Z;
    public final HIDAxis A_TWIST;
    public final HIDAxis A_THROTTLE;

    public final HIDButton B_1;
    public final HIDButton B_2;
    public final HIDButton B_3;
    public final HIDButton B_4;
    public final HIDButton B_5;
    public final HIDButton B_6;
    public final HIDButton B_7;
    public final HIDButton B_8;
    public final HIDButton B_9;
    public final HIDButton B_10;
    public final HIDButton B_11;
    public final HIDButton B_12;
    public final HIDButton B_TRIGGER;
    public final HIDButton B_THUMB;

    public final POVButton POV0;
    public final POVButton POV45;
    public final POVButton POV90;
    public final POVButton POV135;
    public final POVButton POV180;
    public final POVButton POV225;
    public final POVButton POV270;
    public final POVButton POV315;

    private final POVButton.POVButtonPoller povButtonPoller;

    private final Map<String, Axis> axes = new HashMap<>();
    private final Map<String, Button> buttons = new HashMap<>();

    public LE3DPJoystick(GenericHID hid) {
        super(hid, new Name("LE3DPJoystick"));

        A_X = new HIDAxis(hid, 0);
        A_Y = new HIDAxis(hid, 1);
        A_TWIST = A_Z = new HIDAxis(hid, 2);
        A_THROTTLE = new HIDAxis(hid, 3);

        B_TRIGGER = B_1 = new HIDButton(hid, 1);
        B_THUMB = B_2 = new HIDButton(hid, 2);
        B_3 = new HIDButton(hid, 3);
        B_4 = new HIDButton(hid, 4);
        B_5 = new HIDButton(hid, 5);
        B_6 = new HIDButton(hid, 6);
        B_7 = new HIDButton(hid, 7);
        B_8 = new HIDButton(hid, 8);
        B_9 = new HIDButton(hid, 9);
        B_10 = new HIDButton(hid, 10);
        B_11 = new HIDButton(hid, 11);
        B_12 = new HIDButton(hid, 12);

        POV0 = new POVButton(hid, POVButton.POVAngle.A0);
        POV45 = new POVButton(hid, POVButton.POVAngle.A45);
        POV90 = new POVButton(hid, POVButton.POVAngle.A90);
        POV135 = new POVButton(hid, POVButton.POVAngle.A135);
        POV180 = new POVButton(hid, POVButton.POVAngle.A180);
        POV225 = new POVButton(hid, POVButton.POVAngle.A225);
        POV270 = new POVButton(hid, POVButton.POVAngle.A270);
        POV315 = new POVButton(hid, POVButton.POVAngle.A315);

        povButtonPoller = new POVButton.POVButtonPoller(
                hid, 0,
                POV0, POV45, POV90, POV135, POV180, POV225, POV270, POV315
        );

        for (Axis a : new Axis[] {A_X, A_Y, A_Z, A_THROTTLE}) {
            axes.put(a.getName(), a);
        }
        for (Button b : new Button[] {B_1, B_2, B_3, B_4, B_5, B_6, B_7, B_8, B_9, B_10, B_11, B_12,
                POV0, POV45, POV90, POV135, POV180, POV225, POV270, POV315}) {
            buttons.put(b.getName(), b);
        }
    }

    public LE3DPJoystick(GenericHID hid, boolean centerThrottleRange) {
        this(hid);
        if (centerThrottleRange) {
            centerThrottleRange();
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

    public void centerThrottleRange() {
        A_THROTTLE.setScale(2.0);
        A_THROTTLE.setOffset(-1.0);
    }

    public void resetThrottleRange() {
        A_THROTTLE.resetModifiers();
    }
}
