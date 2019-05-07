package frc.team7170.lib.oi;

import frc.team7170.lib.Named;

/**
 * An abstraction for an axis on a {@link Controller Controller} or, more generally, a {@code double}-valued point of
 * interaction with a user.
 *
 * @author Robert Russell
 * @see Button
 * @see Controller
 * @see KeyMap
 * @see KeyBindings
 */
public interface Axis extends Named, Controllered {

    /**
     * Get the value of the {@code Axis}. This is typically a number in the range {@code [-1, 1]}.
     *
     * @return the value of the {@code Axis}. This is typically a number in the range {@code [-1, 1]}.
     */
    double get();
}
