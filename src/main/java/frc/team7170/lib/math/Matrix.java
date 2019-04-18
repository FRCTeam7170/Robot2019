package frc.team7170.lib.math;

// TODO: generalize for other data types (subclasses of Number)
// TODO: add support for inplace operations
// TODO: thread safety
/**
 * <p>
 * A simple matrix interface. It provides basic operations on matrix objects, such as addition, matrix-matrix
 * multiplication, matrix-{@linkplain Vector vector} multiplication, element-wise multiplication, transposition,
 * etcetera. It also provides functionality for viewing and copying subsections of matrices, and functionality for
 * iteratively viewing (with or without mutation) subsections of matrices.
 * </p>
 * <p>
 * No guarantees are made regarding how matrices are backed. Specific implementations must be consulted for those
 * details.
 * </p>
 *
 * @author Robert Russell
 * @see Vector
 */
public interface Matrix {

    /**
     * Get the number of rows in the matrix.
     * @return the number of rows in the matrix.
     */
    int nRows();

    /**
     * Get the number of columns in the matrix.
     * @return the number of columns in the matrix.
     */
    int nCols();

    /**
     * Add this matrix and the given matrix and return the result. This matrix and the given matrix are unchanged.
     * @param other the matrix to add to this matrix.
     * @return the matrix whose elements are the sum of this matrix's and the given matrix's elements.
     * @throws IllegalArgumentException if the dimensions of the given matrix are not congruent with this matrix.
     */
    Matrix add(Matrix other) throws IllegalArgumentException;

    /**
     * Add the given value to each element of this matrix and return the result. This matrix is unchanged.
     * @param value the value to add to each element of this matrix.
     * @return the matrix whose elements are this matrix's elements plus the given value.
     */
    Matrix add(double value);

    /**
     * Subtract the given matrix from this matrix and return the result. This matrix and the given matrix are unchanged.
     * @param other the matrix to subtract from this matrix.
     * @return the matrix whose elements are this matrix's elements less the given matrix's elements.
     * @throws IllegalArgumentException if the dimensions of the given matrix are not congruent with this matrix.
     */
    Matrix subtract(Matrix other) throws IllegalArgumentException;

    /**
     * Subtract the given value from each element of this matrix and return the result. This matrix is unchanged.
     * @param value the value to subtract from each element of this matrix.
     * @return the matrix whose elements are this matrix's elements less the given value.
     */
    default Matrix subtract(double value) {
        return add(-value);
    }

    /**
     * Multiply the given matrix and this matrix and return the result. This is matrix multiplication, <em>not</em>
     * element-wise multiplication. This matrix and the given matrix are unchanged.
     * @param other the matrix to multiply with this matrix.
     * @return this matrix times the given matrix.
     * @throws IllegalArgumentException if the dimensions of the given matrix are not congruent with this matrix.
     */
    Matrix multiply(Matrix other) throws IllegalArgumentException;

    /**
     * Multiply the given {@linkplain Vector vector} and this matrix and return the result. This is matrix-vector
     * multiplication, <em>not</em> element-wise multiplication. This matrix and the given vector are unchanged.
     * @param other the {@linkplain Vector vector} to multiply with this matrix.
     * @return this matrix times the given vector.
     * @throws IllegalArgumentException if the dimensions of the given vector are not congruent with this matrix.
     */
    Matrix multiply(Vector other) throws IllegalArgumentException;

    /**
     * Multiply the given matrix and this matrix and return the result. This is element-wise multiplication,
     * <em>not</em> matrix multiplication. This matrix and the given matrix are unchanged.
     * @param other the matrix to multiply with this matrix.
     * @return the matrix whose elements are this matrix's elements times the given matrix's elements.
     * @throws IllegalArgumentException if the dimensions of the given matrix are not congruent with this matrix.
     */
    Matrix multiplyElementWise(Matrix other) throws IllegalArgumentException;

    /**
     * Multiply each element of this matrix by the given value and return the result. This matrix is unchanged.
     * @param value the value to multiply each element of this matrix by.
     * @return the matrix whose elements are this matrix's elements times the given value.
     */
    Matrix multiply(double value);

