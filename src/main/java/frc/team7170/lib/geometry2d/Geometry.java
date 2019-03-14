package frc.team7170.lib.geometry2d;

public interface Geometry<G extends Geometry<G>> {

    G transform(Transformation transformation);
}
