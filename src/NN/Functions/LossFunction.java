package NN.Functions;

import Matrix.Matrix;
import Vector.Vector;

public interface LossFunction {
    double loss (double output, double target);
    double derivative (double output, double target);

    default Vector loss (Matrix output, Matrix target) {
        return new Matrix (output.rows, output.cols) {
            @Override
            public double get(int row, int col) {
                return LossFunction.this.loss(output.get(row, col), target.get(row, col));
            }
        }.transposed().getRowMean();
    }

    default Matrix derivative (Matrix output, Matrix target) {
        return new Matrix (output.rows, output.cols) {
            @Override
            public double get(int row, int col) {
                return LossFunction.this.derivative(output.get(row, col), target.get(row, col));
            }
        };
    }
}
