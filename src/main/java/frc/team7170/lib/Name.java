package frc.team7170.lib;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * A container for a single {@link String String} that represents a name. Additionally, {@code Name} exposes some static
 * methods for checking the validity of names in the form of {@code String String}s. In either case, artificial
 * restrictions are placed on the form of a name and what characters a name can contain according to a RegEx
 * {@link Pattern Pattern} so that all names used throughout an application are forced to be--to some degree--
 * consistent. The exact restrictions placed on names can be customized through
 * {@link Name#setValidNamePattern(Pattern)} or {@link Name#setValidNamePattern(String)}.
 *
 * TODO document default name rules here and what name rules are required for spooky lib to not fail at runtime
 *
 * @author Robert Russell
 * @see Named
 */
public final class Name {

    /**
     * The global RegEx {@link Pattern Pattern} describing what constitutes a valid {@code Name}. By default, a valid
     * name is any sequence without any whitespace and without the following characters: TODO
     * This definition can be
     * customized through {@link Name#setValidNamePattern(Pattern)} or {@link Name#setValidNamePattern(String)}. Access
     * is synchronized on {@code Name.class}.
     */
    private static Pattern validNamePattern = Pattern.compile("^(?:[^\\\\/,:;|'\"](?!\\s))*$");

    /**
     * Whether the {@link Name#validNamePattern validNamePattern} has been used already or not. This flag is used to
     * warn the user if they globally change what constitutes a valid name even after an older definition has been used
     * to verify one or more names already. Access is synchronized on {@code Name.class}.
     */
    private static boolean validNamePatternUsed = false;

    /**
     * The empty name.
     */
    public static final Name UNNAMED = new Name("");

    private final String name;

    /**
     * @param name the name as a {@link String String}.
     */
    public Name(String name) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException(String.format("invalid name '%s'", name));
        }
        this.name = name;
    }

    /**
     * Get the name as a {@link String String}.
     *
     * @return the name.
     */
    // TODO: remove all uses of this method in favour of toString
    @Deprecated
    public String getName() {
        return name;
    }

    /**
     * Get whether or not the given name is valid according to the
     * {@linkplain Name#validNamePattern global naming rules}.
     *
     * @param name the name to check for validity.
     * @return whether or not the given name is valid.
     * @throws NullPointerException if the given name is {@code null}.
     */
    public static synchronized boolean isValidName(String name) {
        validNamePatternUsed = true;
        return validNamePattern.matcher(Objects.requireNonNull(name, "name must be non-null")).matches();
    }

    /**
     * Require that the given name be valid according to the {@linkplain Name#validNamePattern global naming rules}.
     * This function should be used similarly to {@link Objects#requireNonNull(Object)} and the other methods of a
     * similar signature in {@link Objects Objects}.
     *
     * @param name the name to require validity on.
     * @return the given name for sake of composing calls to this function in variable assignments or other
     * function/method invocations.
     * @throws NullPointerException if the given name is {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the
     * {@linkplain Name#validNamePattern global naming rules}.
     */
    public static String requireValidName(String name) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException(String.format("invalid name '%s'", name));
        }
        return name;
    }

    /**
     * Set the {@link Name#validNamePattern validNamePattern} to the given {@link Pattern Pattern}. If a name has
     * already been verified with a previous {@code validNamePattern}, log a warning using
     * {@link java.util.logging Java util logging} since that(those) previously verified name(s) is(are) now technically
     * invalid.
     *
     * @param pattern the {@code Pattern} to use as the new {@code validNamePattern}.
     * @throws NullPointerException if the given {@code Pattern} is {@code null}.
     */
    public static synchronized void setValidNamePattern(Pattern pattern) {
        if (validNamePatternUsed) {
            // This should happen infrequently, so do not store a reference to the Logger.
            Logger.getLogger(Name.class.getName()).warning(
                    "A previous validNamePattern has already been used to validate names!"
            );
        }
        validNamePattern = Objects.requireNonNull(pattern, "pattern must be non-null");
    }

    /**
     * Set the {@link Name#validNamePattern validNamePattern} using the given pattern {@link String String}. If a name
     * has already been verified with a previous {@code validNamePattern}, log a warning using
     * {@link java.util.logging Java util logging} since that(those) previously verified name(s) is(are) now technically
     * invalid.
     *
     * @param patternString the {@code String} to use to set the new {@code validNamePattern}.
     * @throws NullPointerException if {@code patternString} is {@code null}.
     */
    public static void setValidNamePattern(String patternString) {
        // Pattern.compile does not appear to do null-checking itself.
        setValidNamePattern(Pattern.compile(
                Objects.requireNonNull(patternString, "patternString must be non-null")
        ));
    }

    /**
     * Get the underlying {@link String String} representation of this {@code Name}.
     *
     * @return the underlying {@link String String} representation of this {@code Name}.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Check whether this {@code Name} equals the given {@link Object Object}. This is true only if
     * {@code obj instanceof Name} and if the underlying name of this {@code Name} and {@code obj} are
     * {@linkplain String#equals(Object) equal}.
     *
     * @param obj the {@code Object} to check for equality against.
     * @return whether this {@code Name} equals the given {@link Object Object}.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Name)) {
            return false;
        }
        Name other = (Name) obj;
        return this.name.equals(other.name);
    }

    /**
     * Get a hash code based on the underlying {@link String String} name of this {@code Name}.
     *
     * @return a hash code based on the underlying {@link String String} name of this {@code Name}.
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
