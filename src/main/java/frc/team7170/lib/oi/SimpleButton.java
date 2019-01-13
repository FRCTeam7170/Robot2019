package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;


public class SimpleButton extends Button {

    private final int port;
    private final GenericHID hid;

    public SimpleButton(GenericHID hid, int port, String name) {
        super(name);
        if (port > hid.getButtonCount()) {
            throw new IllegalArgumentException("button port number exceeds the number of buttons on given HID");
        }
        this.hid = hid;
        this.port = port;
    }

    public SimpleButton(GenericHID hid, int port) {
        this(hid, port, String.valueOf(port));
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
