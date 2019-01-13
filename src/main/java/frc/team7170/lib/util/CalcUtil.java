package frc.team7170.lib.util;


/**
 * A collection of various calculations that might be done frequently enough throughout the robot code as to warrant the
 * need for a unified location for all of them.
 */
public final class CalcUtil {

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
     * Clamp a given value into a given domain.
     * @param val The value to check.
     * @param lower The lower bound.
     * @param upper The upper bound.
     * @return The original value if lower <= val <= upper otherwise the extreme of the range which the value exceeds.
     */
    public static double applyBounds(double val, double lower, double upper) {
        if (lower <= val && val <= upper) {
            return val;
        } else if (val > upper) {
            return upper;
        }
        return lower;
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
}
