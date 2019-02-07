package frc.team7170.lib.oi;

import frc.team7170.lib.Name;

public abstract class ScaledAxis implements Axis {

    private double scale;
    private double offset;
    private final Name name;

    public ScaledAxis(Name name, double scale, double offset) {
        this.name = name;
        this.scale = scale;
        this.offset = offset;
    }

    public ScaledAxis(Name name) {
        this(name, 1.0, 0.0);
    }

    @Override
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

    @Override
    public final String getName() {
        return name.getName();
    }

    @Override
    public Name getCheckedName() {
        return name;
    }
}
