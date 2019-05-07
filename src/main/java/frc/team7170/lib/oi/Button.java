package frc.team7170.lib.oi;

import frc.team7170.lib.Named;

/**
 * <p>
 * An abstraction for a button on a {@link Controller Controller} or, more generally, a {@code boolean}-valued point of
 * interaction with a user.
 * </p>
 * <p>
 * A note of terminology: when we say the {@code Button} is "pressed", this does not necessarily mean a physical button
 * is being pressed, as some {@code Button}s may not represent physical buttons at all; therefore, a {@code Button}
 * being "pressed" should only be taken as being "active". The same but opposite technicality applies when we say a
 * {@code Button} is "released".
 * </p>
 *
 * @author Robert Russell
 * @see Axis
 * @see Controller
 * @see KeyMap
 * @see KeyBindings
 */
public interface Button extends Named, Controllered {

    /**
     * Get the value of the {@code Button}; that is, get whether the {@code Button} is currently pressed.
     *
     * @return whether the {@code Button} is currently pressed.
     */
    boolean get();

    /**
     * Get whether or not the {@code Button} has been pressed since the last invocation of {@code getPressed} or, if
     * {@code getPressed} has not yet been called, if the {@code Button} has yet been pressed at all.
     *
     * @return whether or not the {@code Button} has been pressed since the last invocation of {@code getPressed} or, if
     * {@code getPressed} has not yet been called, if the {@code Button} has yet been pressed at all.
     */
    boolean getPressed();

    /**
     * Get whether or not the {@code Button} has been released since the last invocation of {@code getReleased} or, if
     * {@code getReleased} has not yet been called, if the {@code Button} has yet been released at all.
     *
     * @return whether or not the {@code Button} has been released since the last invocation of {@code getReleased} or,
     * if {@code getReleased} has not yet been called, if the {@code Button} has yet been released at all.
     */
    boolean getReleased();
}
