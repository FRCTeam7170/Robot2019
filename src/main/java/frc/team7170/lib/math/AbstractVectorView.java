package frc.team7170.lib.math;

// This overrides all methods in ArrayVector that reference data.
abstract class AbstractVectorView extends ArrayVector {

    protected final int[] indices;

    AbstractVectorView(int[] indices) {
        super(new double[0]);
        this.indices = indices;
    }

    AbstractVectorView(int startIdx, int endIdx) {
        this(CalcUtil.rangeToIndices(startIdx, endIdx));
    }

    @Override
    public int length() {
        return indices.length;
    }

    @Override
    public abstract double get(int idx) throws IndexOutOfBoundsException;

    @Override
    public abstract void set(int idx, double value) throws IndexOutOfBoundsException;

    @Override
    public double[] toArray() {
        double[] copy = new double[length()];
        visit((i, value) -> copy[i] = value);
        return copy;
    }

    @Override
    public Vector copy(int startIdx, int endIdx) {
        startIdx = CalcUtil.normalizeArrayIndex(startIdx, length());
        endIdx = CalcUtil.normalizeArrayIndex(endIdx, length());
        // copy(int[]) is slower than copy(int, int), but uses get(int) rather than accessing data directly.
        return super.copy(CalcUtil.rangeToIndices(startIdx, endIdx));
    }

    @Override
    public Matrix asRowMatrix() {
        double[][] newData = new double[1][length()];
        visit((i, value) -> newData[0][i] = value);
        return new ArrayMatrix(newData);
    }
}
