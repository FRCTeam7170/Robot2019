package frc.team7170.lib.oi;

/**
 * <p>
 * An action which requires a single input from an {@link Axis Axis}. For example, one might have {@code AxisAction}s
 * for driving the left side of a robot, driving the right side of a robot, an elevator's vertical movement, etc.
 * </p>
 * <p>
 * In most cases, this should be implemented only by an {@code enum} containing <em>all</em> the {@code AxisAction}s for
 * an entire robot program. Continuing the example above, one might have:
 * <pre>{@code
 * public enum AxisActions implements AxisAction {
 *     DRIVE_L,
 *     DRIVE_R,
 *     ELEVATOR;
 *     // etc.
 * }
 * }</pre>
 * When this technique is used, the interface contract is already satisfied by {@link Enum Enum}.
 * </p>
 *
 * @author Robert Russell
 * @see Axis
 * @see KeyMap
 * @see KeyBindings
 */
public interface AxisAction {

    /**
     * Get the name of this {@code AxisAction}. This method is named such that it is automatically
     * {@link Enum#name() satisfied} if an {@code enum} implements this interface.
     *
     * @return the name of this {@code AxisAction}.
     */
    String name();
}
