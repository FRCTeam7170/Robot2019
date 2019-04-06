package frc.team7170.lib.math;

abstract class MatrixVectorView extends AbstractVectorView {

    private static class MatrixRowView extends MatrixVectorView {

        private MatrixRowView(Matrix parent, int row) {
            super(parent, row, parent.nCols());
        }

        @Override
        public double get(int idx) throws IndexOutOfBoundsException {
            idx = CalcUtil.normalizeArrayIndexRestrictive(idx, length());
            return parent.get(majorIdx, indices[idx]);
        }

        @Override
        public void set(int idx, double value) throws IndexOutOfBoundsException {
            idx = CalcUtil.normalizeArrayIndexRestrictive(idx, length());
            parent.set(majorIdx, indices[idx], value);
        }
    }

    private static class MatrixColView extends MatrixVectorView {

        private MatrixColView(Matrix parent, int col) {
            super(parent, col, parent.nRows());
        }

        @Override
        public double get(int idx) throws IndexOutOfBoundsException {
            idx = CalcUtil.normalizeArrayIndexRestrictive(idx, length());
            return parent.get(indices[idx], majorIdx);
        }

        @Override
        public void set(int idx, double value) throws IndexOutOfBoundsException {
            idx = CalcUtil.normalizeArrayIndexRestrictive(idx, length());
            parent.set(indices[idx], majorIdx, value);
        }
    }

    protected final Matrix parent;
    protected final int majorIdx;

    private MatrixVectorView(Matrix parent, int majorIdx, int length) {
        super(0, length);
        this.parent = parent;
        this.majorIdx = majorIdx;
    }

    static MatrixVectorView newRowView(Matrix parent, int row) {
        return new MatrixRowView(parent, row);
    }

    static MatrixVectorView newColView(Matrix parent, int col) {
        return new MatrixColView(parent, col);
    }
}
