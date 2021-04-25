package NN;

import Matrix.Matrix;
import Matrix.RelMatrix;
import Matrix.StatMatrix;
import NN.Functions.ActivationFunction;
import Vector.RelVector;
import Vector.StatVector;

public class Layer {
    final public RelMatrix weights;
    final public RelVector biases;
    final public ActivationFunction activation;

    protected Layer (int from, int to, ActivationFunction activation) {
        this.weights = StatMatrix.random(from, to);
        this.biases = StatVector.random(to);
        this.activation = activation;
    }

    protected StatMatrix unactivatedForward (Matrix input) {
        return input.mul(weights).sum(biases).toStatic();
    }

    public StatMatrix forward (Matrix input) {
        return activation.activate(unactivatedForward(input)).toStatic();
    }
}
