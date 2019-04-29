package frc.team7170.lib.data;

import frc.team7170.lib.Named;
import frc.team7170.lib.Pair;
import frc.team7170.lib.data.property.Property;

import java.util.*;

/**
 * A nestable container for {@linkplain Property properties}. {@code PropertyGroup}s are {@link Named Named} to
 * facilitate the common use case of making hierarchies of properties. When making hierarchical structures, each
 * {@code PropertyGroup} may have only one parent but many children. {@code PropertyGroup}s are also
 * {@link Iterable Iterable} for convenient iteration through all the contained properties.
 *
 * @apiNote This interface is generic so that whether {@code PropertyGroup}s accept
 * {@linkplain frc.team7170.lib.data.property.RProperty readable},
 * {@linkplain frc.team7170.lib.data.property.WProperty writable}, or readable and writable properties is controllable.
 *
 * @param <P> the type of property contained in this {@code PropertyGroup}.
 *
 * @author Robert Russell
 * @see Property
 */
public interface PropertyGroup<P extends Property> extends Named, Iterable<Pair<PropertyGroup<P>, P>> {

    /**
     * Construct and return a new {@code PropertyGroup} with this {@code PropertyGroup} as the parent.
     *
     * @param name the name to apply to the new {@code PropertyGroup}.
     * @return the new {@code PropertyGroup}.
     * @throws NullPointerException if the given name is {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    PropertyGroup<P> newSubGroup(String name);

    /**
     * Merge all the {@link Property properties} and sub-groups contained within the given {@code PropertyGroup} into
     * this {@code PropertyGroup}. The given {@code PropertyGroup} and its sub-groups are not changed.
     *
     * @param group the {@code PropertyGroup} to merge into this one.
     * @throws NullPointerException if the given {@code PropertyGroup} is {@code null}.
     */
    default void merge(PropertyGroup<P> group) {
        Objects.requireNonNull(group, "cannot merge in null group");
        // Merge each property into this group.
        group.getProperties().forEach(this::addProperty);
        // Merge each sub-group into this group.
        for (PropertyGroup<P> subgroup : group.getSubGroups()) {
            // Note we create a new group whose parent is this group, then recursively populate it. We could not simply
            // transfer ownership of each sub-group of the given group to this group because the parent of groups is
            // immutable and we specify that the given groups and its sub-groups will not change as a result of this
            // operation.
            newSubGroup(subgroup.getName()).merge(subgroup);
        }
    }

    /**
     * Get the sub-groups contained within this {@code PropertyGroup}. This only includes the sub-groups of this
     * {@code PropertyGroup}, not those nested in child {@code PropertyGroup}s.
     *
     * @implSpec This should return a <em>new</em> {@code List}, not that {@code List} which may or may not be used
     * internally to store this {@code PropertyGroup}'s sub-groups.
     *
     * @return the sub-groups contained within this {@code PropertyGroup}.
     */
    List<PropertyGroup<P>> getSubGroups();

    /**
     * Remove the sub-group with the given name from this {@code PropertyGroup}.
     *
     * @param name the name of the sub-group to remove.
     * @return whether or not a sub-group of the given name was found and removed.
     * @throws NullPointerException if the given name is {@code null}.
     */
    boolean removeSubGroup(String name);

    /**
     * Get the parent {@code PropertyGroup} of this {@code PropertyGroup}, or {@code null} if this {@code PropertyGroup}
     * has no parent.
     *
     * @return the parent {@code PropertyGroup} of this {@code PropertyGroup}, or {@code null} if this
     * {@code PropertyGroup} has no parent.
     */
    PropertyGroup<P> getParentGroup();

    /**
     * Add a {@link Property Property} to this {@code PropertyGroup}.
     *
     * @param property the {@code Property} to add to this {@code PropertyGroup}.
     * @throws NullPointerException if {@code property} is {@code null}.
     */
    void addProperty(P property);

    /**
     * Get the {@linkplain Property properties} contained within this {@code PropertyGroup}. This only includes the
     * top-level properties, not those nested in child {@code PropertyGroup}s.
     *
     * @implSpec This should return a <em>new</em> {@code List}, not that {@code List} which may or may not be used
     * internally to store this {@code PropertyGroup}'s properties.
     *
     * @return the {@linkplain Property properties} contained within this {@code PropertyGroup}.
     */
    List<P> getProperties();

    /**
     * Remove the {@link Property Property} with the given name from this {@code PropertyGroup}.
     *
     * @param name the name of the {@code Property} to remove.
     * @return whether or not a {@code Property} of the given name was found and removed.
     * @throws NullPointerException if the given name is {@code null}.
     */
    boolean removeProperty(String name);

    /**
     * <p>
     * Get the {@link List List} of names of this {@code PropertyGroup}'s ancestors and this {@code PropertyGroup}
     * itself in order.
     * </p>
     * <p>
     * For example, if this {@code PropertyGroup} has name "C", it's parent has name "B", and it's grandparent--the
     * top-level ancestor--has name "A", then this returns the list {@code [A, B, C]}.
     * </p>
     *
     * @return the {@link List List} of names of this {@code PropertyGroup}'s ancestors and this {@code PropertyGroup}
     * itself in order.
     */
    default List<String> getLineage() {
        // Use LinkedList for fast head insertion.
        LinkedList<String> lineage = new LinkedList<>();
        PropertyGroup<P> propertyGroup = this;
        while (propertyGroup != null) {
            lineage.addFirst(propertyGroup.getName());
            propertyGroup = propertyGroup.getParentGroup();
        }
        return lineage;
    }

    /**
     * Get an iterator over all the {@link Pair Pair}s of {@linkplain Property properties} contained in this
     * {@code PropertyGroup} and its sub {@code PropertyGroup}s and the parent {@code PropertyGroup} of each of those
     * properties.
     *
     * @return an iterator.
     */
    @Override
    Iterator<Pair<PropertyGroup<P>, P>> iterator();
}
