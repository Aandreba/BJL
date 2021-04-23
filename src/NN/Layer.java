package NN;

import Matrix.Matrix;
import Matrix.RelMatrix;
import Matrix.StatMatrix;
import NN.Functions.ActivationFunction;
import Vector.RelVector;
import Vector.StatVector;

public class Layer {
    final public int inputs, outputs;
    final public RelMatrix weights;
    final public RelVector biases;

    public Layer (int inputs, int outputs) {
        this.inputs = inputs;
        this.outputs = outputs;

        this.weights = StatMatrix.random(inputs, outputs, -1, 1);
        this.biases = StatVector.random(outputs, -1, 1);
    }

    public Layer (RelMatrix weights, RelVector biases) {
        this.weights = weights;
        this.biases = biases;

        this.inputs = weights.cols;
        this.outputs = weights.rows;
    }

    public Matrix forward (Matrix input) {
        return input.mul(weights).sum(biases);
    }

    public Matrix activate (Matrix input, ActivationFunction activation) {
        return activation.activate(forward(input));
    }
}
