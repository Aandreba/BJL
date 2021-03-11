package Matrix;

public abstract class RelMatrix extends Matrix {
    public RelMatrix (int rows, int cols) {
        super(rows, cols);
    }

    public abstract void set (int row, int col, double value);
}
