package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;


public class SimpleAxis extends Axis {

    private final int port;
    private final GenericHID hid;

    public SimpleAxis(GenericHID hid, int port, String name) {
        super(name);
        if (port >= hid.getAxisCount()) {
            throw new IllegalArgumentException("axis port number exceeds the number of axes on given HID");
        }
        this.hid = hid;
        this.port = port;
    }

    public SimpleAxis(GenericHID hid, int port) {
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
