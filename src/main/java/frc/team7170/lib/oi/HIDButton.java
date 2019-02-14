package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;
import frc.team7170.lib.Name;

public class HIDButton extends TriggerButton {

    private final GenericHID hid;
    private final int port;

    public HIDButton(GenericHID hid, int port, Name name) {
        super(name);
        if (port > hid.getButtonCount()) {
            throw new IllegalArgumentException("button port number exceeds the number of buttons on given HID");
        }
        this.hid = hid;
        this.port = port;
    }

    public HIDButton(GenericHID hid, int port) {
        this(hid, port, new Name(String.valueOf(port)));
    }

    @Override
    public boolean get() {
        return hid.getRawButton(port);
    }

    @Override
    public boolean getPressed() {
        return hid.getRawButtonPressed(port);
    }

    @Override
    public boolean getReleased() {
        return hid.getRawButtonReleased(port);
    }

    public int getPort() {
        return port;
    }

    public GenericHID getHid() {
        return hid;
    }
}
