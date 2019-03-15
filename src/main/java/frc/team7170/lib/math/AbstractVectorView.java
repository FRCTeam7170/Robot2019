package frc.team7170.lib.math;

abstract class AbstractVectorView extends VectorImpl {

    protected final int[] indices;

    AbstractVectorView(double[] data, int[] indices) {
        // TODO: should lazily generate data matrix on toArray()
        super(indexByArray(data, indices));
        this.indices = indices;
    }

    AbstractVectorView(double[] data, int startIdx, int endIdx) {
        this(data, CalcUtil.rangeToIndices(startIdx, endIdx));
    }

    @Override
    public int length() {
        return indices.length;
    }

    @Override
    public Vector copy(int startIdx, int endIdx) {
        startIdx = CalcUtil.rectifyArrayIndex(startIdx, length());
        endIdx = CalcUtil.rectifyArrayIndex(endIdx, length());
        // copy(int[]) is slower than copy(int, int), but uses get(int) rather than accessing data directly.
        return super.copy(CalcUtil.rangeToIndices(startIdx, endIdx));
    }

    @Override
    public abstract double get(int idx) throws IndexOutOfBoundsException;

    @Override
    public void set(int idx, double value) throws IndexOutOfBoundsException {
        idx = CalcUtil.rectifyArrayIndexRestrictive(idx, length());
        super.set(idx, value);  // So that the data array gets update in case toArray is called.
        setByParent(idx, value);
    }

    protected abstract void setByParent(int idx, double value) throws IndexOutOfBoundsException;

    private static double[] indexByArray(double[] array, int[] indices) {
        double[] result = new double[indices.length];
        for (int i = 0; i < indices.length; ++i) {
            result[i] = array[indices[i]];
        }
        return result;
    }
}
