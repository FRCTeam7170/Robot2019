package frc.team7170.lib.oi;

/**
 * <p>
 * An action which requires a single input from a {@link Button Button}. For example, one might have
 * {@code ButtonAction}s for triggering a shooter, toggling between gearbox ratios, commanding a PID-controlled
 * elevator to raise to a certain height, etc.
 * </p>
 * <p>
 * In most cases, this should be implemented only by an {@code enum} containing <em>all</em> the {@code ButtonAction}s
 * for an entire robot program. Continuing the example above, one might have:
 * <pre>{@code
 * public enum ButtonActions implements ButtonAction {
 *     SHOOT,
 *     SHIFT,
 *     ELEVATOR_LEVEL_1;
 *     // etc.
 * }
 * }</pre>
 * When this technique is used, the interface contract is already satisfied by {@link Enum Enum}.
 * </p>
 *
 * @author Robert Russell
 * @see Button
 * @see KeyMap
 * @see KeyBindings
 */
public interface ButtonAction {

    /**
     * Get the name of this {@code ButtonAction}. This method is named such that it is automatically
     * {@link Enum#name() satisfied} if an {@code enum} implements this interface.
     *
     * @return the name of this {@code ButtonAction}.
     */
    String name();
}
