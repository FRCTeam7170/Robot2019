package frc.team7170.lib.math;

class MatrixView extends MatrixImpl {

    private final Matrix parent;
    private final int[] rows;
    private final int[] cols;

    MatrixView(Matrix parent, int[] rows, int[] cols) {
        super(new double[0][0]);
        this.parent = parent;
        this.rows = rows;
        this.cols = cols;
    }

    MatrixView(Matrix parent, int startRow, int startCol, int endRow, int endCol) {
        this(parent, CalcUtil.rangeToIndices(startRow, endRow), CalcUtil.rangeToIndices(startCol, endCol));
    }

    @Override
    public int nRows() {
        return rows.length;
    }

    @Override
    public int nCols() {
        return cols.length;
    }

    @Override
    public double get(int row, int col) throws IndexOutOfBoundsException {
        row = CalcUtil.rectifyArrayIndexRestrictive(row, nRows());
        col = CalcUtil.rectifyArrayIndexRestrictive(col, nCols());
        return parent.get(rows[row], cols[col]);
    }

    @Override
    public void set(int row, int col, double value) throws IndexOutOfBoundsException {
        row = CalcUtil.rectifyArrayIndexRestrictive(row, nRows());
        col = CalcUtil.rectifyArrayIndexRestrictive(col, nCols());
        parent.set(rows[row], cols[col], value);
    }

    @Override
    public double[][] toArray() {
        double[][] copy = new double[nRows()][nCols()];
        visitRowWise((r, c, value) -> copy[r][c] = value);
        return copy;
    }

    @Override
    public void setRow(int rowIdx, Vector row) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (row.length() != nCols()) {
            throw new IllegalArgumentException("row must be same width as matrix");
        }
        rowIdx = CalcUtil.rectifyArrayIndexRestrictive(rowIdx, nRows());
        for (int c = 0; c < nCols(); ++c) {
            set(rowIdx, c, row.get(c));
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Matrix copy(int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        return super.copy(CalcUtil.rangeToIndices(startRow, startCol), CalcUtil.rangeToIndices(endRow, endCol));
    }

    @Override
    public Vector copyRow(int row) throws IndexOutOfBoundsException {
        row = CalcUtil.rectifyArrayIndexRestrictive(row, nRows());
        double[] rowData = new double[nCols()];
        for (int c = 0; c < nCols(); ++c) {
            rowData[c] = get(row, c);
        }
        return new VectorImpl(rowData);
    }
}
