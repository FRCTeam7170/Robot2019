package frc.team7170.lib.math;


import java.util.stream.IntStream;

/**
 * A collection of various calculations that might be done frequently enough throughout the robot code as to warrant a
 * unified location for all of them.
 */
public final class CalcUtil {

    private static final double EPSILON = 1e-6;

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
     * @param val The value to check.
     * @param lower The lower bound.
     * @param upper The upper bound.
     * @return The original value if lower <= val <= upper, otherwise the extreme of the range which the value exceeds.
     */
    public static double applyBounds(double val, double lower, double upper) {
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

    public static double applyBounds(double val, double absMax) {
        return applyBounds(val, -absMax, absMax);
    }

    /**
     * Convert a number originally defined within a given range and alter it to fit a new range in such a way that the
     * ratio between the magnitude of the portion of the range above the value to that below the value remains constant
     * before and after the mapping.
     * E.g. {@code mapRange(5, 0, 10, 0, 20)} would return 10 because 10 is in the centre of the range [0, 20] in the
     * same way that 5 is in the centre of the range [0, 10].
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

    public static double interpolate(double val0, double val1, double x) {
        return val0 + (val1 - val0) * x;
    }

    // TODO: some of these might be more appropriately placed in an ArrayUtil class

    public static int rectifyArrayIndex(int idx, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length must be greater than 0");
        }
        if (idx > length) {
            return length;
        } else if (idx < 0) {
            return idx + length;
        }
        return idx;
    }

    public static int rectifyArrayIndexRestrictive(int idx, int length) {
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

    public static int[] rangeToIndices(int startIdx, int endIdx) {
        return IntStream.range(startIdx, endIdx).toArray();
    }
}
