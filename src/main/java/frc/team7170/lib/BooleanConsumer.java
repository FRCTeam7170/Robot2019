package frc.team7170.lib;

/**
 * A {@link FunctionalInterface FunctionalInterface} that returns {@code void} and accepts a single primitive
 * {@code boolean} value.
 *
 * @apiNote For some reason, no equivalent interface is provided in the standard Java libraries even though a
 * complementary {@link java.util.function.BooleanSupplier BooleanSupplier} functional interface is.
 *
 * @author Robert Russell
 */
@FunctionalInterface
public interface BooleanConsumer {

    /**
     * Accepts a value.
     *
     * @param value the value to accept.
     */
    void accept(boolean value);
}
