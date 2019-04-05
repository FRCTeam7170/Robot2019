package frc.team7170.lib.math;

import java.util.Arrays;

public class MatrixImpl implements Matrix {

    private final double[][] data;

    public MatrixImpl(int nRows, int nCols) {
        data = new double[nRows][nCols];
    }

    public MatrixImpl(double[][] data) {
        this.data = data;
    }

    private boolean matchingSize(Matrix other) {
        return (nRows() == other.nRows()) && (nCols() == other.nCols());
    }

    @Override
    public int nRows() {
        return data.length;
    }

    @Override
    public int nCols() {
        return nRows() > 0 ? data[0].length : 0;
    }

    @Override
    public Matrix add(Matrix other) throws IllegalArgumentException {
        if (!matchingSize(other)) {
            throw new IllegalArgumentException("matrix size must be identical for addition");
        }
        double[][] newData = new double[nRows()][nCols()];
        visitRowWise((r, c, value) -> newData[r][c] = value + other.get(r, c));
        return new MatrixImpl(newData);
    }

    @Override
    public Matrix add(double value) {
        double[][] newData = new double[nRows()][nCols()];
        visitRowWise((r, c, thisValue) -> newData[r][c] = thisValue + value);
        return new MatrixImpl(newData);
    }

    @Override
    public Matrix subtract(Matrix other) throws IllegalArgumentException {
        if (!matchingSize(other)) {
            throw new IllegalArgumentException("matrix size must be identical for subtraction");
        }
        double[][] newData = new double[nRows()][nCols()];
        visitRowWise((r, c, value) -> newData[r][c] = value - other.get(r, c));
        return new MatrixImpl(newData);
    }

    @Override
    public Matrix multiply(Matrix other) throws IllegalArgumentException {
        if (nCols() != other.nRows()) {
            throw new IllegalArgumentException("invalid matrix size for matrix multiplication");
        }
        double[][] result = new double[nRows()][other.nCols()];
        for (int r = 0; r < nRows(); ++r) {
            Vector row = copyRow(r);
            for (int c = 0; c < other.nCols(); ++c) {
                result[r][c] = row.dot(other.copyCol(c));
            }
        }
        return new MatrixImpl(result);
    }

    @Override
    public Matrix multiply(Vector other) throws IllegalArgumentException {
        if (nCols() == other.length()) {
            return multiply(other.asColMatrix());
        } else if (nRows() != other.length()) {
            return multiply(other.asRowMatrix());
        } else {
            throw new IllegalArgumentException("invalid vector length for matrix-vector multiplication");
        }
    }

    @Override
    public Matrix multiplyElementWise(Matrix other) throws IllegalArgumentException {
        if (!matchingSize(other)) {
            throw new IllegalArgumentException("matrix size must be identical for element-wise multiplication");
        }
        double[][] newData = new double[nRows()][nCols()];
        visitRowWise((r, c, value) -> newData[r][c] = value * other.get(r, c));
        return new MatrixImpl(newData);
    }

    @Override
    public Matrix multiply(double value) {
        double[][] newData = new double[nRows()][nCols()];
        visitRowWise((r, c, thisValue) -> newData[r][c] = thisValue * value);
        return new MatrixImpl(newData);
    }

    @Override
    public Matrix power(double power) {
        double[][] newData = new double[nRows()][nCols()];
        visitRowWise((r, c, value) -> newData[r][c] = Math.pow(value, power));
        return new MatrixImpl(newData);
    }

    @Override
    public Matrix transpose() {
        double[][] result = new double[nCols()][nRows()];
        visitRowWise((r, c, value) -> result[c][r] = value);
        return new MatrixImpl(result);
    }

    @Override
    public double get(int row, int col) throws IndexOutOfBoundsException {
        row = CalcUtil.rectifyArrayIndexRestrictive(row, nRows());
        col = CalcUtil.rectifyArrayIndexRestrictive(col, nCols());
        return data[row][col];
    }

    @Override
    public void set(int row, int col, double value) throws IndexOutOfBoundsException {
        row = CalcUtil.rectifyArrayIndexRestrictive(row, nRows());
        col = CalcUtil.rectifyArrayIndexRestrictive(col, nCols());
        data[row][col] = value;
    }

    @Override
    public double[][] toArray() {
        double[][] copy = new double[nRows()][];
        for (int r = 0; r < nRows(); ++r) {
            copy[r] = Arrays.copyOf(data[r], nCols());
        }
        return copy;
    }

