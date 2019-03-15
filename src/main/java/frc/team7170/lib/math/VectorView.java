package frc.team7170.lib.math;

class VectorView extends AbstractVectorView {

    private final Vector parent;

    VectorView(Vector parent, int[] indices) {
        super(parent.toArray(), indices);
        this.parent = parent;
    }

    VectorView(Vector parent, int startIdx, int endIdx) {
        super(parent.toArray(), startIdx, endIdx);
        this.parent = parent;
    }

    @Override
    public double get(int idx) throws IndexOutOfBoundsException {
        idx = CalcUtil.rectifyArrayIndexRestrictive(idx, length());
        return parent.get(indices[idx]);
    }

    @Override
    protected void setByParent(int idx, double value) throws IndexOutOfBoundsException {
        parent.set(indices[idx], value);
    }
}
