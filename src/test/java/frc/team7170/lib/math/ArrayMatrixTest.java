package frc.team7170.lib.math;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ArrayMatrixTest {

    private static final double EPSILON = 1e-6;

    private Random random = new Random();

    private Matrix constructRandomMatrix() {
        return new ArrayMatrix(new double[][] {
                new double[] {rand(), rand(), rand()},
                new double[] {rand(), rand(), rand()},
                new double[] {rand(), rand(), rand()}
        });
    }

    private double rand() {
        return random.nextDouble() * 10.0;
    }

    private int[] getTestIndices() {
        return new int[] {
                random.nextInt(3),
                random.nextInt(3)
        };
    }

    @Test
    void nRows() {
        assertThat(constructRandomMatrix().nRows(), is(3));
    }

    @Test
    void nCols() {
        assertThat(constructRandomMatrix().nCols(), is(3));
    }

    @Test
    void add() {
        Matrix mat1 = constructRandomMatrix();
        Matrix mat2 = constructRandomMatrix();
        int[] idx = getTestIndices();
        double expected = mat1.get(idx[0], idx[1]) + mat2.get(idx[0], idx[1]);
        assertThat(mat1.add(mat2).get(idx[0], idx[1]), closeTo(expected, EPSILON));
    }

    @Test
    void add1() {
        Matrix mat1 = constructRandomMatrix();
        double val = rand();
        int[] idx = getTestIndices();
        double expected = mat1.get(idx[0], idx[1]) + val;
        assertThat(mat1.add(val).get(idx[0], idx[1]), closeTo(expected, EPSILON));
    }

    @Test
    void subtract() {
        Matrix mat1 = constructRandomMatrix();
        Matrix mat2 = constructRandomMatrix();
        int[] idx = getTestIndices();
        double expected = mat1.get(idx[0], idx[1]) - mat2.get(idx[0], idx[1]);
        assertThat(mat1.subtract(mat2).get(idx[0], idx[1]), closeTo(expected, EPSILON));
    }

    @Test
    void subtract1() {
        Matrix mat1 = constructRandomMatrix();
        double val = rand();
        int[] idx = getTestIndices();
        double expected = mat1.get(idx[0], idx[1]) - val;
        assertThat(mat1.subtract(val).get(idx[0], idx[1]), closeTo(expected, EPSILON));
    }

    @Test
    void multiply() {
        Matrix mat1 = constructRandomMatrix();
        Matrix mat2 = constructRandomMatrix();
        int[] idx = getTestIndices();
        double expected = mat1.copyRow(idx[0]).dot(mat2.copyCol(idx[1]));
        assertThat(mat1.multiply(mat2).get(idx[0], idx[1]), closeTo(expected, EPSILON));
    }

    @Test
    void multiply1() {
        Matrix mat1 = constructRandomMatrix();
        Vector vec = constructRandomMatrix().copyRow(0);
        int[] idx = getTestIndices();
        double expected = mat1.copyRow(idx[0]).dot(vec);
        assertThat(mat1.multiply(vec).get(idx[0], 0), closeTo(expected, EPSILON));
    }

    @Test
    void multiplyElementWise() {
        Matrix mat1 = constructRandomMatrix();
        Matrix mat2 = constructRandomMatrix();
        int[] idx = getTestIndices();
        double expected = mat1.get(idx[0], idx[1]) * mat2.get(idx[0], idx[1]);
        assertThat(mat1.multiplyElementWise(mat2).get(idx[0], idx[1]), closeTo(expected, EPSILON));
    }

    @Test
    void multiply2() {
        Matrix mat1 = constructRandomMatrix();
        double val = rand();
        int[] idx = getTestIndices();
        double expected = mat1.get(idx[0], idx[1]) * val;
        assertThat(mat1.multiply(val).get(idx[0], idx[1]), closeTo(expected, EPSILON));
    }

    @Test
    void power() {
        Matrix mat1 = constructRandomMatrix();
        double val = rand();
        int[] idx = getTestIndices();
        double expected = Math.pow(mat1.get(idx[0], idx[1]), val);
        assertThat(mat1.power(val).get(idx[0], idx[1]), closeTo(expected, EPSILON));
    }

    @Test
    void transpose() {
        Matrix mat1 = constructRandomMatrix();
        int[] idx = getTestIndices();
        double expected = mat1.get(idx[1], idx[0]);
        assertThat(mat1.transpose().get(idx[0], idx[1]), closeTo(expected, EPSILON));
    }

    @Test
    void set() {
        Matrix mat1 = constructRandomMatrix();
        double val = rand();
        int[] idx = getTestIndices();
        mat1.set(idx[0], idx[1], val);
        assertThat(mat1.get(idx[0], idx[1]), closeTo(val, EPSILON));
    }

    @Test
    void toArray() {
        Matrix mat1 = constructRandomMatrix();
        int[] idx = getTestIndices();
        double expected = mat1.get(idx[0], idx[1]);
        assertThat(mat1.toArray()[idx[0]][idx[1]], closeTo(expected, EPSILON));
    }

    @Test
    void setRow() {
        Matrix mat1 = constructRandomMatrix();
        Vector vec = constructRandomMatrix().copyRow(0);
        int[] idx = getTestIndices();
        double expected = vec.get(idx[1]);
        mat1.setRow(idx[0], vec);
        assertThat(mat1.get(idx[0], idx[1]), closeTo(expected, EPSILON));
    }

    @Test
    void setCol() {
        Matrix mat1 = constructRandomMatrix();
        Vector vec = constructRandomMatrix().copyRow(0);
        int[] idx = getTestIndices();
        double expected = vec.get(idx[0]);
        mat1.setCol(idx[1], vec);
        assertThat(mat1.get(idx[0], idx[1]), closeTo(expected, EPSILON));
    }

    @Test
    void view() {
        Matrix mat1 = constructRandomMatrix();
        double val = rand();
        int[] idx = getTestIndices();
        mat1.view(idx[0], idx[1], 3, 3).set(0, 0, val);
        assertThat(mat1.get(idx[0], idx[1]), closeTo(val, EPSILON));
    }

    @Test
    void view1() {
        Matrix mat1 = constructRandomMatrix();
        double val = rand();
        int[] idx = getTestIndices();
        mat1.view(new int[] {idx[0]}, new int[] {idx[1]}).set(0, 0, val);
        assertThat(mat1.get(idx[0], idx[1]), closeTo(val, EPSILON));
    }

    @Test
    void viewRow() {
        Matrix mat1 = constructRandomMatrix();
        double val = rand();
        int[] idx = getTestIndices();
        mat1.viewRow(idx[0]).set(idx[1], val);
        assertThat(mat1.get(idx[0], idx[1]), closeTo(val, EPSILON));
    }

    @Test
    void viewCol() {
        Matrix mat1 = constructRandomMatrix();
        double val = rand();
        int[] idx = getTestIndices();
        mat1.viewCol(idx[1]).set(idx[0], val);
        assertThat(mat1.get(idx[0], idx[1]), closeTo(val, EPSILON));
    }

    @Test
    void copy() {
        Matrix mat1 = constructRandomMatrix();
        int[] idx = getTestIndices();
        double expected = mat1.get(idx[0], idx[1]);
        assertThat(mat1.copy(idx[0], idx[1], 3, 3).get(0, 0), closeTo(expected, EPSILON));
    }

    @Test
    void copy1() {
        Matrix mat1 = constructRandomMatrix();
        int[] idx = getTestIndices();
        double expected = mat1.get(idx[0], idx[1]);
        assertThat(mat1.copy(new int[] {idx[0]}, new int[] {idx[1]}).get(0, 0), closeTo(expected, EPSILON));
    }

    @Test
    void copyRow() {
        Matrix mat1 = constructRandomMatrix();
        int[] idx = getTestIndices();
        double expected = mat1.get(idx[0], idx[1]);
        assertThat(mat1.copyRow(idx[0]).get(idx[1]), closeTo(expected, EPSILON));
    }

    @Test
    void copyCol() {
        Matrix mat1 = constructRandomMatrix();
        int[] idx = getTestIndices();
        double expected = mat1.get(idx[0], idx[1]);
        assertThat(mat1.copyCol(idx[1]).get(idx[0]), closeTo(expected, EPSILON));
    }

    @Test
    void visitRowWise() {
    }

    @Test
    void visitColWise() {
    }

    @Test
    void visitRows() {
    }

    @Test
    void visitCols() {
    }

    @Test
    void mutateRowWise() {
    }

    @Test
    void mutateColWise() {
    }

    @Test
    void mutateRows() {
    }

    @Test
    void mutateCols() {
    }
}