package frc.team7170.lib;

/**
 * Anything that logically has a name.
 *
 * @implSpec In most cases, singleton implementors should simply override {@link Named#getName() getName} to return a
 * literal {@code String} that has already been verified to be a valid name according to the global naming rules set out
 * in {@link Name Name}. Non-singleton implementors should either accept a {@code Name} upon construction, in which case
 * no manual verification is required, or accept a {@link String String} upon construction, in which case the
 * implementor must {@linkplain Name#requireValidName(String) manually check the validity} of the given {@code String}.
 *
 * @author Robert Russell
 * @see Name
 */
// TODO: make BaseNamed class for Named base classes to inherit from
public interface Named {

    /**
     * Get the name as a {@link String String}.
     *
     * @return the name as a {@link String String}.
     */
    String getName();

    /**
     * Get the name as a {@link Name Name} object.
     *
     * @implSpec The default implementation constructs a new {@code Name} using the {@link String String} representation
     * of the name from {@link Named#getName() getName}.
     *
     * @return the name as a {@link Name Name} object.
     */
    default Name getNameObject() {
        return new Name(getName());
    }
}
