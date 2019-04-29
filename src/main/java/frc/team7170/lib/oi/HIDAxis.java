package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;
import frc.team7170.lib.Name;

/**
 * TODO: currently here
 */
public class HIDAxis extends ScaledAxis {

    private final GenericHID hid;
    private final int port;

    public HIDAxis(GenericHID hid, int port, String name) {
        super(name);
        /*
        if (port >= hid.getAxisCount()) {
            throw new IllegalArgumentException("axis port number exceeds the number of axes on given HID");
        }
        */
        this.hid = hid;
        this.port = port;
    }

    public HIDAxis(GenericHID hid, int port) {
        this(hid, port, String.valueOf(port));
    }

    @Override
    public double getRaw() {
        return hid.getRawAxis(port);
    }

    public int getPort() {
        return port;
    }

    public GenericHID getHid() {
        return hid;
    }
}
