package frc.team7170.lib.data;

import frc.team7170.lib.Name;
import frc.team7170.lib.Pair;
import frc.team7170.lib.data.property.Property;

import java.util.*;
import java.util.function.Predicate;

public class DefaultPropertyGroup<P extends Property> implements PropertyGroup<P> {

    private final String name;
    private final PropertyGroup<P> parent;
    private final List<PropertyGroup<P>> children = new ArrayList<>();
    private final List<P> properties = new ArrayList<>();

    public DefaultPropertyGroup(String name) {
        this(name, null);
    }

    private DefaultPropertyGroup(String name, PropertyGroup<P> parent) {
        this.name = Name.requireValidName(name);
        this.parent = parent;
    }

    @Override
    public PropertyGroup<P> newSubGroup(String name) {
        PropertyGroup<P> propertyGroup = new DefaultPropertyGroup<>(
                Objects.requireNonNull(name, "sub-group name must be non-null")
        );
        children.add(propertyGroup);
        return propertyGroup;
    }

    @Override
    public List<PropertyGroup<P>> getSubGroups() {
        return List.copyOf(children);
    }

    @Override
    public boolean removeSubGroup(String name) {
        Objects.requireNonNull(name, "sub-group name must be non-null");
        return removeFirstIf(children, subgroup -> subgroup.getName().equals(name));
    }

    @Override
    public PropertyGroup<P> getParentGroup() {
        return parent;
    }

    @Override
    public void addProperty(P property) {
        properties.add(Objects.requireNonNull(property, "property must be non-null"));
    }

    @Override
    public List<P> getProperties() {
        return List.copyOf(properties);
    }

    @Override
    public boolean removeProperty(String name) {
        Objects.requireNonNull(name, "property name must be non-null");
        return removeFirstIf(properties, property -> property.getName().equals(name));
    }

    private class PropertyGroupIterator implements Iterator<Pair<PropertyGroup<P>, P>> {

        private class ThisPairIterator implements Iterator<Pair<PropertyGroup<P>, P>> {

            private final Iterator<P> propertyIterator = properties.iterator();

            @Override
            public boolean hasNext() {
                return propertyIterator.hasNext();
            }

            @Override
            public Pair<PropertyGroup<P>, P> next() {
                return new Pair<>(DefaultPropertyGroup.this, propertyIterator.next());
            }
        }

        private final Iterator<PropertyGroup<P>> subgroupIterator = children.iterator();
        private Iterator<Pair<PropertyGroup<P>, P>> currentPairIterator = new ThisPairIterator();

        private PropertyGroupIterator() {
            checkAndPreparePairIterator();
        }

        @Override
        public boolean hasNext() {
            return currentPairIterator.hasNext();
        }

        @Override
        public Pair<PropertyGroup<P>, P> next() {
            Pair<PropertyGroup<P>, P> next = currentPairIterator.next();
            checkAndPreparePairIterator();
            return next;
        }

        private void checkAndPreparePairIterator() {
            while (!currentPairIterator.hasNext() && subgroupIterator.hasNext()) {
                currentPairIterator = subgroupIterator.next().iterator();
            }
        }
    }

    @Override
    public Iterator<Pair<PropertyGroup<P>, P>> iterator() {
        return new PropertyGroupIterator();
    }

    @Override
    public String getName() {
        return name;
    }

    // TODO: any other useful collections util functions used elsewhere that fit nice in a CollectionsUtils class?
    private static <E> boolean removeFirstIf(Collection<E> collection, Predicate<E> condition) {
        for (Iterator<E> iterator = collection.iterator(); iterator.hasNext();) {
            if (condition.test(iterator.next())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
