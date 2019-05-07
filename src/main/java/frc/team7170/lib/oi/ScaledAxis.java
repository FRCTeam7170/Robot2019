package frc.team7170.lib.oi;

import frc.team7170.lib.Name;

/**
 * <p>
 * A base implementation of an {@link Axis Axis} that supports scaling and offsetting the {@code Axis}'s value.
 * </p>
 * <p>
 * This can be useful in situations such as with the
 * {@linkplain LE3DPJoystick Logitech Extreme 3D Pro Joystick Controller} where the "throttle" control ranges by default
 * from {@code 0} to {@code 1}; by setting the scale of the throttle to {@code 2} and the offset to {@code -1}, the
 * range can be "{@linkplain LE3DPJoystick#centerThrottleRange() centered}" (i.e. the range would be {@code [-1, 1]}).
 * </p>
 *
 * @author Robert Russell
 */
// TODO: fix docs with controller arg
public abstract class ScaledAxis extends BaseControllered implements Axis {

    private final String name;
    private double scale;
    private double offset;

    /**
     * Construct a new {@code ScaledAxis} with the given initial scale and offset.
     *
     * @param name the name of the {@code ScaledAxis}.
     * @param scale the initial scale.
     * @param offset the initial offset.
     * @throws NullPointerException if the given name is {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link Name Name}.
     */
    public ScaledAxis(Controller controller, String name, double scale, double offset) {
        super(controller);
        this.name = Name.requireValidName(name);
        this.scale = scale;
        this.offset = offset;
    }

    /**
     * Construct a new {@code ScaledAxis} with the scale and offset defaulting to {@code 1} and {@code 0}, respectively.
     *
     * @param name the name of the {@code ScaledAxis}.
     * @throws NullPointerException if the given name is {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public ScaledAxis(Controller controller, String name) {
        this(controller, name, 1.0, 0.0);
    }

    /**
     * Get the value of the {@code ScaledAxis} after the scale and offset are applied.
     *
     * @return the value of the {@code ScaledAxis} after the scale and offset are applied.
     */
    @Override
    public double get() {
        return getRaw() * scale + offset;
    }

    /**
     * Get the value of the {@code ScaledAxis} before the scale and offset are applied.
     *
     * @return the value of the {@code ScaledAxis} before the scale and offset are applied.
     */
    public abstract double getRaw();

    /**
     * Get the current scale of the {@code ScaledAxis}.
     *
     * @return the current scale of the {@code ScaledAxis}.
     */
    public double getScale() {
        return scale;
    }

    /**
     * Set the current scale of the {@code ScaledAxis}.
     *
     * @param scale the scale to set on the {@code ScaledAxis}.
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * Get the current offset of the {@code ScaledAxis}.
     *
     * @return the current offset of the {@code ScaledAxis}.
     */
    public double getOffset() {
        return offset;
    }

    /**
     * Set the current offset of the {@code ScaledAxis}.
     *
     * @param offset the offset to set on the {@code ScaledAxis}.
     */
    public void setOffset(double offset) {
        this.offset = offset;
    }

    /**
     * Reset the scale and offset on the {@code ScaledAxis} to {@code 1} and {@code 0}, respectively.
     */
    public void resetModifiers() {
        setScale(1.0);
        setOffset(0.0);
    }

    @Override
    public final String getName() {
        return name;
    }
}
