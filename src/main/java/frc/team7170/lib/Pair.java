package frc.team7170.lib;

/**
 * A generic pair of values. Null values are permitted.
 *
 * @param <L> the type of the "left"/first value.
 * @param <R> the type of the "right"/second value.
 *
 * @author Robert Russell
 */
public class Pair<L, R> {

    private final L left;
    private final R right;

    /**
     * @param left the left value. Can be null.
     * @param right the right value. Can be null.
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Get the left value.
     * @return the left value.
     */
    public L getLeft() {
        return left;
    }

    /**
     * Get the right value.
     * @return the right value.
     */
    public R getRight() {
        return right;
    }

    /**
     * Get the hash code of this {@code Pair}. The hash code of {@code Pair}s is defined to be the left/first value XOR
     * the right/second value.
     * @return the hash code of this {@code Pair}
     */
    @Override
    public int hashCode() {
        return left.hashCode() ^ right.hashCode();
    }

    /**
     * Determine whether or not this {@code Pair} is equal to the given {@code Object}. This is true if and only if
     * {@code obj instanceof Pair} and this {@code Pair} and the given one have equal left and right values, as defined
     * in their own {@link Object#equals(Object) equals} methods.
     * @param obj the object to check for equality with this {@code Pair}.
     * @return whether or not this {@code Pair} is equal to the given {@code Object}.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair other = (Pair) obj;
        return left.equals(other.left) && right.equals(other.right);
    }

    @Override
    public String toString() {
        return String.format("<%s, %s>", left.toString(), right.toString());
    }
}
