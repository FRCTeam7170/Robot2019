package frc.team7170.lib.math;


import java.util.stream.IntStream;

/**
 * A collection of various calculations that might be done frequently enough throughout the robot code as to warrant a
 * unified location for all of them.
 *
 * @author Robert Russell
 */
public final class CalcUtil {

    private static final double EPSILON = 1e-6;

    // Enforce non-instantiability.
    private CalcUtil() {}

    /**
     * Check if a number lies within some threshold of some intermediary value.
     * @param val The number.
     * @param median The number the threshold is centered on.
     * @param thresh The threshold.
     * @return If the number lies within the threshold.
     */
    public static boolean inThreshold(double val, double median, double thresh) {
        return (median - thresh <= val) && (val <= median + thresh);
    }

    /**
     * Check if two numbers are within some small epsilon of each other.
     * @param val1 The first number.
     * @param val2 The second number.
     * @return Whether or not the numbers are within epsilon of each other.
     */
    public static boolean epsilonEquals(double val1, double val2) {
        return inThreshold(val1 - val2, 0, EPSILON);
    }

    /**
     * Clamp a given value into a given domain.
     * @param val The value to clamp.
     * @param lower The lower bound.
     * @param upper The upper bound.
     * @return The original value if {@code lower <= val <= upper}, otherwise the extreme of the range which the value
     * exceeds.
     * @throws IllegalArgumentException if the lower bound is greater than the upper bound.
     * @see CalcUtil#clamp(double, double)
     */
    public static double clamp(double val, double lower, double upper) {
        if (lower > upper) {
            throw new IllegalArgumentException("lower bound must be less than upper bound");
        }
        if (lower <= val && val <= upper) {
            return val;
        } else if (val > upper) {
            return upper;
        }
        return lower;
    }

    /**
     * Clamp a given value within some absolute range of zero.
     * @param val The value to clamp.
     * @param absMax The maximum absolute (i.e. positive) allowable difference from zero.
     * @return The original value if {@code -absMax <= val <= absMax}, otherwise the extreme of the range which the
     * value exceeds.
     * @throws IllegalArgumentException if absMax is negative.
     * @see CalcUtil#clamp(double, double, double)
     */
    public static double clamp(double val, double absMax) {
        return clamp(val, -absMax, absMax);
    }

    /**
     * <p>
     * Take a number originally defined within a given range and alter it to fit a given new range in such a way that
     * the ratio between the magnitude of the portion of the range above the value to that below the value remains
     * constant before and after the mapping.
     * </p>
     * <p>
     * E.g. {@code mapRange(5, 0, 10, 0, 20)} would return 10 because 10 is in the centre of the range {@code [0, 20]}
     * in the same way that 5 is in the centre of the range {@code [0, 10]}.
     * </p>
     * @param val The number.
     * @param lower The original lower bound.
     * @param upper The original upper bound.
     * @param target_lower The new lower bound.
     * @param target_upper The new upper bound.
     * @return The new mapped value.
     */
    public static double mapRange(double val, double lower, double upper, double target_lower, double target_upper) {
        return (val - lower) / (upper - lower) * (target_upper - target_lower) + target_lower;
    }

    /**
     * <p>
     * Linearly interpolate between {@code val0} and {@code val1} according to {@code x}.
     * </p>
     * <p>
     * Strictly speaking, if <code>f(y) = ky, k&#8712;&#8477;</code> (i.e. {@code f} is linear) with {@code f(a) = val0}
     * and {@code f(b) = val1}, and function {@code g} linearly maps {@code [0, 1]} to {@code [a, b]}, then this
     * function returns {@code f(g(x))}.
     * </p>
     * @param val0 The first value.
     * @param val1 The second value.
     * @param x Where to interpolate between {@code val0} and {@code val1}. Should be in range {@code [0, 1]}.
     * @return The interpolated value.
     */
    public static double interpolate(double val0, double val1, double x) {
        return val0 + (val1 - val0) * x;
    }

    // TODO: some of these might be more appropriately placed in an ArrayUtil class

    /**
     * <p>
     * Given an index to an array of a given length, clamp positive indices to be less than or equal to the length or,
     * if the index is negative, treat it as starting from the end of the array and return the appropriate positive
     * index.
     * </p>
     * <p>
     * The first mode of operation--when the index is positive--can be useful when dealing with ranges of indices where
     * one wishes to consider only the valid indices in a range and ignore the invalid ones. The second mode of
     * operation--when the index is negative--can be useful to add negative index support to custom container classes.
     * </p>
     * @param idx The index.
     * @param length The length of the array or other data structure.
     * @return The normalized index.
     * @throws IllegalArgumentException if the given length is less than zero.
     * @see CalcUtil#normalizeArrayIndexRestrictive(int, int)
     */
    public static int normalizeArrayIndex(int idx, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length must be greater than 0");
        }
        if (idx >= length) {
            return length;
        } else if (idx < 0) {
            return idx + length;
        }
        return idx;
    }

    /**
     * <p>
     * Given an index to an array of a given length, throw an error if the index is greater than or equal to the length
     * or, if the index is negative, treat it as starting from the end of the array and return the appropriate positive
     * index.
     * </p>
     * <p>
     * This function can be useful to add negative index support to custom container classes.
     * </p>
     * @param idx The index.
     * @param length The length of the array or other data structure.
     * @return The normalized index.
     * @throws IllegalArgumentException if the given length is less than zero.
     * @throws IndexOutOfBoundsException if the given index is greater than or equal to the give length.
     * @see CalcUtil#normalizeArrayIndexRestrictive(int, int)
     */
    public static int normalizeArrayIndexRestrictive(int idx, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length must be greater than 0");
        }
        if (idx >= length) {
            throw new IndexOutOfBoundsException();
        } else if (idx < 0) {
            return idx + length;
        }
        return idx;
    }

    /**
     * Convert a given range of indices into an array of indices.
     * @param startIdx The beginning of the index range, inclusive.
     * @param endIdx The end of the index range, exclusive.
     * @return The array of indices, empty if {@code startIdx >= endIdx}.
     */
    public static int[] rangeToIndices(int startIdx, int endIdx) {
        return IntStream.range(startIdx, endIdx).toArray();
    }
}
