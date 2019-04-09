package frc.team7170.lib.math.geometry2d;

import frc.team7170.lib.math.ArrayVector;
import frc.team7170.lib.math.Vector;

/**
 * A vector of cardinality 2 for use in 2D geometry. As per convention, the two components are called {@code x} and
 * {@code y}, respectively.
 * @implSpec This uses a {@link Vector Vector} behind the scenes.
 * @author Robert Russell
 */
public class Vector2D implements Geometry<Vector2D> {

    public static final Vector2D ORIGIN = new Vector2D(0, 0);

    private final Vector vector;

    public Vector2D(double x, double y) {
        vector = new ArrayVector(new double[] {x, y});
    }

    private Vector2D(Vector vector) {
        this.vector = vector;
    }

    public double getX() {
        return vector.get(0);
    }

    public double getY() {
        return vector.get(1);
    }

    public double norm() {
        return vector.norm();
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(vector.add(other.vector));
    }

    public Vector2D add(double value) {
        return new Vector2D(vector.add(value));
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(vector.subtract(other.vector));
    }

    public Vector2D subtract(double value) {
        return new Vector2D(vector.subtract(value));
    }

    public Vector2D scale(double value) {
        return new Vector2D(vector.scale(value));
    }

    public Vector2D inverse() {
        return new Vector2D(vector.inverse());
    }

    public Vector2D multiplyElementWise(Vector2D other) {
        return new Vector2D(vector.multiplyElementWise(other.vector));
    }

    public double dot(Vector2D other) {
        return vector.dot(other.vector);
    }

    public Vector2D power(double power) {
        return new Vector2D(vector.power(power));
    }

    @Override
    public Vector2D transform(AffineTransformation2D transformation) {
        return null;  // TODO
    }
}
