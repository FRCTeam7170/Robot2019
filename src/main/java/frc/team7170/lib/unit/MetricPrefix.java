package frc.team7170.lib.unit;

public enum MetricPrefix {
    YOTTA(24),
    ZETTA(21),
    EXA(18),
    PETA(15),
    TERA(12),
    GIGA(9),
    MEGA(6),
    KILO(3),
    HECTO(2),
    DECA(1),
    BASE(0),
    DECI(-1),
    CENTI(-2),
    MILLI(-3),
    MICRO(-6),
    NANO(-9),
    PICO(-12),
    FEMTO(-15),
    ATTO(-18),
    ZEPTO(-21),
    YOCTO(-24);

    public final int power;

    MetricPrefix(int power) {
        this.power = power;
    }

    public double getFactor() {
        return Math.pow(10, power);
    }
}
