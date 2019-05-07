package frc.team7170.lib.oi;

import frc.team7170.lib.Named;

import java.util.Map;

/**
 * A group of {@linkplain Axis axes} and/or {@linkplain Button buttons}. In many cases, {@code Controller}s represent
 * physical controllers (i.e. gamepads, button boards, etc.), although they do not have to.
 *
 * @author Robert Russell
 * @see Axis
 * @see Button
 */
public interface Controller extends Named {

    /**
     * Get a {@link Map Map} mapping the {@linkplain Axis#getName() names} of {@linkplain Axis axes} contained in this
     * {@code Controller} to the {@code Axis} objects themselves.
     *
     * @apiNote This method is used internally for decoding {@link SerializableKeyMap SerializableKeyMap}s.
     *
     * @implNote This should return a <em>new</em> {@code Map}, not that {@code Map} which may or may not be used
     * internally to store this {@code Controller}'s axes.
     *
     * @return a {@link Map Map} mapping the {@linkplain Axis#getName() names} of {@linkplain Axis axes} contained in
     * this {@code Controller} to the {@code Axis} objects themselves.
     */
    Map<String, Axis> getAxesNamesMap();

    /**
     * Get a {@link Map Map} mapping the {@linkplain Button#getName() names} of {@linkplain Button buttons} contained in
     * this {@code Controller} to the {@code Button} objects themselves.
     *
     * @apiNote This method is used internally for decoding {@link SerializableKeyMap SerializableKeyMap}s.
     *
     * @implNote This should return a <em>new</em> {@code Map}, not that {@code Map} which may or may not be used
     * internally to store this {@code Controller}'s buttons.
     *
     * @return a {@link Map Map} mapping the {@linkplain Button#getName() names} of {@linkplain Button buttons}
     * contained in this {@code Controller} to the {@code Button} objects themselves.
     */
    Map<String, Button> getButtonsNamesMap();
}
