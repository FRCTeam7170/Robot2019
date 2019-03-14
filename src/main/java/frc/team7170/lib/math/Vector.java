package frc.team7170.lib.math;

public interface Vector {

    int length();

    double norm();

    Vector add(Vector other) throws IllegalArgumentException;

    Vector add(double value);

    default Vector subtract(double value) {
        return add(-value);
    }

    Vector scale(double value);

    default Vector inverse() {
        return scale(-1.0);
    }

    double dot(Vector other) throws IllegalArgumentException;

    Vector power(double power);

    double get(int idx) throws IndexOutOfBoundsException;

    void set(int idx, double value) throws IndexOutOfBoundsException;

    double[] toArray();

    Vector view(int startIdx, int endIdx);

    Vector view(int[] indices) throws IndexOutOfBoundsException;

    Vector copy(int startIdx, int endIdx);

    Vector copy(int[] indices) throws IndexOutOfBoundsException;

    default Vector copy() {
        return copy(0, length());
    }

    void visit(VectorVisitor visitor, int startIdx, int endIdx);

    void mutate(VectorMutator visitor, int startIdx, int endIdx);

    default void visit(VectorVisitor visitor) {
        visit(visitor, 0, length());
    }

    default void mutate(VectorMutator visitor) {
        mutate(visitor, 0, length());
    }

    @FunctionalInterface
    interface VectorVisitor {
        void accept(int idx, double value);
    }

    @FunctionalInterface
    interface VectorMutator {
        double accept(int idx, double value);
    }
}
