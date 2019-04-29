package frc.team7170.lib.oi;

import frc.team7170.lib.Named;

/**
 * An immutable set of mappings from {@link AxisAction AxisAction}s and {@link ButtonAction ButtonAction}s to
 * {@linkplain Axis axes} and {@linkplain Button buttons}, respectively. {@code KeyMap}s support having multiple
 * bindings to the same {@code Axis} or {@code Button}, but do <em>not</em> support having multiple bindings from the
 * same {@code AxisAction} or {@code ButtonAction}. Using {@code KeyMap}s, one can define and switch between multiple
 * sets of key bindings for different scenarios or according to robot driver preference.
 *
 * @implNote All implementors of {@code KeyMap} should also provide an accompanying implementation of
 * {@link Builder Builder}. Joshua Bloch's "Effective Java" (Second Edition) Item 2 describes how to properly employ the
 * builder pattern.
 *
 * @author Robert Russell
 * @see Axis
 * @see Button
 * @see AxisAction
 * @see ButtonAction
 */
public interface KeyMap extends Named {

    /**
     * A builder for constructing instances of {@code KeyMap}s.
     *
     * @apiNote The use of a builder for creating {@code KeyMap}s is made necessary (or at lest favourable) by
     * {@code KeyMap}s being immutable.
     *
     * @param <T> the type of {@code KeyMap} built by this {@code Builder}.
     *
     * @author Robert Russell
     */
    // TODO: accept Controller obj as well in bind methods?
    interface Builder<T extends KeyMap> {

        /**
         * <p>
         * Add a binding from the given {@link AxisAction AxisAction} to the given {@link Axis Axis}.
         * </p>
         * <p>
         * If a {@code Builder} already contains a binding for the given {@code AxisAction}, the older binding is
         * silently overwritten. There is no issue with binding to an {@code Axis} which this {@code Builder} already
         * contains a binding to.
         * </p>
         *
         * @param action the {@code AxisAction} to serve as the key for this new binding.
         * @param axis the {@code Axis} to serve as the value for this new binding.
         * @return this {@code Builder} object for sake of chaining invocations of {@code bind}.
         * @throws NullPointerException if the given {@code AxisAction} <em>or</em> {@code Axis} are {@code null}.
         */
        Builder<T> bind(AxisAction action, Axis axis);

        /**
         * <p>
         * Add a binding from the given {@link ButtonAction ButtonAction} to the given {@link Button Button}.
         * </p>
         * <p>
         * If a {@code Builder} already contains a binding for the given {@code ButtonAction}, the older binding is
         * silently overwritten. There is no issue with binding to an {@code Button} which this {@code Builder} already
         * contains a binding to.
         * </p>
         *
         * @param action the {@code ButtonAction} to serve as the key for this new binding.
         * @param button the {@code Button} to serve as the value for this new binding.
         * @return this {@code Builder} object for sake of chaining invocations of {@code bind}.
         * @throws NullPointerException if the given {@code ButtonAction} <em>or</em> {@code Button} are {@code null}.
         */
        Builder<T> bind(ButtonAction action, Button button);

        /**
         * Construct and return a new instance of {@code T} (an implementation of {@code KeyMap}) with the previously
         * bound bindings.
         *
         * @return a new instance of {@code T} (an implementation of {@code KeyMap}) with the previously bound bindings.
         */
        T build();
    }

    /**
     * Get the {@link Axis Axis} associated with the given {@link AxisAction AxisAction}, or {@code null} if the given
     * {@code AxisAction} has no binding in this {@code KeyMap}.
     *
     * @param action the {@code AxisAction} whose associated {@link Axis Axis} is returned.
     * @return the {@link Axis Axis} associated with the given {@link AxisAction AxisAction}, or {@code null} if the
     * given {@code AxisAction} has no binding in this {@code KeyMap}.
     * @throws NullPointerException if the given {@code AxisAction} is {@code null}.
     */
    Axis actionToAxis(AxisAction action);

    /**
     * Get the {@link Button Button} associated with the given {@link ButtonAction ButtonAction}, or {@code null} if the
     * given {@code ButtonAction} has no binding in this {@code KeyMap}.
     *
     * @param action the {@code ButtonAction} whose associated {@link Button Button} is returned.
     * @return the {@link Button Button} associated with the given {@link ButtonAction ButtonAction}, or {@code null} if
     * the given {@code ButtonAction} has no binding in this {@code KeyMap}.
     * @throws NullPointerException if the given {@code ButtonAction} is {@code null}.
     */
    Button actionToButton(ButtonAction action);

    /**
     * Get whether or not this {@code KeyMap} contains a binding for the given {@link AxisAction AxisAction}.
     *
     * @param action the {@code AxisAction} to check for an associated binding.
     * @return whether or not this {@code KeyMap} contains a binding for the given {@link AxisAction AxisAction}.
     * @throws NullPointerException if the given {@code AxisAction} is {@code null}.
     */
    boolean hasBindingFor(AxisAction action);

    /**
     * Get whether or not this {@code KeyMap} contains a binding for the given {@link ButtonAction ButtonAction}.
     *
     * @param action the {@code ButtonAction} to check for an associated binding.
     * @return whether or not this {@code KeyMap} contains a binding for the given {@link ButtonAction ButtonAction}.
     * @throws NullPointerException if the given {@code ButtonAction} is {@code null}.
     */
    boolean hasBindingFor(ButtonAction action);
}
