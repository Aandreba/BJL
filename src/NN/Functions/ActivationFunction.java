package NN.Functions;

import Matrix.Matrix;
import Vector.Vector;

import java.io.Serializable;

public interface ActivationFunction extends Serializable {
    double activate (double input);
    double derivative (double input);

    default Matrix activate (Matrix input) {
        return new Matrix (input.rows, input.cols) {
            @Override
            public double get(int row, int col) {
                return ActivationFunction.this.activate(input.get(row, col));
            }
        };
    }

    default Matrix derivative (Matrix input) {
        return new Matrix (input.rows, input.cols) {
            @Override
            public double get(int row, int col) {
                return ActivationFunction.this.derivative(input.get(row, col));
            }
        };
    }
}
