package Matrix;

import Vector.StatVector;
import Vector.Vector;
import java.nio.ByteBuffer;

public class StatMatrix extends RelMatrix {
    protected double[][] values;

    public StatMatrix (int rows, int cols) {
        super(rows, cols);
        this.values = new double[rows][cols];
    }

    public StatMatrix (double[]... values) {
        super(values.length, values[0].length);
        this.values = values;
    }

    public static StatMatrix fromByteBuffer (ByteBuffer bb) {
        int cols = bb.getInt(0);
        int rows = (bb.capacity() - 4) / (8 * cols);

        StatVector ret = new StatVector(rows * cols);

        for (int i=0;i<ret.getLength();i++) {
            ret.set(i, bb.getDouble(4 + 8*i));
        }

        return ret.toMatrix(cols).toStatic();
    }

    public static StatMatrix fromBytes (byte[] bytes) {
        return fromByteBuffer(ByteBuffer.wrap(bytes));
    }

    public static StatMatrix random (int rows, int cols, double from, double to) {
        return Matrix.random(rows, cols, from, to).toStatic();
    }

    public static StatMatrix random (int rows, int cols) {
        return Matrix.random(rows, cols).toStatic();
    }

    public static StatMatrix randomCUDA (int rows, int cols, boolean isDouble) {
        return StatVector.randomCUDA(rows * cols, isDouble).toMatrix(cols).toStatic();
    }

    public static StatMatrix randomCUDA (int rows, int cols) {
        return StatVector.randomCUDA(rows * cols).toMatrix(cols).toStatic();
    }

    public static StatMatrix randomCUDA (int rows, int cols, double from, double to) {
        return StatVector.randomCUDA(rows * cols, from, to).toMatrix(cols).toStatic();
    }

    public static StatMatrix randomCUDA (int rows, int cols, float from, float to) {
        return StatVector.randomCUDA(rows * cols, from, to).toMatrix(cols).toStatic();
    }

    public static StatMatrix randomIntCUDA (int rows, int cols, boolean isLong) {
        return StatVector.randomIntCUDA(rows * cols, isLong).toMatrix(cols).toStatic();
    }

    public static StatMatrix randomIntCUDA (int rows, int cols) {
        return StatVector.randomIntCUDA(rows * cols, false).toMatrix(cols).toStatic();
    }

    public static StatMatrix randomIntCUDA (int rows, int cols, long from, long to) {
        return StatVector.randomIntCUDA(rows * cols, from, to).toMatrix(cols).toStatic();
    }

    public static StatMatrix randomIntCUDA (int rows, int cols, int from, int to) {
        return StatVector.randomIntCUDA(rows * cols, from, to).toMatrix(cols).toStatic();
    }

    @Override
    public double get(int row, int col) {
        return this.values[row][col];
    }

    @Override
    public void set (int row, int col, double value) {
        this.values[row][col] = value;
    }

    public void set (int row, Vector value) {
        this.values[row] = value.toArray();
    }

    public void set (int row, double... values) {
        this.values[row] = values;
    }

    public <T extends Number> void set (int row, int col, T value) {
        this.values[row][col] = value.doubleValue();
    }

    @Override
    public double[][] toArray() {
        return values;
    }

    public StatMatrix clone () {
        StatMatrix ret = new StatMatrix(rows, cols);

        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                ret.set(i,j, get(i,j));
            }
        }

        return ret;
    }
}
