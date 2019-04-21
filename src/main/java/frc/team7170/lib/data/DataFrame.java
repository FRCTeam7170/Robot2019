package frc.team7170.lib.data;

import frc.team7170.lib.Named;
import frc.team7170.lib.Pair;
import frc.team7170.lib.data.property.Property;

import java.util.Iterator;
import java.util.List;

/**
 * A nestable container for {@linkplain Property properties}. {@code DataFrame}s are {@link Named Named} to facilitate
 * the common use case of making hierarchies of properties. When making hierarchical structures, each {@code DataFrame}
 * may have only one parent but many children. {@code DataFrame}s are also {@link Iterable Iterable} for convenient
 * iteration through all the contained properties.
 *
 * @apiNote This interface is generic so that whether {@code DataFrame}s accept
 * {@linkplain frc.team7170.lib.data.property.RProperty readable},
 * {@linkplain frc.team7170.lib.data.property.WProperty writable}, or readable and writable properties is controllable.
 *
 * @param <P> the type of property contained in this {@code DataFrame}.
 *
 * @author Robert Russell
 * @see Property
 */
public interface DataFrame<P extends Property> extends Named, Iterable<Pair<DataFrame, P>> {

    /**
     * Construct and return a new {@code DataFrame} with this {@code DataFrame} as the parent.
     *
     * @param name the name to apply to the new {@code DataFrame}.
     * @return the new {@code DataFrame}.
     * @throws NullPointerException if {@code name} is null.
     */
    DataFrame<P> newSubFrame(String name);

    /**
     * Merge all the {@link Property properties} and sub-frames contained within the given {@code DataFrame} into this
     * {@code DataFrame}. The given {@code DataFrame} and its sub-frames are not changed.
     *
     * @param frame the {@code DataFrame} to merge into this one.
     * @throws NullPointerException if {@code frame} is null.
     */
    default void merge(DataFrame<P> frame) {
        // Merge each property into this frame.
        frame.getProperties().forEach(this::addProperty);
        // Merge each sub-frame into this frame.
        for (DataFrame<P> subframe : frame.getSubFrames()) {
            // Note we create a new frame whose parent is this frame, then recursively populate it. We could not simply
            // transfer ownership of each sub-frame of the given frame to this frame because the parent of frames is
            // immutable and we specify that the given frames and its sub-frames will not change as a result of this
            // operation.
            newSubFrame(subframe.getName()).merge(subframe);
        }
    }

    /**
     * Get the sub-frames contained within this {@code DataFrame}. This only includes the sub-frames of this
     * {@code DataFrame}, not those nested in child {@code DataFrame}s.
     *
     * @implSpec This should return a <em>new</em> {@code List}, not that {@code List} which may or may not be used
     * internally to store this {@code DataFrame}'s sub-frames.
     *
     * @return the sub-frames contained within this {@code DataFrame}.
     */
    List<DataFrame<P>> getSubFrames();

    /**
     * Remove the sub-frame with the given name from this {@code DataFrame}.
     *
     * @param name the name of the sub-frame to remove.
     * @return whether or not a sub-frame of the given name was found and removed.
     * @throws NullPointerException if {@code name} is null.
     */
    boolean removeSubFrame(String name);

    /**
     * Get the parent {@code DataFrame} of this {@code DataFrame}, or null if this {@code DataFrame} has no parent.
     *
     * @return the parent {@code DataFrame} of this {@code DataFrame}, or null if this {@code DataFrame} has no parent.
     */
    DataFrame<P> getParentFrame();

    /**
     * Add a {@link Property Property} to this {@code DataFrame}.
     *
     * @param property the {@code Property} to add to this {@code DataFrame}.
     * @throws NullPointerException if {@code property} is null.
     */
    void addProperty(P property);

    /**
     * Get the {@linkplain Property properties} contained within this {@code DataFrame}. This only includes the
     * top-level properties, not those nested in child {@code DataFrame}s.
     *
     * @implSpec This should return a <em>new</em> {@code List}, not that {@code List} which may or may not be used
     * internally to store this {@code DataFrame}'s properties.
     *
     * @return the {@linkplain Property properties} contained within this {@code DataFrame}.
     */
    List<P> getProperties();

    /**
     * Remove the {@link Property Property} with the given name from this {@code DataFrame}.
     *
     * @param name the name of the {@code Property} to remove.
     * @return whether or not a {@code Property} of the given name was found and removed.
     * @throws NullPointerException if {@code name} is null.
     */
    boolean removeProperty(String name);

    /**
     * Get an iterator over all the {@link Pair Pair}s of {@linkplain Property properties} contained in this
     * {@code DataFrame} and its sub {@code DataFrame}s and the parent {@code DataFrame} of each of those properties.
     *
     * @return an iterator.
     */
    @Override
    Iterator<Pair<DataFrame, P>> iterator();
}
