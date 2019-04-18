package frc.team7170.lib.data.property;

/**
 * A readable and writable {@link Property Property}. This interface adds no new functionality; it is simply a
 * consolidation of the {@link RProperty RProperty} and {@link WProperty WProperty} interfaces.
 *
 * @author Robert Russell
 * @see Property
 * @see RProperty
 * @see WProperty
 * @see PropertyFactory
 */
public interface RWProperty extends RProperty, WProperty {}
