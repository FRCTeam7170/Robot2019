package frc.team7170.lib.data.property;

import java.util.Objects;

/**
 * The abstract base class used for the properties returned in {@link PropertyFactory PropertyFactory}.
 *
 * @implNote This class is not strictly abstract (it fulfills the {@link Property Property} interface and has no
 * abstract methods), but it would be useless to instantiate.
 *
 * @author Robert Russell
 * @see Property
 * @see PropertyFactory
 */
abstract class BaseProperty implements Property {

    private final String name;
    private final PropertyType type;

    /**
     * @param name the name of the property. Cannot be null.
     * @param type the type of the property. Cannot be null.
     * @throws NullPointerException if either of {@code name} or {@code type} are {@code null}.
     */
    BaseProperty(String name, PropertyType type) {
        this.name = Objects.requireNonNull(name, "properties' name must be non-null");
        this.type = Objects.requireNonNull(type, "properties' type must be non-null");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PropertyType getType() {
        return type;
    }
}
