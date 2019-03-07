package frc.team7170.lib;

import java.util.logging.Logger;

// TODO: CURRENTLY HERE - print the min and max deltas, not including wraps

public class WrappingDouble {

    private static final Logger LOGGER = Logger.getLogger(WrappingDouble.class.getName());

    public static class WrappingDoubleCharacterizer {

        private static class Averager {

            private double average;
            private long nSamples = 0L;

            public Averager(double initAvg) {
                average = initAvg;
            }

            public Averager() {
                this(0.0);
            }

            public void feed(double value) {
                ++nSamples;
                average = average * (1.0 - 1.0 / (double) nSamples) + value / (double) nSamples;
            }
        }

        private final Averager sigma2;
        private final Averager samples = new Averager();
        private double lastValue;

        public WrappingDoubleCharacterizer(double initValue, double initSigma) {
            lastValue = initValue;
            sigma2 = new Averager(initSigma*initSigma);
        }

        public WrappingDoubleCharacterizer(double initValue) {
            this(initValue, 0.0);
        }

        public void feed(double value) {
            double absDelta = Math.abs(value - lastValue);
            samples.feed(absDelta);
            sigma2.feed(absDelta*absDelta - 2*absDelta*samples.average + samples.average*samples.average);
            lastValue = value;
        }

        public double getStdDeviation() {
            return Math.sqrt(sigma2.average);
        }

        public double getMean() {
            return samples.average;
        }
    }

    private enum WrapDirection {
        HIGH_TO_LOW(true) {
            @Override
            public boolean agreesWithDelta(double delta) {
                return delta < 0;
            }
        },

        LOW_TO_HIGH(true) {
            @Override
            public boolean agreesWithDelta(double delta) {
                return delta > 0;
            }
        },

        INVALID(false) {
            @Override
            public boolean agreesWithDelta(double delta) {
                throw new UnsupportedOperationException();
            }
        };

        private final boolean isWrap;

        WrapDirection(boolean isWrap) {
            this.isWrap = isWrap;
        }

        public abstract boolean agreesWithDelta(double delta);
    }

    private final double maxValue;
    private final double nearExtremeThreshold;
    private final double maxNoiseDeviation;
    private final double stdDeviation;
    private final double mean;
    private double lastValue;
    private double relativePosition;
    private int cycles = 0;
    private double offset = 0.0;
    private WrapDirection currWrapDirection = WrapDirection.INVALID;

    public WrappingDouble(double maxValue,
                          double nearExtremeThreshold,
                          double maxNoiseDeviation,
                          double stdDeviation,
                          double mean,
                          double initVal) {
        this.maxValue = maxValue;
        this.nearExtremeThreshold = Math.abs(nearExtremeThreshold);
        this.maxNoiseDeviation = maxNoiseDeviation;
        this.stdDeviation = stdDeviation;
        this.mean = mean;
        this.lastValue = this.relativePosition = initVal;
    }

    // TODO: telescoping constructors / builder

    public void feed(double value) {
        double delta = value - lastValue;
        if (!deltaDeviationPermissible(delta)) {
            if (!currWrapDirection.isWrap) {
                currWrapDirection = deltaToWrapDirection(delta);
                switch (currWrapDirection) {
                    case HIGH_TO_LOW:
                        System.out.println("TRIGGERED HIGH -> LOW WRAP");
                        ++cycles;
                        break;
                    case LOW_TO_HIGH:
                        System.out.println("TRIGGERED LOW -> HIGH WRAP");
                        --cycles;
                        break;
                    case INVALID:
                        LOGGER.warning(String.format("Deviation for delta %.03f was considered non-noise, yet " +
                                "exhibited an invalid delta direction at an extreme or was not near an extreme.", delta));
                }
            } else if (!currWrapDirection.agreesWithDelta(delta)) {
                LOGGER.warning("Wrap direction switched in the middle of a wrap? Highly unlikely.");
                switch (currWrapDirection) {
                    case HIGH_TO_LOW:
                        --cycles;
                        currWrapDirection = WrapDirection.LOW_TO_HIGH;
                        break;
                    case LOW_TO_HIGH:
                        ++cycles;
                        currWrapDirection = WrapDirection.HIGH_TO_LOW;
                        break;
                }
            }
        } else if (currWrapDirection.isWrap && nearOppositeExtreme(value)) {
            /* This is from before the second condition above was added.
            switch (currWrapDirection) {
                case HIGH_TO_LOW:
                    if (!nearLowExtreme(offsetValue)) {
                        System.out.println("High-to-low wrap supposedly finished, but we're not near the low extreme!");
                    }
                    break;
                case LOW_TO_HIGH:
                    if (!nearHighExtreme(offsetValue)) {
                        System.out.println("Low-to-high wrap supposedly finished, but we're not near the high extreme!");
                    }
                    break;
            }
            */
            currWrapDirection = WrapDirection.INVALID;
        }
        relativePosition = lastValue = value;
    }

    private boolean nearOppositeExtreme(double value) {
        switch (currWrapDirection) {
            case HIGH_TO_LOW:
                return nearLowExtreme(value);
            case LOW_TO_HIGH:
                return nearHighExtreme(value);
            default:
                throw new AssertionError();
        }
    }

    private boolean deltaDeviationPermissible(double delta) {
        return Math.abs(Math.abs(delta) - mean) <= maxNoiseDeviation * stdDeviation;
    }

    private boolean nearHighExtreme(double value) {
        return value >= (maxValue - nearExtremeThreshold);
    }

    private boolean nearLowExtreme(double value) {
        return value <= nearExtremeThreshold;
    }

    private WrapDirection deltaToWrapDirection(double delta) {
        if (nearHighExtreme(lastValue) && delta < 0) {
            return WrapDirection.HIGH_TO_LOW;
        } else if (nearLowExtreme(lastValue) && delta > 0) {
            return WrapDirection.LOW_TO_HIGH;
        } else {
            return WrapDirection.INVALID;
        }
    }

    public double get() {
        return relativePosition + cycles * maxValue - offset;
    }

    public void reset() {
        offset = relativePosition;
        relativePosition = 0.0;
        cycles = 0;
    }

    /*
    public static void main(String[] args) {
        Random random = new Random();
        WrappingDoubleCharacterizer wdc = new WrappingDoubleCharacterizer(random.nextDouble());
        for (double j = 0.0; j <= 360.0; j += random.nextDouble()) {
            wdc.feed(j);
        }
        System.out.println(String.format("SD: %f; MEAN: %f", wdc.getStdDeviation(), wdc.getMean()));
        for (int j = 0; j < 100; j++) {
            double initVal = random.nextDouble();
            WrappingDouble wc = new WrappingDouble(
                    360,
                    90,
                    3.0,
                    wdc.getStdDeviation(),
                    wdc.getMean(),
                    initVal
            );
            int revs = 0;
            for (double i = initVal;; i += random.nextDouble()) {
                if (i >= 360) {
                    if (revs == 3) {
                        break;
                    }
                    ++revs;
                    i = 0.0;
                }
                wc.feed(i);
                // System.out.println(String.format("FED WITH: %f; CURR WC: %f", i, wc.get()));
            }
            System.out.println(wc.get());
        }
    }
    */
}
