package frc.team7170.lib;

public class Name {

    public static final Name UNNAMED = new Name("");

    private final String name;

    public Name(String name) {
        if (!isValidName(name)) {
            throw new RuntimeException(String.format("invalid name '%s'", name));
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected boolean isValidName(String name) {
        return isValidNameDefault(name);
    }

    public static boolean isValidNameDefault(String name) {
        return !name.matches("[^a-zA-Z0-9_]");
    }

    public static void assertValidName(String name) {
        if (!isValidNameDefault(name)) {
            throw new RuntimeException(String.format("invalid name '%s'", name));
        }
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
