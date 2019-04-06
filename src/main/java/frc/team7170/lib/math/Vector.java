package frc.team7170.lib.math;

/**
 * <p>
 * A simple vector interface. It provides basic operations on vector objects, such as addition, dot product,
 * element-wise multiplication, conversion to {@linkplain Vector#asRowMatrix() row}- or
 * {@linkplain Vector#asColMatrix() column}-{@linkplain Matrix matrices}, etcetera. It also provides functionality for
 * viewing and copying subsections of vectors, and functionality for iteratively viewing (with or without mutation)
 * subsections of vectors.
 * </p>
 * <p>
 * No guarantees are made regarding how vectors are backed. Specific implementations must be consulted for those
 * details.
 * </p>
 *
 * @author Robert Russell
 * @see Matrix
 */
public interface Vector {

    /**
     * Get the length of this vector.
     * @return the length of this vector.
     */
    int length();

    /**
     * Calculate and return the Euclidean norm of this vector; that is, for a vector
     * <code>v = &lt;x<sub>0</sub>...x<sub>n</sub>&gt;</code>, this method returns
     * <code>&#8730;(x<sub>0</sub>&sup2;...x<sub>n</sub>&sup2;)</code>.
     * @return the Euclidean norm of this vector.
     */
    double norm();

    /**
     * Add this vector and the given vector and return the result. This vector and the given vector are unchanged.
     * @param other the vector to add to this vector.
     * @return the vector whose elements are the sum of this vector's and the given vector's elements.
     * @throws IllegalArgumentException if the dimensions of the given vector are not congruent with this vector.
     */
    Vector add(Vector other) throws IllegalArgumentException;

    /**
     * Add the given value to each element of this vector and return the result. This vector is unchanged.
     * @param value the value to add to each element of this vector.
     * @return the vector whose elements are this vector's elements plus the given value.
     */
    Vector add(double value);

    /**
     * Subtract the given vector from this vector and return the result. This vector and the given vector are unchanged.
     * @param other the vector to subtract from this vector.
     * @return the vector whose elements are this vector's elements less the given vector's elements.
     * @throws IllegalArgumentException if the dimensions of the given vector are not congruent with this vector.
     */
    Vector subtract(Vector other) throws IllegalArgumentException;

    /**
     * Subtract the given value from each element of this vector and return the result. This vector is unchanged.
     * @param value the value to subtract from each element of this vector.
     * @return the vector whose elements are this vector's elements less the given value.
     */
    default Vector subtract(double value) {
        return add(-value);
    }

    /**
     * Scale this vector by the given value and return the result. This vector is unchanged.
     * @param value the value to scale each element of this vector by.
     * @return the vector whose elements are this vector's elements scaled by the given value.
     */
    Vector scale(double value);

    /**
     * Calculate and return the inverse of this vector; that is, this vector scaled by {@code -1}. This vector is
     * unchanged.
     * @return the inverse of this vector.
     */
    default Vector inverse() {
        return scale(-1.0);
    }

    /**
     * Multiply the given vector and this vector and return the result. This is element-wise multiplication,
     * <em>not</em> matrix multiplication or dot product. This vector and the given vector are unchanged.
     * @param other the vector to multiply with this vector.
     * @return the vector whose elements are this vector's elements times the given vector's elements.
     * @throws IllegalArgumentException if the dimensions of the given vector are not congruent with this vector.
     */
    Vector multiplyElementWise(Vector other) throws IllegalArgumentException;

    /**
     * Calculate and return the dot product of this vector and the given vector. This vector and the given vector are
     * unchanged.
     * @param other the vector to "dot with" this vector.
     * @return the dot product of this vector and the given vector.
     * @throws IllegalArgumentException if the dimensions of the given vector are not congruent with this vector.
     */
    double dot(Vector other) throws IllegalArgumentException;

    /**
     * Raise each element of this vector to the given power and return the result. This vector is unchanged.
     * @param power the power to raise each element of this vector to.
     * @return the vector whose elements are this vector's elements raised to the given power.
     */
    Vector power(double power);

    /**
     * Get the element at the given index.
     * @param idx the index, zero-indexed. A negative index is interpreted as starting from the end.
     * @return the element at the given index.
     * @throws IndexOutOfBoundsException if the index is greater than or equal to the
     * {@linkplain Vector#length() length} of this vector.
     */
    double get(int idx) throws IndexOutOfBoundsException;

    /**
     * Set the element at the given index to the given value.
     * @param idx the index, zero-indexed. A negative index is interpreted as starting from the end.
     * @param value the value to set at the given index.
     * @throws IndexOutOfBoundsException if the index is greater than or equal to the
     * {@linkplain Vector#length() length} of this vector.
     */
    void set(int idx, double value) throws IndexOutOfBoundsException;

    /**
     * Convert the vector into an array equivalent. This returns a new array, <em>not</em> the array backing this
     * vector, should this vector be backed by an array.
     * @return a nested array equivalent of this vector.
     */
    double[] toArray();

