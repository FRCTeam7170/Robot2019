package frc.team7170.lib.math.geometry2d;

import frc.team7170.lib.math.Matrix;
import frc.team7170.lib.math.ArrayMatrix;

public class AffineTransformation2D {

    private static final Matrix IDENTITY = new ArrayMatrix(new double[][] {
            new double[] {1.0, 0.0, 0.0},
            new double[] {0.0, 1.0, 0.0},
            new double[] {0.0, 0.0, 1.0}
    });

    private final Matrix matrix;

    public AffineTransformation2D() {
        matrix = IDENTITY;
    }

    private AffineTransformation2D(Matrix matrix) {
        this.matrix = matrix;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public AffineTransformation2D compose(AffineTransformation2D other) {
        return new AffineTransformation2D(matrix.multiply(other.getMatrix()));
    }

    public AffineTransformation2D translate(double x, double y) {
        return new AffineTransformation2D(matrix.multiply(newMat(1.0, 0.0, x, 0.0, 1.0, y)));
    }

    public AffineTransformation2D translate(Vector2D vector) {
        return translate(vector.getX(), vector.getY());
    }

    public AffineTransformation2D rotate(Angle angle, Vector2D about) {
        double sin = angle.sin();
        double cos = angle.cos();
        double x = about.getX();
        double y = about.getY();
        // This is what you get if you translate by (x, y), rotate by the given angle, then translate by (-x, -y).
        Matrix rotMat = newMat(cos, -sin, x + y*sin - x*cos, sin, cos, y - y*cos - x*sin);
        return new AffineTransformation2D(matrix.multiply(rotMat));
    }

    public AffineTransformation2D rotate(Angle angle) {
        return rotate(angle, Vector2D.ORIGIN);
    }

    public AffineTransformation2D stretch(double kx, double ky) {
        return new AffineTransformation2D(matrix.multiply(newMat(kx, 0.0, 0.0, 0.0, ky, 0.0)));
    }

    public AffineTransformation2D squeeze(double k) {
        return stretch(k, 1/k);
    }

    public AffineTransformation2D shearX(double k) {
        return new AffineTransformation2D(matrix.multiply(newMat(1.0, k, 0.0, 0.0, 1.0, 0.0)));
    }

    public AffineTransformation2D shearY(double k) {
        return new AffineTransformation2D(matrix.multiply(newMat(1.0, 0.0, 0.0, k, 1.0, 0.0)));
    }

    public AffineTransformation2D reflect(Line2D line) {
        return null;  // TODO
    }

    private static Matrix newMat(double a00, double a01, double a02, double a10, double a11, double a12) {
        return new ArrayMatrix(new double[][] {
                new double[] {a00, a01, a02},
                new double[] {a10, a11, a12},
                new double[] {0.0, 0.0, 1.0}
        });
    }
}
