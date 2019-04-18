package frc.team7170.lib;

import java.util.Objects;

/**
 * A container for a single {@code String} that represents a name. This places artificial restrictions on the form of a
 * name and what characters a name can contain so that all names used throughout an application are force to be--to some
 * degree--consistent.
 * TODO: option to set global name options (must be done before any names instantiated...?)
 * TODO: allow placing restrictions on form of name
 *
 * @author Robert Russell
 * @see Named
 */
public class Name {

    /**
     * The empty name.
     */
    public static final Name UNNAMED = new Name("");

    private final String name;

    /**
     * @param name the name as a string.
     */
    public Name(String name) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException(String.format("invalid name '%s'", name));
        }
        this.name = name;
    }

    /**
     * Get the name as a string.
     * @return the name.
     */
    public String getName() {
        return name;
    }

    protected boolean isValidName(String name) {
        return isValidNameDefault(name);
    }

    public static boolean isValidNameDefault(String name) {
        return !name.matches("[^a-zA-Z0-9_]");
    }

    public static String requireValidName(String name) {
        if (!isValidNameDefault(Objects.requireNonNull(name, "name must be non-null"))) {
            throw new IllegalArgumentException(String.format("invalid name '%s'", name));
        }
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Name)) {
            return false;
        }
        Name other = (Name) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