    @Override
    public void setRow(int rowIdx, Vector row) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (row.length() != nCols()) {
            throw new IllegalArgumentException("row must be same width as matrix");
        }
        rowIdx = CalcUtil.rectifyArrayIndexRestrictive(rowIdx, nRows());
        data[rowIdx] = row.toArray();
    }

    @Override
    public void setCol(int colIdx, Vector col) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (col.length() != nRows()) {
            throw new IllegalArgumentException("col must be same height as matrix");
        }
        colIdx = CalcUtil.rectifyArrayIndexRestrictive(colIdx, nCols());
        for (int r = 0; r < nRows(); ++r) {
            set(r, colIdx, col.get(r));
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Matrix view(int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        return new MatrixView(this, startRow, startCol, endRow, endCol);
    }

    @Override
    public Matrix view(int[] rows, int[] cols) throws IndexOutOfBoundsException {
        for (int r = 0; r < rows.length; ++r) {
            rows[r] = CalcUtil.rectifyArrayIndexRestrictive(rows[r], nRows());
        }
        for (int c = 0; c < cols.length; ++c) {
            cols[c] = CalcUtil.rectifyArrayIndexRestrictive(cols[c], nCols());
        }
        return new MatrixView(this, rows, cols);
    }

    @Override
    public Vector viewRow(int row) throws IndexOutOfBoundsException {
        return MatrixVectorView.newRowView(this, CalcUtil.rectifyArrayIndexRestrictive(row, nRows()));
    }

    @Override
    public Vector viewCol(int col) throws IndexOutOfBoundsException {
        return MatrixVectorView.newColView(this, CalcUtil.rectifyArrayIndexRestrictive(col, nCols()));
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Matrix copy(int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        int nRows = endRow - startRow;
        double[][] newData = new double[nRows > 0 ? nRows : 0][];
        if ((endCol - startCol) >= 0) {  // Arrays.copyOfRange(double[], int, int) errors otherwise
            for (int r = startRow; r < endRow; ++r) {
                newData[r - startRow] = Arrays.copyOfRange(data[r], startCol, endCol);
            }
        }
        return new MatrixImpl(newData);
    }

    @Override
    public Matrix copy(int[] rows, int[] cols) throws IndexOutOfBoundsException {
        double[][] newData = new double[rows.length][cols.length];
        for (int r = 0; r < rows.length; ++r) {
            int row = CalcUtil.rectifyArrayIndexRestrictive(rows[r], nRows());
            for (int c = 0; c < cols.length; ++c) {
                int col = CalcUtil.rectifyArrayIndexRestrictive(cols[c], nCols());
                newData[r][c] = get(row, col);
            }
        }
        return new MatrixImpl(newData);
    }

    @Override
    public Vector copyRow(int row) throws IndexOutOfBoundsException {
        return new VectorImpl(Arrays.copyOf(data[CalcUtil.rectifyArrayIndexRestrictive(row, nRows())], nCols()));
    }

    @Override
    public Vector copyCol(int col) throws IndexOutOfBoundsException {
        col = CalcUtil.rectifyArrayIndexRestrictive(col, nCols());
        double[] colData = new double[nRows()];
        for (int r = 0; r < nRows(); ++r) {
            colData[r] = get(r, col);
        }
        return new VectorImpl(colData);
    }

    // TODO: WRONG IMPL! -- indices do not necessarily define a rectangular selection
    @Override
    @SuppressWarnings("Duplicates")
    public void visitRowWise(MatrixEntryVisitor visitor, int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        for (int r = startRow; r < endRow; ++r) {
            for (int c = startCol; c < endCol; ++c) {
                visitor.visit(r, c, get(r, c));
            }
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void visitColWise(MatrixEntryVisitor visitor, int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        for (int c = startCol; c < endCol; ++c) {
            for (int r = startRow; r < endRow; ++r) {
                visitor.visit(r, c, get(r, c));
            }
        }
    }

    @Override
    public void visitRows(MatrixVectorVisitor visitor, int startRow, int endRow) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        for (int r = startRow; r < endRow; ++r) {
            visitor.visit(r, copyRow(r));
        }
    }

    @Override
    public void visitCols(MatrixVectorVisitor visitor, int startCol, int endCol) {
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        for (int c = startCol; c < endCol; ++c) {
            visitor.visit(c, copyCol(c));
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void mutateRowWise(MatrixEntryMutator mutator, int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        for (int r = startRow; r < endRow; ++r) {
            for (int c = startCol; c < endCol; ++c) {
                set(r, c, mutator.mutate(r, c, get(r, c)));
            }
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void mutateColWise(MatrixEntryMutator mutator, int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        for (int c = startCol; c < endCol; ++c) {
            for (int r = startRow; r < endRow; ++r) {
                set(r, c, mutator.mutate(r, c, get(r, c)));
            }
        }
    }

    @Override
    public void mutateRows(MatrixVectorVisitor visitor, int startRow, int endRow) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        for (int r = startRow; r < endRow; ++r) {
            visitor.visit(r, viewRow(r));
        }
    }

    @Override
    public void mutateCols(MatrixVectorVisitor visitor, int startCol, int endCol) {
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        for (int c = startCol; c < endCol; ++c) {
            visitor.visit(c, viewCol(c));
        }
    }
}
