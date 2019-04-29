package frc.team7170.lib.data.property;

import frc.team7170.lib.Name;
import frc.team7170.lib.data.ValueType;

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
    private final ValueType type;

    /**
     * @param name the name of the property. Cannot be null.
     * @param type the type of the property. Cannot be null.
     * @throws NullPointerException if either of {@code name} or {@code type} are {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link Name Name}.
     */
    BaseProperty(String name, ValueType type) {
        this.name = Name.requireValidName(name);
        this.type = Objects.requireNonNull(type, "properties' type must be non-null");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ValueType getType() {
        return type;
    }
}
