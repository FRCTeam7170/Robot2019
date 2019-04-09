package frc.team7170.lib.math;

import java.util.Arrays;

/**
 * A simple matrix implementation using a row-major nested array as the backing data structure.
 */
public class ArrayMatrix implements Matrix {

    private final double[][] data;

    /**
     * @param nRows the number of rows in the matrix.
     * @param nCols the number of columns in the matrix.
     */
    public ArrayMatrix(int nRows, int nCols) {
        data = new double[nRows][nCols];
    }

    /**
     * @param data the nested array to initialize the matrix with.
     */
    public ArrayMatrix(double[][] data) {
        // TODO: should this make a copy of the array?
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
        return new ArrayMatrix(newData);
    }

    @Override
    public Matrix add(double value) {
        double[][] newData = new double[nRows()][nCols()];
        visitRowWise((r, c, thisValue) -> newData[r][c] = thisValue + value);
        return new ArrayMatrix(newData);
    }

    @Override
    public Matrix subtract(Matrix other) throws IllegalArgumentException {
        if (!matchingSize(other)) {
            throw new IllegalArgumentException("matrix size must be identical for subtraction");
        }
        double[][] newData = new double[nRows()][nCols()];
        visitRowWise((r, c, value) -> newData[r][c] = value - other.get(r, c));
        return new ArrayMatrix(newData);
    }

    @Override
    public Matrix multiply(Matrix other) throws IllegalArgumentException {
        if (nCols() != other.nRows()) {
            throw new IllegalArgumentException("invalid matrix size for matrix multiplication");
        }
        double[][] result = new double[nRows()][other.nCols()];
        visitRows((rowIdx, row) -> other.visitCols((colIdx, col) -> result[rowIdx][colIdx] = row.dot(col)));
        return new ArrayMatrix(result);
    }

    @Override
    public Matrix multiply(Vector other) throws IllegalArgumentException {
        if (nCols() != other.length()) {
            throw new IllegalArgumentException("invalid vector length for matrix-vector multiplication");
        }
        return multiply(other.asColMatrix());
    }

    @Override
    public Matrix multiplyElementWise(Matrix other) throws IllegalArgumentException {
        if (!matchingSize(other)) {
            throw new IllegalArgumentException("matrix size must be identical for element-wise multiplication");
        }
        double[][] newData = new double[nRows()][nCols()];
        visitRowWise((r, c, value) -> newData[r][c] = value * other.get(r, c));
        return new ArrayMatrix(newData);
    }

    @Override
    public Matrix multiply(double value) {
        double[][] newData = new double[nRows()][nCols()];
        visitRowWise((r, c, thisValue) -> newData[r][c] = thisValue * value);
        return new ArrayMatrix(newData);
    }

    @Override
    public Matrix power(double power) {
        double[][] newData = new double[nRows()][nCols()];
        visitRowWise((r, c, value) -> newData[r][c] = Math.pow(value, power));
        return new ArrayMatrix(newData);
    }

    @Override
    public Matrix transpose() {
        double[][] result = new double[nCols()][nRows()];
        visitRowWise((r, c, value) -> result[c][r] = value);
        return new ArrayMatrix(result);
    }

    @Override
    public double get(int row, int col) throws IndexOutOfBoundsException {
        row = CalcUtil.normalizeArrayIndexRestrictive(row, nRows());
        col = CalcUtil.normalizeArrayIndexRestrictive(col, nCols());
        return data[row][col];
    }

    @Override
    public void set(int row, int col, double value) throws IndexOutOfBoundsException {
        row = CalcUtil.normalizeArrayIndexRestrictive(row, nRows());
        col = CalcUtil.normalizeArrayIndexRestrictive(col, nCols());
        data[row][col] = value;
    }

    @Override
    public double[][] toArray() {
        // Ideally we'd use visitRows here to minimize direct access to data, but that would lead to copying each row of
        // the matrix twice.
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
        rowIdx = CalcUtil.normalizeArrayIndexRestrictive(rowIdx, nRows());
        data[rowIdx] = row.toArray();
    }

