package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Notifier;
import frc.team7170.lib.Name;

public class POVButton extends TriggerButton {

    public enum POVAngle {
        A0(0),
        A45(45),
        A90(90),
        A135(135),
        A180(180),
        A225(225),
        A270(270),
        A315(315);

        private final int angle;

        POVAngle(int angle) {
            this.angle = angle;
        }
    }

    public static class POVButtonPoller {

        private static final int pollRateMs = 1;

        private final Notifier notifier = new Notifier(this::poll);
        private final GenericHID hid;
        private final int pov;
        private final POVButton button0, button45, button90, button135, button180, button225, button270, button315;
        private POVButton lastPressed = null;

        public POVButtonPoller(GenericHID hid,
                               int pov,
                               POVButton button0,
                               POVButton button45,
                               POVButton button90,
                               POVButton button135,
                               POVButton button180,
                               POVButton button225,
                               POVButton button270,
                               POVButton button315) {
            this.hid = hid;
            this.pov = pov;
            this.button0 = button0;
            this.button45 = button45;
            this.button90 = button90;
            this.button135 = button135;
            this.button180 = button180;
            this.button225 = button225;
            this.button270 = button270;
            this.button315 = button315;
            notifier.startPeriodic((double) pollRateMs / 1000.0);
        }

        private void poll() {
            POVButton pressedButton = angleToButton(hid.getPOV(pov));
            if (pressedButton != lastPressed) {
                if (lastPressed != null) {
                    lastPressed.released = true;
                }
                if (pressedButton != null) {
                    pressedButton.pressed = true;
                }
                lastPressed = pressedButton;
            }
        }

        private POVButton angleToButton(int angle) {
            switch (angle) {
                case -1:
                    return null;
                case 0:
                    return button0;
                case 45:
                    return button45;
                case 90:
                    return button90;
                case 135:
                    return button135;
                case 180:
                    return button180;
                case 225:
                    return button225;
                case 270:
                    return button270;
                case 315:
                    return button315;
                default:
                    throw new AssertionError();
            }
        }
    }

    private final GenericHID hid;
    private final POVAngle angle;
    private boolean pressed = false;
    private boolean released = false;

    public POVButton(GenericHID hid, POVAngle angle, Name name) {
        super(name);
        this.hid = hid;
        this.angle = angle;
    }

    public POVButton(GenericHID hid, POVAngle angle) {
        this(hid, angle, new Name(angle.name()));
    }

    @Override
    public boolean get() {
        return hid.getPOV() == angle.angle;
    }

    // TODO: this logic might not be consistent with WPI getPressed and getReleased... should check

    @Override
    public boolean getPressed() {
        if (pressed) {
            pressed = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean getReleased() {
        if (released) {
            released = false;
            return true;
        }
        return false;
    }

    public GenericHID getHid() {
        return hid;
    }

    public POVAngle getPOVAngle() {
        return angle;
    }
}
