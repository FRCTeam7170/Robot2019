package frc.team7170.lib.math.geometry2d;

public class Angle {

    public enum Quadrant {
        Q1, Q2, Q3, Q4
    }

    private final double radians;

    private Angle(double radians) {
        this.radians = normalize(radians);
    }

    public double sin() {
        return Math.sin(radians);
    }

    public double cos() {
        return Math.cos(radians);
    }

    public double tan() {
        return Math.tan(radians);
    }

    public double radians() {
        return radians;
    }

    public double degrees() {
        return Math.toDegrees(radians);
    }

    public Angle add(Angle other) {
        return new Angle(radians + other.radians);
    }

    public Angle subtract(Angle other) {
        return new Angle(radians - other.radians);
    }

    public Angle acuteReference() {
        switch (quadrant()) {
            case Q1:
                return this;
            case Q2:
                return new Angle(Math.PI - radians);
            case Q3:
                return new Angle(radians - Math.PI);
            case Q4:
                return new Angle(2* Math.PI - radians);
            default:
                throw new AssertionError();
        }
    }

    public Line2D getRay() {
        return null;  // TODO
    }

    public Quadrant quadrant() {
        if (radians > 3 / 2 * Math.PI) {
            return Quadrant.Q4;
        } else if (radians > Math.PI) {
            return Quadrant.Q3;
        } else if (radians > Math.PI / 2) {
            return Quadrant.Q2;
        } else {
            return Quadrant.Q1;
        }
    }

    public boolean isAcute() {
        return quadrant() == Quadrant.Q1;
    }

    public boolean isObtuse() {
        return quadrant() == Quadrant.Q2;
    }

    public boolean isReflex() {
        Quadrant quadrant = quadrant();
        return (quadrant == Quadrant.Q3) || (quadrant == Quadrant.Q4);
    }

    private static double normalize(double radians) {
        radians %= (2 * Math.PI);
        if (radians < 0) {
            radians += 2 * Math.PI;
        }
        return radians;
    }

    public static Angle fromRadians(double radians) {
        return new Angle(radians);
    }

    public static Angle fromDegrees(double degrees) {
        return new Angle(Math.toRadians(degrees));
    }

    public static Angle fromVector(Vector2D vector) {
        return new Angle(Math.atan2(vector.getY(), vector.getX()));
    }

    // TODO: more factory methods: angle between lines/vectors (reflex or obtuse/acute), etc.
}
