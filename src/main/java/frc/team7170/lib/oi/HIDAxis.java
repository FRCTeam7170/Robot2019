package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;

import java.util.Objects;

/**
 * A {@link ScaledAxis ScaledAxis} that retrieves its value from a single {@link GenericHID#getRawAxis(int) axis} on a
 * {@link GenericHID GenericHID}.
 *
 * @author Robert Russell
 */
// TODO: fix docs with controller arg
public class HIDAxis extends ScaledAxis {

    private final GenericHID hid;
    private final int port;

    /**
     * Construct a new {@code HIDAxis} with an explicit name.
     *
     * @param hid the {@link GenericHID GenericHID} to get the {@link GenericHID#getRawAxis(int) axis value} from.
     * @param port the axis number.
     * @param name the name of the {@code HIDAxis}.
     * @throws NullPointerException if the given name or the given {@code GenericHID} is {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public HIDAxis(GenericHID hid, int port, Controller controller, String name) {
        super(controller, name);
        /* This sanity check means a controller must be plugged in for the robot to be on, which is a bit silly.
        if (port >= hid.getAxisCount()) {
            throw new IllegalArgumentException("axis port number exceeds the number of axes on given HID");
        }
        */
        this.hid = Objects.requireNonNull(hid, "hid must be non-null");
        this.port = port;
    }

    /**
     * Construct a new {@code HIDAxis} with the name set to the axis number.
     *
     * @param hid the {@link GenericHID GenericHID} to get the {@link GenericHID#getRawAxis(int) axis value} from.
     * @param port the axis number.
     * @throws NullPointerException if the given {@code GenericHID} is {@code null}.
     * @throws IllegalArgumentException if the derived name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name} (i.e. if the global naming rules disallow integer names).
     */
    public HIDAxis(GenericHID hid, int port, Controller controller) {
        this(hid, port, controller, String.valueOf(port));
    }

    @Override
    public double getRaw() {
        return hid.getRawAxis(port);
    }

    /**
     * Get the axis number used by this {@code HIDAxis}.
     *
     * @return the axis number.
     */
    public int getAxisNumber() {
        return port;
    }

    /**
     * Get the {@link GenericHID GenericHID} used by this {@code HIDAxis}.
     *
     * @return the {@link GenericHID GenericHID}.
     */
    public GenericHID getHid() {
        return hid;
    }
}
