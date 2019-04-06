package frc.team7170.lib.math;

class VectorView extends AbstractVectorView {

    private final Vector parent;

    VectorView(Vector parent, int[] indices) {
        super(indices);
        this.parent = parent;
    }

    VectorView(Vector parent, int startIdx, int endIdx) {
        super(startIdx, endIdx);
        this.parent = parent;
    }

    @Override
    public double get(int idx) throws IndexOutOfBoundsException {
        idx = CalcUtil.normalizeArrayIndexRestrictive(idx, length());
        return parent.get(indices[idx]);
    }

    @Override
    public void set(int idx, double value) throws IndexOutOfBoundsException {
        idx = CalcUtil.normalizeArrayIndexRestrictive(idx, length());
        parent.set(indices[idx], value);
    }
}