    @Override
    public void setCol(int colIdx, Vector col) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (col.length() != nRows()) {
            throw new IllegalArgumentException("col must be same height as matrix");
        }
        colIdx = CalcUtil.normalizeArrayIndexRestrictive(colIdx, nCols());
        for (int r = 0; r < nRows(); ++r) {
            set(r, colIdx, col.get(r));
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Matrix view(int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.normalizeArrayIndex(startRow, nRows());
        startCol = CalcUtil.normalizeArrayIndex(startCol, nCols());
        endRow = CalcUtil.normalizeArrayIndex(endRow, nRows());
        endCol = CalcUtil.normalizeArrayIndex(endCol, nCols());
        return new MatrixView(this, startRow, startCol, endRow, endCol);
    }

    @Override
    public Matrix view(int[] rows, int[] cols) throws IndexOutOfBoundsException {
        for (int r = 0; r < rows.length; ++r) {
            rows[r] = CalcUtil.normalizeArrayIndexRestrictive(rows[r], nRows());
        }
        for (int c = 0; c < cols.length; ++c) {
            cols[c] = CalcUtil.normalizeArrayIndexRestrictive(cols[c], nCols());
        }
        return new MatrixView(this, rows, cols);
    }

    @Override
    public Vector viewRow(int row) throws IndexOutOfBoundsException {
        return MatrixVectorView.newRowView(this, CalcUtil.normalizeArrayIndexRestrictive(row, nRows()));
    }

    @Override
    public Vector viewCol(int col) throws IndexOutOfBoundsException {
        return MatrixVectorView.newColView(this, CalcUtil.normalizeArrayIndexRestrictive(col, nCols()));
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Matrix copy(int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.normalizeArrayIndex(startRow, nRows());
        startCol = CalcUtil.normalizeArrayIndex(startCol, nCols());
        endRow = CalcUtil.normalizeArrayIndex(endRow, nRows());
        endCol = CalcUtil.normalizeArrayIndex(endCol, nCols());
        int nRows = endRow - startRow;
        double[][] newData = new double[nRows > 0 ? nRows : 0][];
        if ((endCol - startCol) >= 0) {  // Arrays.copyOfRange(double[], int, int) errors otherwise
            for (int r = startRow; r < endRow; ++r) {
                newData[r - startRow] = Arrays.copyOfRange(data[r], startCol, endCol);
            }
        }
        return new ArrayMatrix(newData);
    }

    @Override
    public Matrix copy(int[] rows, int[] cols) throws IndexOutOfBoundsException {
        double[][] newData = new double[rows.length][cols.length];
        for (int r = 0; r < rows.length; ++r) {
            int row = CalcUtil.normalizeArrayIndexRestrictive(rows[r], nRows());
            for (int c = 0; c < cols.length; ++c) {
                int col = CalcUtil.normalizeArrayIndexRestrictive(cols[c], nCols());
                newData[r][c] = get(row, col);
            }
        }
        return new ArrayMatrix(newData);
    }

    @Override
    public Vector copyRow(int row) throws IndexOutOfBoundsException {
        return new ArrayVector(Arrays.copyOf(data[CalcUtil.normalizeArrayIndexRestrictive(row, nRows())], nCols()));
    }

    @Override
    public Vector copyCol(int col) throws IndexOutOfBoundsException {
        col = CalcUtil.normalizeArrayIndexRestrictive(col, nCols());
        double[] colData = new double[nRows()];
        for (int r = 0; r < nRows(); ++r) {
            colData[r] = get(r, col);
        }
        return new ArrayVector(colData);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void visitRowWise(MatrixEntryVisitor visitor, int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.normalizeArrayIndex(startRow, nRows());
        startCol = CalcUtil.normalizeArrayIndex(startCol, nCols());
        endRow = CalcUtil.normalizeArrayIndex(endRow, nRows());
        endCol = CalcUtil.normalizeArrayIndex(endCol, nCols());
        for (int c = startCol; c < nCols(); ++c) {
            visitor.visit(startRow, c, get(startRow, c));
        }
        for (int r = startRow + 1; r < endRow - 1; ++r) {
            for (int c = 0; c < nCols(); ++c) {
                visitor.visit(r, c, get(r, c));
            }
        }
        for (int c = 0; c < endCol; ++c) {
            visitor.visit(endRow - 1, c, get(endRow - 1, c));
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void visitColWise(MatrixEntryVisitor visitor, int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.normalizeArrayIndex(startRow, nRows());
        startCol = CalcUtil.normalizeArrayIndex(startCol, nCols());
        endRow = CalcUtil.normalizeArrayIndex(endRow, nRows());
        endCol = CalcUtil.normalizeArrayIndex(endCol, nCols());
        for (int r = startRow; r < nRows(); ++r) {
            visitor.visit(r, startCol, get(r, startCol));
        }
        for (int c = startCol + 1; c < endCol - 1; ++c) {
            for (int r = 0; r < nRows(); ++r) {
                visitor.visit(r, c, get(r, c));
            }
        }
        for (int r = 0; r < endRow; ++r) {
            visitor.visit(r, endCol - 1, get(r, endCol - 1));
        }
    }

    @Override
    public void visitRows(MatrixVectorVisitor visitor, int startRow, int endRow) {
        startRow = CalcUtil.normalizeArrayIndex(startRow, nRows());
        endRow = CalcUtil.normalizeArrayIndex(endRow, nRows());
        for (int r = startRow; r < endRow; ++r) {
            visitor.visit(r, copyRow(r));
        }
    }

    @Override
    public void visitCols(MatrixVectorVisitor visitor, int startCol, int endCol) {
        startCol = CalcUtil.normalizeArrayIndex(startCol, nCols());
        endCol = CalcUtil.normalizeArrayIndex(endCol, nCols());
        for (int c = startCol; c < endCol; ++c) {
            visitor.visit(c, copyCol(c));
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void mutateRowWise(MatrixEntryMutator mutator, int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.normalizeArrayIndex(startRow, nRows());
        startCol = CalcUtil.normalizeArrayIndex(startCol, nCols());
        endRow = CalcUtil.normalizeArrayIndex(endRow, nRows());
        endCol = CalcUtil.normalizeArrayIndex(endCol, nCols());
        for (int c = startCol; c < nCols(); ++c) {
            set(startRow, c, mutator.mutate(startRow, c, get(startRow, c)));
        }
        for (int r = startRow + 1; r < endRow - 1; ++r) {
            for (int c = 0; c < nCols(); ++c) {
                set(r, c, mutator.mutate(r, c, get(r, c)));
            }
        }
        for (int c = 0; c < endCol; ++c) {
            set(endRow - 1, c, mutator.mutate(endRow - 1, c, get(endRow - 1, c)));
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void mutateColWise(MatrixEntryMutator mutator, int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.normalizeArrayIndex(startRow, nRows());
        startCol = CalcUtil.normalizeArrayIndex(startCol, nCols());
        endRow = CalcUtil.normalizeArrayIndex(endRow, nRows());
        endCol = CalcUtil.normalizeArrayIndex(endCol, nCols());
        for (int r = startRow; r < nRows(); ++r) {
            set(r, startCol, mutator.mutate(r, startCol, get(r, startCol)));
        }
        for (int c = startCol + 1; c < endCol - 1; ++c) {
            for (int r = 0; r < nRows(); ++r) {
                set(r, c, mutator.mutate(r, c, get(r, c)));
            }
        }
        for (int r = 0; r < endRow; ++r) {
            set(r, endCol - 1, mutator.mutate(r, endCol - 1, get(r, endCol - 1)));
        }
    }

    @Override
    public void mutateRows(MatrixVectorMutator mutator, int startRow, int endRow) {
        startRow = CalcUtil.normalizeArrayIndex(startRow, nRows());
        endRow = CalcUtil.normalizeArrayIndex(endRow, nRows());
        for (int r = startRow; r < endRow; ++r) {
            setRow(r, mutator.mutate(r, copyRow(r)));
        }
    }

    @Override
    public void mutateCols(MatrixVectorMutator mutator, int startCol, int endCol) {
        startCol = CalcUtil.normalizeArrayIndex(startCol, nCols());
        endCol = CalcUtil.normalizeArrayIndex(endCol, nCols());
        for (int c = startCol; c < endCol; ++c) {
            setCol(c, mutator.mutate(c, copyCol(c)));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < nRows(); ++r) {
            for (int c = 0; c < nCols() - 1; ++c) {
                sb.append(get(r, c)).append(" ");
            }
            // So that there's not an extra space on the end of each row
            sb.append(get(r, nCols() - 1));
            if (r != nRows() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
