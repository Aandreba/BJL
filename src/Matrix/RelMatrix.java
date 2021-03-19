package Matrix;

import Vector.RelVector;
import Vector.Vector;

public abstract class RelMatrix extends Matrix {
    public RelMatrix (int rows, int cols) {
        super(rows, cols);
    }

    public abstract void set (int row, int col, double value);

    public void set (int row, Vector value) {
        for (int i=0;i<value.getLength();i++) {
            set(row, i, value.get(i));
        }
    }

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

    public void add (Matrix b) {
        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                set(i, j, get(i,j) + b.get(i,j));
            }
        }
    }

    public void add (double b) {
        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                set(i, j, get(i,j) + b);
            }
        }
    }

    public <T extends Number> void add (T b) {
        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                set(i, j, get(i,j) + b.doubleValue());
            }
        }
    }
}
