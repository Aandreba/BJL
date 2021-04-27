package NN;

import Matrix.Matrix;
import Matrix.StatMatrix;
import NN.Functions.ActivationFunction;
import Vector.StatVector;

import java.io.Serializable;

public class Layer implements Serializable {
    final public StatMatrix weights;
    final public StatVector biases;
    final public ActivationFunction activation;

    protected Layer (int from, int to, ActivationFunction activation) {
        this.weights = StatMatrix.random(from, to);
        this.biases = StatVector.random(to);
        this.activation = activation;
    }

    protected StatMatrix unactivatedForward (Matrix input) {
        return input.mul(weights).sum(biases).toStatic();
    }

    protected StatMatrix unactivatedForwardCUDA (Matrix input) {
        return input.mulCUDA(weights, 1, 0).sum(biases).toStatic();
    }

    public StatMatrix forward (Matrix input) {
        return activation.activate(unactivatedForward(input)).toStatic();
    }
}