    /**
     * Divide each element of this matrix by the given value and return the result. This matrix is unchanged.
     * @param value the value to divide each element of this matrix by.
     * @return the matrix whose elements are this matrix's elements divided by the given value.
     */
    default Matrix divide(double value) {
        return multiply(1.0 / value);
    }

    /**
     * Raise each element of this matrix to the given power and return the result. This matrix is unchanged.
     * @param power the power to raise each element of this matrix to.
     * @return the matrix whose elements are this matrix's elements raised to the given power.
     */
    Matrix power(double power);

    /**
     * Determine and return the transpose of this matrix. This returns a matrix backed by new data, <em>not</em> a view
     * into the old matrix. This matrix is unchanged.
     * @return the transpose of this matrix.
     */
    Matrix transpose();

    /**
     * Get the element at the given row and column indices.
     * @param row the row index, zero-indexed. A negative index is interpreted as starting from the right.
     * @param col the column index, zero-indexed. A negative index is interpreted as starting from the bottom.
     * @return the element at the given indices.
     * @throws IndexOutOfBoundsException if either the row or column index are greater than or equal to the
     * {@linkplain Matrix#nRows() height} or {@linkplain Matrix#nCols() width} of this matrix, respectively.
     */
    double get(int row, int col) throws IndexOutOfBoundsException;

    /**
     * Set the element at the given row and column indices to the given value.
     * @param row the row index, zero-indexed. A negative index is interpreted as starting from the right.
     * @param col the column index, zero-indexed. A negative index is interpreted as starting from the bottom.
     * @param value the value to set at the given indices.
     * @throws IndexOutOfBoundsException if either the row or column index are greater than or equal to the
     * {@linkplain Matrix#nRows() height} or {@linkplain Matrix#nCols() width} of this matrix, respectively.
     */
    void set(int row, int col, double value) throws IndexOutOfBoundsException;

    /**
     * Convert the matrix into a nested array equivalent. This returns a new array, <em>not</em> the array backing this
     * matrix, should this matrix be backed by an array.
     * @return a nested array equivalent of this matrix.
     */
    double[][] toArray();

    /**
     * Set the row at the given row index to the given {@linkplain Vector vector}.
     * @param rowIdx the row index, zero-indexed. A negative index is interpreted as starting from the bottom.
     * @param row the {@linkplain Vector vector} to set at the given row index.
     * @throws IndexOutOfBoundsException if the row index is greater than or equal to the
     * {@linkplain Matrix#nRows() height} of the matrix.
     * @throws IllegalArgumentException if the dimensions of the given vector are not congruent with this matrix.
     */
    void setRow(int rowIdx, Vector row) throws IndexOutOfBoundsException, IllegalArgumentException;

    /**
     * Set the column at the given column index to the given {@linkplain Vector vector}.
     * @param colIdx the column index, zero-indexed. A negative index is interpreted as starting from the right.
     * @param col the {@linkplain Vector vector} to set at the given column index.
     * @throws IndexOutOfBoundsException if the column index is greater than or equal to the
     * {@linkplain Matrix#nCols() width} of the matrix.
     * @throws IllegalArgumentException if the dimensions of the given vector are not congruent with this matrix.
     */
    void setCol(int colIdx, Vector col) throws IndexOutOfBoundsException, IllegalArgumentException;

    /**
     * <p>
     * Generate a view of this matrix spanning from a given top-left index to a given bottom-right index. Any changes to
     * the returned view matrix will be reflected in this matrix and vice versa.
     * </p>
     * <p>
     * No guarantees are made regarding how this view is backed. It may be backed row-wise, in which case row operations
     * may be quicker; or it may be backed column-wise, in which case column operations may be quicker. If efficiency
     * when using views is important, one must consider the implementation of the matrix/view. A possible approach to
     * increase speed when performing many operations that disagree with the ordering of the backing data structure of
     * the matrix might be to first get the {@linkplain Matrix#transpose() transpose} of matrix, perform the operations,
     * and then re-transpose the matrix.
     * </p>
     * @param startRow the row index of the top-left position in the matrix, zero-indexed and inclusive. A negative
     *                 index is interpreted as starting from the right.
     * @param startCol the column index of the top-left position in the matrix, zero-indexed and inclusive. A negative
     *                 index is interpreted as starting from the bottom.
     * @param endRow the row index of the bottom-right position in the matrix, zero-indexed and exclusive. A negative
     *               index is interpreted as starting from the right.
     * @param endCol the column index of the bottom-right position in the matrix, zero-indexed and exclusive. A negative
     *               index is interpreted as starting from the bottom.
     * @return a view of this matrix spanning from a given top-left index to a given bottom-right index.
     */
    Matrix view(int startRow, int startCol, int endRow, int endCol);

