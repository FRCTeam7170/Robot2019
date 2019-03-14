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
        // TODO: move this to an ArrayUtil class
        double[][] otherData = other.toArray();
        double[][] newData = new double[nRows()][nCols()];
        for (int r = 0; r < nRows(); ++r) {
            for (int c = 0; c < nCols(); ++c) {
                newData[r][c] = data[r][c] + otherData[r][c];
            }
        }
        return new MatrixImpl(newData);
    }

    @Override
    public Matrix add(double value) {
        return null;
    }

    @Override
    public Matrix subtract(Matrix other) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Matrix multiply(Matrix other) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Matrix multiply(Vector other) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Matrix multiplyElementWise(Matrix other) {
        return null;
    }

    @Override
    public Matrix multiply(double value) {
        return null;
    }

    @Override
    public Matrix power(double power) {
        return null;
    }

    @Override
    public Matrix transpose() {
        return null;
    }

    @Override
    public double get(int row, int col) throws IndexOutOfBoundsException {
        return data[row][col];
    }

    @Override
    public void set(int row, int col, double value) throws IndexOutOfBoundsException {
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
    public void setRow(int rowIdx, Vector row) throws IndexOutOfBoundsException {

    }

    @Override
    public void setCol(int colIdx, Vector col) throws IndexOutOfBoundsException {

    }

    @Override
    public Matrix view(int lowerRow, int upperRow, int lowerCol, int upperCol) {
        return null;
    }

    @Override
    public Matrix view(int[] rows, int[] cols) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public Vector rowView(int row) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public Vector colView(int col) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public Matrix copy(int lowerRow, int upperRow, int lowerCol, int upperCol) {
        return null;
    }

    @Override
    public Matrix copy(int[] rows, int[] cols) throws IndexOutOfBoundsException {
        double[][] newData = new double[rows.length][];
        for (int i = 0; i < rows.length; ++i) {
            // newData[i] = Arrays.copyOfRange(data[])
        }
        return null;
    }

    @Override
    public Vector copyRow(int row) throws IndexOutOfBoundsException {
        return new VectorImpl(Arrays.copyOf(data[CalcUtil.rectifyArrayIndexRestrictive(row, nRows())], nCols()));
    }

    // TODO: moderately expensive operation--cache result?
    @Override
    public Vector copyCol(int col) throws IndexOutOfBoundsException {
        col = CalcUtil.rectifyArrayIndexRestrictive(col, nCols());
        double[] colData = new double[nRows()];
        for (int r = 0; r < nRows(); ++r) {
            colData[r] = data[r][col];
        }
        return new VectorImpl(colData);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void visitRowWise(MatrixEntryVisitor visitor, int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        for (int r = startRow; r < endRow; ++r) {
            for (int c = startCol; c < endCol; ++c) {
                visitor.accept(r, c, get(r, c));
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
                visitor.accept(r, c, get(r, c));
            }
        }
    }

    @Override
    public void visitRows(MatrixVectorVisitor visitor, int startRow, int endRow) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        for (int r = startRow; r < endRow; ++r) {
            visitor.accept(r, copyRow(r));
        }
    }

    @Override
    public void visitCols(MatrixVectorVisitor visitor, int startCol, int endCol) {
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        for (int c = startCol; c < endCol; ++c) {
            visitor.accept(c, copyCol(c));
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void mutateRowWise(MatrixEntryMutator visitor, int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        for (int r = startRow; r < endRow; ++r) {
            for (int c = startCol; c < endCol; ++c) {
                set(r, c, visitor.accept(r, c, get(r, c)));
            }
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void mutateColWise(MatrixEntryMutator visitor, int startRow, int startCol, int endRow, int endCol) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        for (int c = startCol; c < endCol; ++c) {
            for (int r = startRow; r < endRow; ++r) {
                set(r, c, visitor.accept(r, c, get(r, c)));
            }
        }
    }

    @Override
    public void mutateRows(MatrixVectorVisitor visitor, int startRow, int endRow) {
        startRow = CalcUtil.rectifyArrayIndex(startRow, nRows());
        endRow = CalcUtil.rectifyArrayIndex(endRow, nRows());
        for (int r = startRow; r < endRow; ++r) {
            visitor.accept(r, rowView(r));
        }
    }

    @Override
    public void mutateCols(MatrixVectorVisitor visitor, int startCol, int endCol) {
        startCol = CalcUtil.rectifyArrayIndex(startCol, nCols());
        endCol = CalcUtil.rectifyArrayIndex(endCol, nCols());
        for (int c = startCol; c < endCol; ++c) {
            visitor.accept(c, colView(c));
        }
    }
}
