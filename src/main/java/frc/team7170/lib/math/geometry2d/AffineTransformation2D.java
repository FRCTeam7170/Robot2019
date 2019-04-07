package frc.team7170.lib.math.geometry2d;

import frc.team7170.lib.math.Matrix;

public interface AffineTransformation2D {

    Matrix getMatrix();

    AffineTransformation2D compose(AffineTransformation2D other);

    AffineTransformation2D translate(double x, double y);

    AffineTransformation2D translate(Vector2D vector);

    AffineTransformation2D rotate(Angle angle, Vector2D about);

    default AffineTransformation2D rotate(Angle angle) {
        return rotate(angle, Vector2D.ORIGIN);
    }

    AffineTransformation2D stretch(double kx, double ky);

    AffineTransformation2D squeeze(double k);

    AffineTransformation2D shear(double kx, double ky);

    AffineTransformation2D reflect(Line2D line);
}