    /**
     * <p>
     * Generate a view of this matrix including the given rows and columns. Any changes to the returned view matrix will
     * be reflected in this matrix and vice versa.
     * </p>
     * <p>
     * No guarantees are made regarding how this view is backed. It may be backed row-wise, in which case row operations
     * may be quicker; or it may be backed column-wise, in which case column operations may be quicker. If efficiency
     * when using views is important, one must consider the implementation of the matrix/view. A possible approach to
     * increase speed when performing many operations that disagree with the ordering of the backing data structure of
     * the matrix might be to first get the {@linkplain Matrix#transpose() transpose} of matrix, perform the operations,
     * and then re-transpose the matrix.
     * </p>
     * @param rows the row indices to include in the view, zero-indexed. Negative indices are interpreted as starting
     *             from the right.
     * @param cols the column indices to include in the view, zero-indexed. Negative indices are interpreted as starting
     *             from the bottom.
     * @return a view of this matrix including the given rows and columns.
     * @throws IndexOutOfBoundsException if any of the row or column indices are greater than or equal to the
     * {@linkplain Matrix#nRows() height} or {@linkplain Matrix#nCols() width} of the matrix, respectively.
     */
    Matrix view(int[] rows, int[] cols) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Generate a view of this matrix consisting of a single, given row. Any changes to the returned view
     * {@linkplain Vector vector} will be reflected in this matrix and vice versa.
     * </p>
     * <p>
     * No guarantees are made regarding how this view is backed. It may be backed row-wise, in which case row operations
     * may be quicker; or it may be backed column-wise, in which case column operations may be quicker. If efficiency
     * when using views is important, one must consider the implementation of the matrix/view. A possible approach to
     * increase speed when performing many operations that disagree with the ordering of the backing data structure of
     * the matrix might be to first get the {@linkplain Matrix#transpose() transpose} of matrix, perform the operations,
     * and then re-transpose the matrix.
     * </p>
     * @param row the row index of the view, zero-indexed. A negative index is interpreted as starting from the bottom.
     * @return a view of this matrix consisting of a single, given row.
     * @throws IndexOutOfBoundsException if the row index is greater than or equal to the
     * {@linkplain Matrix#nRows() height} of the matrix.
     */
    Vector viewRow(int row) throws IndexOutOfBoundsException;

    /**
     * <p>
     * Generate a view of this matrix consisting of a single, given column. Any changes to the returned view
     * {@linkplain Vector vector} will be reflected in this matrix and vice versa.
     * </p>
     * <p>
     * No guarantees are made regarding how this view is backed. It may be backed row-wise, in which case row operations
     * may be quicker; or it may be backed column-wise, in which case column operations may be quicker. If efficiency
     * when using views is important, one must consider the implementation of the matrix/view. A possible approach to
     * increase speed when performing many operations that disagree with the ordering of the backing data structure of
     * the matrix might be to first get the {@linkplain Matrix#transpose() transpose} of matrix, perform the operations,
     * and then re-transpose the matrix.
     * </p>
     * @param col the column index of the view, zero-indexed. A negative index is interpreted as starting from the
     *            right.
     * @return a view of this matrix consisting of a single, given column.
     * @throws IndexOutOfBoundsException if the column index is greater than or equal to the
     * {@linkplain Matrix#nCols() width} of the matrix.
     */
    Vector viewCol(int col) throws IndexOutOfBoundsException;

    /**
     * Generate a copy of this matrix spanning from a given top-left index to a given bottom-right index.
     * @param startRow the row index of the top-left position in the matrix, zero-indexed and inclusive. A negative
     *                 index is interpreted as starting from the right.
     * @param startCol the column index of the top-left position in the matrix, zero-indexed and inclusive. A negative
     *                 index is interpreted as starting from the bottom.
     * @param endRow the row index of the bottom-right position in the matrix, zero-indexed and exclusive. A negative
     *               index is interpreted as starting from the right.
     * @param endCol the column index of the bottom-right position in the matrix, zero-indexed and exclusive. A negative
     *               index is interpreted as starting from the bottom.
     * @return a copy of this matrix spanning from a given top-left index to a given bottom-right index.
     */
    Matrix copy(int startRow, int startCol, int endRow, int endCol);

    /**
     * Generate a copy of this matrix including the given rows and columns.
     * @param rows the row indices to include in the copy, zero-indexed. Negative indices are interpreted as starting
     *             from the right.
     * @param cols the column indices to include in the copy, zero-indexed. Negative indices are interpreted as starting
     *             from the bottom.
     * @return a copy of this matrix including the given rows and columns.
     * @throws IndexOutOfBoundsException if any of the row or column indices are greater than or equal to the
     * {@linkplain Matrix#nRows() height} or {@linkplain Matrix#nCols() width} of the matrix, respectively.
     */
    Matrix copy(int[] rows, int[] cols) throws IndexOutOfBoundsException;

    /**
     * Copy this entire matrix.
     * @return a copy of this entire matrix.
     */
    default Matrix copy() {
        return copy(0, 0, nRows(), nCols());
    }

    /**
     * Generate a copy of part of this matrix consisting of a single, given row.
     * @param row the row index to be copied, zero-indexed. A negative index is interpreted as starting from the bottom.
     * @return a copy of part of this matrix consisting of a single, given row.
     * @throws IndexOutOfBoundsException if the row index is greater than or equal to the
     * {@linkplain Matrix#nRows() height} of the matrix.
     */
    Vector copyRow(int row) throws IndexOutOfBoundsException;

    /**
     * Generate a copy of part of this matrix consisting of a single, given column.
     * @param col the column index to be copied, zero-indexed. A negative index is interpreted as starting from the
     *            right.
     * @return a copy of part of this matrix consisting of a single, given column.
     * @throws IndexOutOfBoundsException if the column index is greater than or equal to the
     * {@linkplain Matrix#nCols() width} of the matrix.
     */
    Vector copyCol(int col) throws IndexOutOfBoundsException;

    /**
     * <p>
     * For each element of the matrix between the given top-left index and the given bottom-right index,
     * {@link MatrixEntryVisitor#visit(int, int, double) call the visitor}. Note the indices do not necessarily define
     * a rectangular subsection to iterate over, as is the case with the copy and view methods with a similar signature.
     * For example, given a matrix of the following shape:
     * <table>
     *     <tr>{@code O O O O}</tr>
     *     <tr>{@code O O O O}</tr>
     *     <tr>{@code O O O O}</tr>
     *     <tr>{@code O O O O}</tr>
     * </table>
     * the call {@code matrix.visitRowWise(..., 0, 1, 2, 2)} would iterate over these elements (marked 'X'):
     * <table>
     *     <tr>{@code O X X X}</tr>
     *     <tr>{@code X X X X}</tr>
     *     <tr>{@code X X O O}</tr>
     *     <tr>{@code O O O O}</tr>
     * </table>
     * In order to achieve more complex iteration patterns, use this method in conjunction with the view methods. For
     * example, to iterate over those elements marked 'X' minus the first and last column, one might write
     * {@code matrix.view(0, 1, 4, 3).visitRowWise(..., 0, 0, 2, 1)}.
     * </p>
     * <p>
     * This traverses the matrix along the rows, jumping to the next row once the last column in the current row is
     * reached.
     * </p>
     * @param visitor the visitor.
     * @param startRow the row index of the top-left position in the matrix, zero-indexed and inclusive. A negative
     *                 index is interpreted as starting from the right.
     * @param startCol the column index of the top-left position in the matrix, zero-indexed and inclusive. A negative
     *                 index is interpreted as starting from the bottom.
     * @param endRow the row index of the bottom-right position in the matrix, zero-indexed and exclusive. A negative
     *               index is interpreted as starting from the right.
     * @param endCol the column index of the bottom-right position in the matrix, zero-indexed and exclusive. A negative
     *               index is interpreted as starting from the bottom.
     */
    void visitRowWise(MatrixEntryVisitor visitor, int startRow, int startCol, int endRow, int endCol);

    /**
     * <p>
     * For each element of the matrix between the given top-left index and the given bottom-right index,
     * {@link MatrixEntryVisitor#visit(int, int, double) call the visitor}. Note the indices do not necessarily define
     * a rectangular subsection to iterate over, as is the case with the copy and view methods with a similar signature.
     * For example, given a matrix of the following shape:
     * <table>
     *     <tr>{@code O O O O}</tr>
     *     <tr>{@code O O O O}</tr>
     *     <tr>{@code O O O O}</tr>
     *     <tr>{@code O O O O}</tr>
     * </table>
     * the call {@code matrix.visitColWise(..., 1, 0, 2, 2)} would iterate over these elements (marked 'X'):
     * <table>
     *     <tr>{@code O X X O}</tr>
     *     <tr>{@code X X X O}</tr>
     *     <tr>{@code X X O O}</tr>
     *     <tr>{@code X X O O}</tr>
     * </table>
     * In order to achieve more complex iteration patterns, use this method in conjunction with the view methods. For
     * example, to iterate over those elements marked 'X' minus the first and last row, one might write
     * {@code matrix.view(1, 0, 3, 4).visitColWise(..., 0, 0, 1, 2)}.
     * </p>
     * <p>
     * This traverses the matrix along the columns, jumping to the next column once the last row in the current column
     * is reached.
     * </p>
     * @param visitor the visitor.
     * @param startRow the row index of the top-left position in the matrix, zero-indexed and inclusive. A negative
     *                 index is interpreted as starting from the right.
     * @param startCol the column index of the top-left position in the matrix, zero-indexed and inclusive. A negative
     *                 index is interpreted as starting from the bottom.
     * @param endRow the row index of the bottom-right position in the matrix, zero-indexed and exclusive. A negative
     *               index is interpreted as starting from the right.
     * @param endCol the column index of the bottom-right position in the matrix, zero-indexed and exclusive. A negative
     *               index is interpreted as starting from the bottom.
     */
    void visitColWise(MatrixEntryVisitor visitor, int startRow, int startCol, int endRow, int endCol);

    /**
     * For each row of the given rows in the matrix,
     * {@link MatrixVectorVisitor#visit(int, Vector)} call the visitor}.
     * @param visitor the visitor.
     * @param startRow the row index to start at, zero-indexed and inclusive. A negative index is interpreted as
     *                 starting from the bottom.
     * @param endRow the row index to end at, zero-indexed and exclusive. A negative index is interpreted as starting
     *               from the bottom.
     */
    void visitRows(MatrixVectorVisitor visitor, int startRow, int endRow);

    /**
     * For each column of the given columns in the matrix,
     * {@link MatrixVectorVisitor#visit(int, Vector)} call the visitor}.
     * @param visitor the visitor.
     * @param startCol the column index to start at, zero-indexed and inclusive. A negative index is interpreted as
     *                 starting from the right.
     * @param endCol the column index to end at, zero-indexed and exclusive. A negative index is interpreted as starting
     *               from the right.
     */
    void visitCols(MatrixVectorVisitor visitor, int startCol, int endCol);

    /**
     * For each element of the matrix,
     * {@link MatrixEntryVisitor#visit(int, int, double) call the visitor}. This traverses the matrix along the rows,
     * jumping to the next row once the last column in the current row is reached.
     * @param visitor the visitor.
     */
    default void visitRowWise(MatrixEntryVisitor visitor) {
        visitRowWise(visitor, 0, 0, nRows(), nCols());
    }

    /**
     * For each element of the matrix,
     * {@link MatrixEntryVisitor#visit(int, int, double) call the visitor}. This traverses the matrix along the
     * columns, jumping to the next column once the last row in the current column is reached.
     * @param visitor the visitor.
     */
    default void visitColWise(MatrixEntryVisitor visitor) {
        visitColWise(visitor, 0, 0, nRows(), nCols());
    }

    /**
     * For each row in the matrix,
     * {@link MatrixVectorVisitor#visit(int, Vector)} call the visitor}.
     * @param visitor the visitor.
     */
    default void visitRows(MatrixVectorVisitor visitor) {
        visitRows(visitor, 0, nRows());
    }

    /**
     * For each column in the matrix,
     * {@link MatrixVectorVisitor#visit(int, Vector)} call the visitor}.
     * @param visitor the visitor.
     */
    default void visitCols(MatrixVectorVisitor visitor) {
        visitCols(visitor, 0, nCols());
    }

    /**
     * <p>
     * For each element of the matrix between the given top-left index and the given bottom-right index,
     * {@link MatrixEntryMutator#mutate(int, int, double)} call the mutator}. Note the indices do not necessarily define
     * a rectangular subsection to iterate over, as is the case with the copy and view methods with a similar signature.
     * For example, given a matrix of the following shape:
     * <table>
     *     <tr>{@code O O O O}</tr>
     *     <tr>{@code O O O O}</tr>
     *     <tr>{@code O O O O}</tr>
     *     <tr>{@code O O O O}</tr>
     * </table>
     * the call {@code matrix.mutateRowWise(..., 0, 1, 2, 2)} would iterate over these elements (marked 'X'):
     * <table>
     *     <tr>{@code O X X X}</tr>
     *     <tr>{@code X X X X}</tr>
     *     <tr>{@code X X O O}</tr>
     *     <tr>{@code O O O O}</tr>
     * </table>
     * In order to achieve more complex iteration patterns, use this method in conjunction with the view methods. For
     * example, to iterate over those elements marked 'X' minus the first and last column, one might write
     * {@code matrix.view(0, 1, 4, 3).mutateRowWise(..., 0, 0, 2, 1)}.
     * </p>
     * <p>
     * This traverses the matrix along the rows, jumping to the next row once the last column in the current row is
     * reached.
     * </p>
     * @param mutator the mutator.
     * @param startRow the row index of the top-left position in the matrix, zero-indexed and inclusive. A negative
     *                 index is interpreted as starting from the right.
     * @param startCol the column index of the top-left position in the matrix, zero-indexed and inclusive. A negative
 *                 index is interpreted as starting from the bottom.
     * @param endRow the row index of the bottom-right position in the matrix, zero-indexed and exclusive. A negative
*               index is interpreted as starting from the right.
     * @param endCol the column index of the bottom-right position in the matrix, zero-indexed and exclusive. A negative
     */
    void mutateRowWise(MatrixEntryMutator mutator, int startRow, int startCol, int endRow, int endCol);

    /**
     * <p>
     * For each element of the matrix between the given top-left index and the given bottom-right index,
     * {@link MatrixEntryMutator#mutate(int, int, double) call the mutator}. Note the indices do not necessarily define
     * a rectangular subsection to iterate over, as is the case with the copy and view methods with a similar signature.
     * For example, given a matrix of the following shape:
     * <table>
     *     <tr>{@code O O O O}</tr>
     *     <tr>{@code O O O O}</tr>
     *     <tr>{@code O O O O}</tr>
     *     <tr>{@code O O O O}</tr>
     * </table>
     * the call {@code matrix.mutateColWise(..., 1, 0, 2, 2)} would iterate over these elements (marked 'X'):
     * <table>
     *     <tr>{@code O X X O}</tr>
     *     <tr>{@code X X X O}</tr>
     *     <tr>{@code X X O O}</tr>
     *     <tr>{@code X X O O}</tr>
     * </table>
     * In order to achieve more complex iteration patterns, use this method in conjunction with the view methods. For
     * example, to iterate over those elements marked 'X' minus the first and last row, one might write
     * {@code matrix.view(1, 0, 3, 4).mutateColWise(..., 0, 0, 1, 2)}.
     * </p>
     * <p>
     * This traverses the matrix along the columns, jumping to the next column once the last row in the current column
     * is reached.
     * </p>
     * @param mutator the mutator.
     * @param startRow the row index of the top-left position in the matrix, zero-indexed and inclusive. A negative
     *                 index is interpreted as starting from the right.
     * @param startCol the column index of the top-left position in the matrix, zero-indexed and inclusive. A negative
     *                 index is interpreted as starting from the bottom.
     * @param endRow the row index of the bottom-right position in the matrix, zero-indexed and exclusive. A negative
     *               index is interpreted as starting from the right.
     * @param endCol the column index of the bottom-right position in the matrix, zero-indexed and exclusive. A negative
     *               index is interpreted as starting from the bottom.
     */
    void mutateColWise(MatrixEntryMutator mutator, int startRow, int startCol, int endRow, int endCol);

    /**
     * For each row of the given rows in the matrix,
     * {@link MatrixVectorMutator#mutate(int, Vector)} call the mutator}.
     * @param mutator the mutator.
     * @param startRow the row index to start at, zero-indexed and inclusive. A negative index is interpreted as
     *                 starting from the bottom.
     * @param endRow the row index to end at, zero-indexed and exclusive. A negative index is interpreted as starting
     *               from the bottom.
     */
    void mutateRows(MatrixVectorMutator mutator, int startRow, int endRow);

    /**
     * For each column of the given columns in the matrix,
     * {@link MatrixVectorMutator#mutate(int, Vector)} call the mutator}.
     * @param mutator the mutator.
     * @param startCol the column index to start at, zero-indexed and inclusive. A negative index is interpreted as
     *                 starting from the right.
     * @param endCol the column index to end at, zero-indexed and exclusive. A negative index is interpreted as starting
     *               from the right.
     */
    void mutateCols(MatrixVectorMutator mutator, int startCol, int endCol);

    /**
     * For each element of the matrix,
     * {@link MatrixEntryMutator#mutate(int, int, double) call the mutator}. This traverses the matrix along the rows,
     * jumping to the next row once the last column in the current row is reached.
     * @param mutator the mutator.
     */
    default void mutateRowWise(MatrixEntryMutator mutator) {
        mutateRowWise(mutator, 0, 0, nRows(), nCols());
    }

    /**
     * For each element of the matrix,
     * {@link MatrixEntryMutator#mutate(int, int, double) call the mutator}. This traverses the matrix along the
     * columns, jumping to the next column once the last row in the current column is reached.
     * @param mutator the mutator.
     */
    default void mutateColWise(MatrixEntryMutator mutator) {
        mutateColWise(mutator, 0, 0, nRows(), nCols());
    }

    /**
     * For each row in the matrix,
     * {@link MatrixVectorMutator#mutate(int, Vector)} call the mutator}.
     * @param mutator the mutator.
     */
    default void mutateRows(MatrixVectorMutator mutator) {
        mutateRows(mutator, 0, nRows());
    }

    /**
     * For each column in the matrix,
     * {@link MatrixVectorMutator#mutate(int, Vector)} call the mutator}.
     * @param mutator the mutator.
     */
    default void mutateCols(MatrixVectorMutator mutator) {
        mutateCols(mutator, 0, nCols());
    }

    /**
     * The functional interface used for all {@code matrix.visitXWise} methods (X is "Row" or "Col").
     */
    @FunctionalInterface
    interface MatrixEntryVisitor {
        /**
         * Called for each element iterated over for scalar value visit methods.
         * @param rowIdx the current row index.
         * @param colIdx the current column index.
         * @param value the value at the row and column indices.
         */
        void visit(int rowIdx, int colIdx, double value);
    }

    /**
     * The functional interface used for all {@code matrix.mutateXWise} methods (X is "Row" or "Col").
     */
    @FunctionalInterface
    interface MatrixEntryMutator {
        /**
         * Called for each element iterated over for scalar value mutate methods.
         * @param rowIdx the current row index.
         * @param colIdx the current column index.
         * @param value the value at the row and column indices.
         * @return the new value to assign at the row and column indices.
         */
        double mutate(int rowIdx, int colIdx, double value);
    }

    /**
     * The functional interface used for all {@code matrix.visitX} methods (X is "Row" or "Col").
     */
    @FunctionalInterface
    interface MatrixVectorVisitor {
        /**
         * Called for each element iterated over for vector value visit methods.
         * @param idx the current row/column index.
         * @param vector the row/column at the index.
         */
        void visit(int idx, Vector vector);
    }

    /**
     * The functional interface used for all {@code matrix.mutateX} methods (X is "Row" or "Col").
     */
    @FunctionalInterface
    interface MatrixVectorMutator {
        /**
         * Called for each element iterated over for vector value mutate methods.
         * @param idx the current row/column index.
         * @param vector the row/column at the index.
         * @return the new vector to assign at the index.
         */
        Vector mutate(int idx, Vector vector);
    }
}
