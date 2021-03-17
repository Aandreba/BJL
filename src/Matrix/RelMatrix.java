package Matrix;

import Vector.RelVector;

public abstract class RelMatrix extends Matrix {
    public RelMatrix (int rows, int cols) {
        super(rows, cols);
    }

    public abstract void set (int row, int col, double value);

    @Override
    public RelVector get (int row) {
        return new RelVector (cols) {
            @Override
            public void set(int pos, double value) {
                RelMatrix.this.set(row, pos, value);
            }

            @Override
            public double get(int pos) {
                return RelMatrix.this.get(row, pos);
            }
        };
    }
}
