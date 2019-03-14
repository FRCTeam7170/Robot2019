package frc.team7170.lib.math;

import java.util.Arrays;

public class VectorImpl implements Vector {

    private final double[] data;

    public VectorImpl(int length) {
        data = new double[length];
    }

    public VectorImpl(double[] data) {
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
        for (double datum : data) {
            norm += datum * datum;
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
    public Vector scale(double value) {
        double[] newData = new double[length()];
        visit((i, thisValue) -> newData[i] = thisValue * value);
        return new VectorImpl(newData);
    }

    @Override
    public double dot(Vector other) throws IllegalArgumentException {
        if (!matchingSize(other)) {
            throw new IllegalArgumentException("vector size must be identical for dot product");
        }
        double dot = 0.0;
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
        return data[idx];
    }

    @Override
    public void set(int idx, double value) throws IndexOutOfBoundsException {
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
        for (int idx : indices) {
            idx = CalcUtil.rectifyArrayIndexRestrictive(idx, length());
            result[idx] = get(idx);
        }
        return new VectorImpl(result);
    }

    @Override
    public void visit(VectorVisitor visitor, int startIdx, int endIdx) {
        startIdx = CalcUtil.rectifyArrayIndex(startIdx, length());
        endIdx = CalcUtil.rectifyArrayIndex(endIdx, length());
        for (int i = startIdx; i < endIdx; ++i) {
            visitor.accept(i, get(i));
        }
    }

    @Override
    public void mutate(VectorMutator visitor, int startIdx, int endIdx) {
        startIdx = CalcUtil.rectifyArrayIndex(startIdx, length());
        endIdx = CalcUtil.rectifyArrayIndex(endIdx, length());
        for (int i = startIdx; i < endIdx; ++i) {
            set(i, visitor.accept(i, get(i)));
        }
    }
}
