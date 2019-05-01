package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;
import frc.team7170.lib.math.CalcUtil;
import frc.team7170.lib.Name;

import java.util.HashMap;
import java.util.Map;

public final class LF310Gamepad extends RumbleController {

    private static final double ZERO_THRESHOLD = 0.01;

    public final HIDAxis A_LX;
    public final HIDAxis A_LY;
    public final HIDAxis A_RX;
    public final HIDAxis A_RY;
    public final HIDAxis A_LTRIGGER;
    public final HIDAxis A_RTRIGGER;
    public final ScaledAxis A_TRIGGERS = new ScaledAxis("TRIGGERS") {
        @Override
        public double getRaw() {
            double left = A_LTRIGGER.get();
            double right = A_RTRIGGER.get();
            boolean leftIsZero = CalcUtil.inThreshold(left, 0, ZERO_THRESHOLD);
            boolean rightIsZero = CalcUtil.inThreshold(right, 0, ZERO_THRESHOLD);
            if (!leftIsZero && !rightIsZero) {
                return 0.0;
            } else if (leftIsZero) {
                return right;
            }
            return -left;
        }
    };

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

    public final POVButton POV0;
    public final POVButton POV45;
    public final POVButton POV90;
    public final POVButton POV135;
    public final POVButton POV180;
    public final POVButton POV225;
    public final POVButton POV270;
    public final POVButton POV315;

    private final Map<String, Axis> axes = new HashMap<>();
    private final Map<String, Button> buttons = new HashMap<>();

    public LF310Gamepad(GenericHID hid) {
        super(hid, new Name(String.format("LF310Gamepad(%d)", hid.getPort())));

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

        Map<POVButton.POVAngle, POVButton> povButtonMap = POVButton.newButtonsWithPoller(hid, 0);
        POV0 = povButtonMap.get(POVButton.POVAngle.A0);
        POV45 = povButtonMap.get(POVButton.POVAngle.A45);
        POV90 = povButtonMap.get(POVButton.POVAngle.A90);
        POV135 = povButtonMap.get(POVButton.POVAngle.A135);
        POV180 = povButtonMap.get(POVButton.POVAngle.A180);
        POV225 = povButtonMap.get(POVButton.POVAngle.A225);
        POV270 = povButtonMap.get(POVButton.POVAngle.A270);
        POV315 = povButtonMap.get(POVButton.POVAngle.A315);

        for (Axis a : new Axis[] {A_LX, A_LY, A_RX, A_RY, A_LTRIGGER, A_RTRIGGER}) {
            axes.put(a.getName(), a);
        }
        for (Button b : new Button[] {B_A, B_B, B_X, B_Y, B_LBUMPER, B_RBUMPER, B_BACK, B_START, B_LJOY, B_RJOY,
                POV0, POV45, POV90, POV135, POV180, POV225, POV270, POV315}) {
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
