package frc.team7170.lib;

// TODO: DigitalInput from AnalogInput with threshold
public class WrappingCounter {

    private final double maxValue;
    private final double minDeltaValue;
    private final double nearExtremeThreshold;
    private double lastValue;
    private double accumulator = 0.0;

    public WrappingCounter(double maxValue, double minDeltaValue, double nearExtremeThreshold) {
        this.maxValue = maxValue;
        this.minDeltaValue = Math.abs(minDeltaValue);
        this.nearExtremeThreshold = Math.abs(nearExtremeThreshold);
    }

    public WrappingCounter(double maxValue, double minDeltaValue) {
        this(maxValue, minDeltaValue, 0.05 * maxValue);
    }

    public WrappingCounter(double maxValue) {
        this(maxValue, 0.5 * maxValue, 0.05 * maxValue);
    }

    public void feed(double value) {
        double deltaValue = value - lastValue;
        if (nearExtreme() && Math.abs(deltaValue) > minDeltaValue) {
            double rectifiedDeltaValue = deltaValue + maxValue;
            if (deltaValue > 0) {
                accumulator -= rectifiedDeltaValue;
            } else {
                accumulator += rectifiedDeltaValue;
            }
        } else {
            accumulator += deltaValue;
        }
        lastValue = value;
    }

    public double get() {
        return accumulator;
    }

    public void reset() {
        accumulator = 0.0;
    }

    private boolean nearExtreme() {
        return CalcUtil.inThreshold(lastValue, maxValue, nearExtremeThreshold) ||
                CalcUtil.inThreshold(lastValue, 0, nearExtremeThreshold);
    }

    /*
    public static void main(String[] args) {
        WrappingCounter wc = new WrappingCounter(360);
        int revs = 0;
        for (int i = 0; i < 361; ++i) {
            System.out.println(String.format("FED WITH: %d; CURR WC: %f", i, wc.get()));
            if (i == 360) {
                if (revs == 3) {
                    break;
                }
                ++revs;
                i = 0;
            }
            wc.feed(i);
        }
    }
    */
}
