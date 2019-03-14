package frc.team7170.lib.geometry2d;

public interface Vector extends Geometry<Vector> {

    double getX();

    double getY();

    Vector inverse();

    default double magnitude() {
        return Math.hypot(getX(), getY());
    }

    Vector add(Vector other);

    default Vector subtract(Vector other) {
        return add(other.inverse());
    }

    Vector scale(double factor);

    double dot(Vector other);
}
