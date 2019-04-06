package frc.team7170.lib.math;

import java.util.Arrays;

/**
 * A simple vector implementation using an array as the backing data structure.
 */
public class VectorImpl implements Vector {

    private final double[] data;

    /**
     * @param length the length of the vector.
     */
    public VectorImpl(int length) {
        data = new double[length];
    }

    /**
     * @param data the array to initialize the vector with.
     */
    public VectorImpl(double[] data) {
        // TODO: should this make a copy of the array?
        this.data = data;
    }

    private boolean matchingSize(Vector other) {
        return length() == other.length();
    }

    @Override
    public int length() {
        return data.length;
    }

    @Override
    public double norm() {
        double norm = 0.0;
        // Can't use visit here because norm is not (effectively) final.
        for (int i = 0; i < length(); ++i) {
            double val = get(i);
            norm += val * val;
        }
        return Math.sqrt(norm);
    }

    @Override
    public Vector add(Vector other) throws IllegalArgumentException {
        if (!matchingSize(other)) {
            throw new IllegalArgumentException("vector size must be identical for addition");
        }
        double[] newData = new double[length()];
        visit((i, value) -> newData[i] = value + other.get(i));
        return new VectorImpl(newData);
    }

    @Override
    public Vector add(double value) {
        double[] newData = new double[length()];
        visit((i, thisValue) -> newData[i] = thisValue + value);
        return new VectorImpl(newData);
    }

    @Override
    public Vector subtract(Vector other) throws IllegalArgumentException {
        if (!matchingSize(other)) {
            throw new IllegalArgumentException("vector size must be identical for subtraction");
        }
        double[] newData = new double[length()];
        visit((i, value) -> newData[i] = value - other.get(i));
        return new VectorImpl(newData);
    }

    @Override
    public Vector scale(double value) {
        double[] newData = new double[length()];
        visit((i, thisValue) -> newData[i] = thisValue * value);
        return new VectorImpl(newData);
    }

    @Override
    public Vector multiplyElementWise(Vector other) throws IllegalArgumentException {
        if (!matchingSize(other)) {
            throw new IllegalArgumentException("vector size must be identical for element-wise multiplication");
        }
        double[] newData = new double[length()];
        visit((i, value) -> newData[i] = value * other.get(i));
        return new VectorImpl(newData);
    }

    @Override
    public double dot(Vector other) throws IllegalArgumentException {
        if (!matchingSize(other)) {
            throw new IllegalArgumentException("vector size must be identical for dot product");
        }
        double dot = 0.0;
        // Can't use visit here because dot is not (effectively) final.
        for (int i = 0; i < length(); ++i) {
            dot += get(i) * other.get(i);
        }
        return dot;
    }

    @Override
    public Vector power(double power) {
        double[] newData = new double[length()];
        visit((i, value) -> newData[i] = Math.pow(value, power));
        return new VectorImpl(newData);
    }

    @Override
    public double get(int idx) throws IndexOutOfBoundsException {
        idx = CalcUtil.rectifyArrayIndexRestrictive(idx, length());
        return data[idx];
    }

    @Override
    public void set(int idx, double value) throws IndexOutOfBoundsException {
        idx = CalcUtil.rectifyArrayIndexRestrictive(idx, length());
        data[idx] = value;
    }

    @Override
    public double[] toArray() {
        return Arrays.copyOf(data, length());
    }

    @Override
    public Vector view(int startIdx, int endIdx) {
        return new VectorView(
                this,
                CalcUtil.rectifyArrayIndex(startIdx, length()),
                CalcUtil.rectifyArrayIndex(endIdx, length())
        );
    }

    @Override
    public Vector view(int[] indices) throws IndexOutOfBoundsException {
        for (int i = 0; i < indices.length; ++i) {
            indices[i] = CalcUtil.rectifyArrayIndexRestrictive(indices[i], length());
        }
        return new VectorView(this, indices);
    }

    @Override
    public Vector copy(int startIdx, int endIdx) {
        startIdx = CalcUtil.rectifyArrayIndex(startIdx, length());
        endIdx = CalcUtil.rectifyArrayIndex(endIdx, length());
        return new VectorImpl(Arrays.copyOfRange(data, startIdx, endIdx));
    }

    @Override
    public Vector copy(int[] indices) throws IndexOutOfBoundsException {
        double[] result = new double[indices.length];
        for (int i = 0; i < indices.length; ++i) {
            result[i] = get(CalcUtil.rectifyArrayIndexRestrictive(indices[i], length()));
        }
        return new VectorImpl(result);
    }

    @Override
    public void visit(VectorVisitor visitor, int startIdx, int endIdx) {
        startIdx = CalcUtil.rectifyArrayIndex(startIdx, length());
        endIdx = CalcUtil.rectifyArrayIndex(endIdx, length());
        for (int i = startIdx; i < endIdx; ++i) {
            visitor.visit(i, get(i));
        }
    }

    @Override
    public void mutate(VectorMutator mutator, int startIdx, int endIdx) {
        startIdx = CalcUtil.rectifyArrayIndex(startIdx, length());
        endIdx = CalcUtil.rectifyArrayIndex(endIdx, length());
        for (int i = startIdx; i < endIdx; ++i) {
            set(i, mutator.mutate(i, get(i)));
        }
    }

    @Override
    public Matrix asRowMatrix() {
        double[][] newData = new double[1][];
        newData[0] = Arrays.copyOf(data, length());
        return new MatrixImpl(newData);
    }

    @Override
    public Matrix asColMatrix() {
        return asRowMatrix().transpose();
    }
}
