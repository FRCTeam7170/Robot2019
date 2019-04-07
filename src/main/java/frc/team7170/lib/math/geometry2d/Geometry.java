package frc.team7170.lib.math.geometry2d;

public interface Geometry<G extends Geometry<G>> {

    G transform(AffineTransformation2D transformation);
}
