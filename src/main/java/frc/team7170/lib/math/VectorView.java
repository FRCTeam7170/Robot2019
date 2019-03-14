package frc.team7170.lib.math;

import java.util.stream.IntStream;

class VectorView extends VectorImpl {

    private final Vector parent;
    private final int[] indices;

    VectorView(Vector parent, int[] indices) {
        super(indexByArray(parent.toArray(), indices));
        this.parent = parent;
        this.indices = indices;
    }

    VectorView(Vector parent, int startIdx, int endIdx) {
        this(parent, rangeToIndices(startIdx, endIdx));
    }

    @Override
    public int length() {
        return indices.length;
    }

    @Override
    public double norm() {
        double norm = 0.0;
        for (int i = 0; i < length(); ++i) {
            double val = get(i);
            norm += val * val;
        }
        return Math.sqrt(norm);
    }

    @Override
    public Vector copy(int startIdx, int endIdx) {
        // copy(int[]) is slower than copy(int, int), but uses get(int) rather than accessing data directly.
        return super.copy(rangeToIndices(startIdx, endIdx));
    }

    @Override
    public double get(int idx) throws IndexOutOfBoundsException {
        return parent.get(indices[idx]);
    }

    @Override
    public void set(int idx, double value) throws IndexOutOfBoundsException {
        parent.set(indices[idx], value);
    }

    private static int[] rangeToIndices(int startIdx, int endIdx) {
        return IntStream.range(startIdx, endIdx).toArray();
    }

    private static double[] indexByArray(double[] array, int[] indices) {
        double[] result = new double[indices.length];
        for (int i = 0; i < indices.length; ++i) {
            result[i] = array[indices[i]];
        }
        return result;
    }
}
