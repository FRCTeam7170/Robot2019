package frc.team7170.lib.oi;


public abstract class Axis {

    private double scale;
    private double offset;
    private final String name;

    public Axis(String name) {
        this(name, 1.0, 0.0);
    }

    public Axis(String name, double scale, double offset) {
        KeyBindings.verifyName(name);
        this.name = name;
        this.scale = scale;
        this.offset = offset;
    }

    public double get() {
        return getRaw() * scale + offset;
    }

    public abstract double getRaw();

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public void resetModifiers() {
        setScale(1.0);
        setOffset(0.0);
    }

    public final String getAxisName() {
        return name;
    }
}
