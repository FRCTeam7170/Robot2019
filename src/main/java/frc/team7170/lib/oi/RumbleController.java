package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Notifier;
import frc.team7170.lib.Name;

public abstract class RumbleController extends HIDController {

    private double leftRumble = 0.0;
    private double rightRumble = 0.0;
    private Notifier leftRumbleNotifier;
    private Notifier rightRumbleNotifier;

    public RumbleController(GenericHID hid, Name name) {
        super(hid, name);
    }

    public void setLeftRumble(double value) {
        if (leftRumbleNotifier != null) {
            leftRumbleNotifier.close();
        }
        leftRumble = value;
        getHid().setRumble(GenericHID.RumbleType.kLeftRumble, value);
    }

    public void setRightRumble(double value) {
        if (rightRumbleNotifier != null) {
            rightRumbleNotifier.close();
        }
        rightRumble = value;
        getHid().setRumble(GenericHID.RumbleType.kRightRumble, value);
    }

    public void setRumble(double value) {
        setLeftRumble(value);
        setRightRumble(value);
    }

    public void disableRumble() {
        setRumble(0.0);
    }

    public void pulseLeftRumble(double value, int ms) {
        if (leftRumbleNotifier == null) {
            leftRumbleNotifier = new Notifier(() -> setLeftRumble(0.0));
        }
        setLeftRumble(value);
        leftRumbleNotifier.startSingle((double) ms / 1000);
    }

    public void pulseRightRumble(double value, int ms) {
        if (rightRumbleNotifier == null) {
            rightRumbleNotifier = new Notifier(() -> setRightRumble(0.0));
        }
        setRightRumble(value);
        rightRumbleNotifier.startSingle((double) ms / 1000);
    }

    public void pulseRumble(double value, int ms) {
        pulseLeftRumble(value, ms);
        pulseRightRumble(value, ms);
    }

    public double getLeftRumble() {
        return leftRumble;
    }

    public double getRightRumble() {
        return rightRumble;
    }
}
