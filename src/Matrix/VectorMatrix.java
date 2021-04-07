package Matrix;

import Vector.Vector;
import Vector.RelVector;

public class VectorMatrix extends Matrix {
    Vector[] vectors;

    public VectorMatrix (int cols, Vector... vectors) {
        super(vectors.length, cols);
        this.vectors = vectors;
    }

    @Override
    public double get(int row, int col) {
        return vectors[row].get(col);
    }

    public Vector get(int row) {
        return vectors[row];
    }
}
