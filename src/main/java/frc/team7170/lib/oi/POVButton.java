package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Notifier;
import frc.team7170.lib.Name;

public class POVButton extends TriggerButton {

    private static final int pollRateMs = 1;

    public enum POVAngle {
        A0(0),
        A45(0),
        A90(0),
        A135(0),
        A180(0),
        A225(0),
        A270(0),
        A315(0);

        private final int angle;

        POVAngle(int angle) {
            this.angle = angle;
        }
    }

    private final Notifier notifier = new Notifier(this::pollPOV);
    private final GenericHID hid;
    private final POVAngle angle;
    private boolean pressed = false;
    private boolean released = false;
    private boolean pressedLastIter = false;

    public POVButton(GenericHID hid, POVAngle angle, Name name) {
        super(name);
        this.hid = hid;
        this.angle = angle;
        notifier.startPeriodic((double) pollRateMs / 1000.0);
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

    private void pollPOV() {
        boolean isCurrentlyPressed = get();
        if (isCurrentlyPressed && !pressedLastIter) {
            pressed = true;
        } else if (!isCurrentlyPressed && pressedLastIter) {
            released = true;
        }
        pressedLastIter = isCurrentlyPressed;
    }

    public GenericHID getHid() {
        return hid;
    }

    public POVAngle getPOVAngle() {
        return angle;
    }
}
