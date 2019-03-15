package frc.team7170.lib.math;

public interface Matrix {

    int nRows();

    int nCols();

    Matrix add(Matrix other) throws IllegalArgumentException;

    Matrix add(double value);

    Matrix subtract(Matrix other) throws IllegalArgumentException;

    default Matrix subtract(double value) {
        return add(-value);
    }

    Matrix multiply(Matrix other) throws IllegalArgumentException;

    Matrix multiply(Vector other) throws IllegalArgumentException;

    Matrix multiplyElementWise(Matrix other);

    Matrix multiply(double value);

    default Matrix divide(double value) {
        return multiply(1.0 / value);
    }

    Matrix power(double power);

    Matrix transpose();

    double get(int row, int col) throws IndexOutOfBoundsException;

    void set(int row, int col, double value) throws IndexOutOfBoundsException;

    double[][] toArray();

    void setRow(int rowIdx, Vector row) throws IndexOutOfBoundsException, IllegalArgumentException;

    void setCol(int colIdx, Vector col) throws IndexOutOfBoundsException, IllegalArgumentException;

    Matrix view(int startRow, int startCol, int endRow, int endCol);

    Matrix view(int[] rows, int[] cols) throws IndexOutOfBoundsException;

    Vector viewRow(int row) throws IndexOutOfBoundsException;

    Vector viewCol(int col) throws IndexOutOfBoundsException;

    Matrix copy(int startRow, int startCol, int endRow, int endCol);

    Matrix copy(int[] rows, int[] cols) throws IndexOutOfBoundsException;

    default Matrix copy() {
        return copy(0, 0, nRows(), nCols());
    }

    Vector copyRow(int row) throws IndexOutOfBoundsException;

    Vector copyCol(int col) throws IndexOutOfBoundsException;

    void visitRowWise(MatrixEntryVisitor visitor, int startRow, int startCol, int endRow, int endCol);

    void visitColWise(MatrixEntryVisitor visitor, int startRow, int startCol, int endRow, int endCol);

    void visitRows(MatrixVectorVisitor visitor, int startRow, int endRow);

    void visitCols(MatrixVectorVisitor visitor, int startCol, int endCol);

    default void visitRowWise(MatrixEntryVisitor visitor) {
        visitRowWise(visitor, 0, 0, nRows(), nCols());
    }

    default void visitColWise(MatrixEntryVisitor visitor) {
        visitColWise(visitor, 0, 0, nRows(), nCols());
    }

    default void visitRows(MatrixVectorVisitor visitor) {
        visitRows(visitor, 0, nRows());
    }

    default void visitCols(MatrixVectorVisitor visitor) {
        visitCols(visitor, 0, nCols());
    }

    void mutateRowWise(MatrixEntryMutator visitor, int startRow, int startCol, int endRow, int endCol);

    void mutateColWise(MatrixEntryMutator visitor, int startRow, int startCol, int endRow, int endCol);

    void mutateRows(MatrixVectorVisitor visitor, int startRow, int endRow);

    void mutateCols(MatrixVectorVisitor visitor, int startCol, int endCol);

    default void mutateRowWise(MatrixEntryMutator visitor) {
        mutateRowWise(visitor, 0, 0, nRows(), nCols());
    }

    default void mutateColWise(MatrixEntryMutator visitor) {
        mutateColWise(visitor, 0, 0, nRows(), nCols());
    }

    default void mutateRows(MatrixVectorVisitor visitor) {
        mutateRows(visitor, 0, nRows());
    }

    default void mutateCols(MatrixVectorVisitor visitor) {
        mutateCols(visitor, 0, nCols());
    }

    @FunctionalInterface
    interface MatrixEntryVisitor {
        void accept(int rowIdx, int colIdx, double value);
    }

    @FunctionalInterface
    interface MatrixEntryMutator {
        double accept(int rowIdx, int colIdx, double value);
    }

    @FunctionalInterface
    interface MatrixVectorVisitor {
        void accept(int idx, Vector vector);
    }
}