    /**
     * Generate a view of this vector spanning from a given index to another given index. Any changes to the returned
     * view vector will be reflected in this vector and vice versa. No guarantees are made regarding how this view is
     * backed.
     * @param startIdx the starting index of the view, zero-indexed and inclusive. A negative index is interpreted as
     *                 starting from the end.
     * @param endIdx the ending index of the view, zero-indexed and exclusive. A negative index is interpreted as
     *               starting from the end.
     * @return a view of this vector spanning from a given index to another given index.
     */
    Vector view(int startIdx, int endIdx);

    /**
     * Generate a view of this vector including the given indices. Any changes to the returned view vector will be
     * reflected in this vector and vice versa. No guarantees are made regarding how this view is backed.
     * @param indices the indices to include in the view, zero-indexed. Negative indices are interpreted as starting
     *                from the end.
     * @return a view of this vector including the given indices.
     * @throws IndexOutOfBoundsException if any of the indices are greater than or equal to the
     * {@linkplain Vector#length() length} of this vector.
     */
    Vector view(int[] indices) throws IndexOutOfBoundsException;

    /**
     * Generate a copy of this vector spanning from a given index to another given index.
     * @param startIdx the starting index of the copy, zero-indexed and inclusive. A negative index is interpreted as
     *                 starting from the end.
     * @param endIdx the ending index of the copy, zero-indexed and exclusive. A negative index is interpreted as
     *               starting from the end.
     * @return a copy of this vector spanning from a given index to another given index.
     */
    Vector copy(int startIdx, int endIdx);

    /**
     * Generate a copy of this vector including the given indices.
     * @param indices the indices to include in the copy, zero-indexed. Negative indices are interpreted as starting
     *                from the end.
     * @return a copy of this vector including the given indices.
     * @throws IndexOutOfBoundsException if any of the indices are greater than or equal to the
     * {@linkplain Vector#length() length} of this vector.
     */
    Vector copy(int[] indices) throws IndexOutOfBoundsException;

    /**
     * Copy this entire vector.
     * @return a copy of this entire vector.
     */
    default Vector copy() {
        return copy(0, length());
    }

    /**
     * For each element of the vector in the span defined by the given indices,
     * {@link VectorVisitor#visit(int, double) call the visitor}.
     * @param visitor the visitor.
     * @param startIdx the starting index, zero-indexed and inclusive. A negative index is interpreted as starting from
     *                 the end.
     * @param endIdx the ending index, zero-indexed and exclusive. A negative index is interpreted as starting from the
     *               end.
     */
    void visit(VectorVisitor visitor, int startIdx, int endIdx);

    /**
     * For each element of the vector in the span defined by the given indices,
     * {@link VectorMutator#mutate(int, double) call the mutator}.
     * @param mutator the mutator.
     * @param startIdx the starting index, zero-indexed and inclusive. A negative index is interpreted as starting from
     *                 the end.
     * @param endIdx the ending index, zero-indexed and exclusive. A negative index is interpreted as starting from the
     */
    void mutate(VectorMutator mutator, int startIdx, int endIdx);

    /**
     * For each element of the vector in the span defined by the given indices,
     * {@link VectorVisitor#visit(int, double) call the visitor}.
     * @param visitor the visitor.
     */
    default void visit(VectorVisitor visitor) {
        visit(visitor, 0, length());
    }

    /**
     * For each element of the vector in the span defined by the given indices,
     * {@link VectorMutator#mutate(int, double) call the mutator}.
     * @param mutator the mutator.
     */
    default void mutate(VectorMutator mutator) {
        mutate(mutator, 0, length());
    }

    /**
     * Create a copy of this vector as a row {@linkplain Matrix matrix} so that if {@code L} is the
     * {@linkplain Vector#length() length of this vector}, then the returned matrix will have
     * {@linkplain Matrix#nCols() width} {@code L} and {@linkplain Matrix#nRows() height} {@code 1}.
     * @return a copy of this vector as a row matrix.
     */
    Matrix asRowMatrix();

    /**
     * Create a copy of this vector as a column {@linkplain Matrix matrix} so that if {@code L} is the
     * {@linkplain Vector#length() length of this vector}, then the returned matrix will have
     * {@linkplain Matrix#nRows() height} {@code L} and {@linkplain Matrix#nCols() width} {@code 1}.
     * @return a copy of this vector as a column matrix.
     */
    Matrix asColMatrix();

    /**
     * The functional interface used for all {@code vector.visit} methods.
     */
    @FunctionalInterface
    interface VectorVisitor {
        /**
         * Called for each element iterated over for visit methods.
         * @param idx the current index.
         * @param value the value at the index.
         */
        void visit(int idx, double value);
    }

    /**
     * The functional interface used for all {@code vector.mutate} methods.
     */
    @FunctionalInterface
    interface VectorMutator {
        /**
         * Called for each element iterated over for mutate methods.
         * @param idx the current index.
         * @param value the value at the index.
         * @return the new value to assign at the index.
         */
        double mutate(int idx, double value);
    }
}
