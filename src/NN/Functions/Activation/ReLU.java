package NN.Functions.Activation;

import NN.Functions.ActivationFunction;

public class ReLU implements ActivationFunction {
    final public static ReLU relu = new ReLU();

    @Override
    public double activate(double input) {
        return Math.max(0, input);
    }

    @Override
    public double derivative(double input) {
        return input < 0 ? 0 : 1;
    }
}
