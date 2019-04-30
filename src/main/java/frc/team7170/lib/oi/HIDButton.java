package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;

import java.util.Objects;

/**
 * A {@link TriggerButton TriggerButton} that retrieves its value from a single
 * {@linkplain GenericHID#getRawButton(int) button} on a {@link GenericHID GenericHID}.
 *
 * @author Robert Russell
 */
public class HIDButton extends TriggerButton {

    private final GenericHID hid;
    private final int port;

    /**
     * Construct a new {@code HIDButton} with an explicit name.
     *
     * @param hid the {@link GenericHID GenericHID} to get the {@linkplain GenericHID#getRawButton(int) button value}
     *            from.
     * @param port the button number.
     * @param name the name of the {@code HIDButton}.
     * @throws NullPointerException if the given name or the given {@code GenericHID} is {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public HIDButton(GenericHID hid, int port, String name) {
        super(name);
        /* This sanity check means a controller must be plugged in for the robot to be on, which is a bit silly.
        if (port > hid.getButtonCount()) {
            throw new IllegalArgumentException("button port number exceeds the number of buttons on given HID");
        }
        */
        this.hid = Objects.requireNonNull(hid, "hid must be non-null");
        this.port = port;
    }

    /**
     * Construct a new {@code HIDButton} with the name set to the button number.
     *
     * @param hid the {@link GenericHID GenericHID} to get the {@linkplain GenericHID#getRawButton(int) button value}
     *            from.
     * @param port the button number.
     * @throws NullPointerException if the given {@code GenericHID} is {@code null}.
     * @throws IllegalArgumentException if the derived name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name} (i.e. if the global naming rules disallow integer names).
     */
    public HIDButton(GenericHID hid, int port) {
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

    /**
     * Get the button number used by this {@code HIDButton}.
     *
     * @return the button number.
     */
    public int getButtonNumber() {
        return port;
    }

    /**
     * Get the {@link GenericHID GenericHID} used by this {@code HIDButton}.
     *
     * @return the {@link GenericHID GenericHID}.
     */
    public GenericHID getHid() {
        return hid;
    }
}
