package frc.team7170.lib.math.geometry2d;

import org.apache.commons.math3.linear.RealMatrix;

public interface Transformation {

    // TODO: consider making a custom matrix implementation so we don't have to lug around commons math
    RealMatrix getMatrix();

    Transformation compose(Transformation other);

    Transformation translate(double x, double y);

    Transformation translate(Vector vector);

    Transformation rotate(Angle angle, Vector about);

    Transformation rotate(Angle angle);

    Transformation stretch(double kx, double ky);

    Transformation squeeze(double k);

    Transformation shear(double kx, double ky);

    // Transformation reflect(Line line);
}
