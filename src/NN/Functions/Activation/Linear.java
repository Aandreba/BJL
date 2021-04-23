package NN.Functions.Activation;

import NN.Functions.ActivationFunction;

public class Linear implements ActivationFunction {
    final public static Linear linear = new Linear();

    @Override
    public double activate (double input) {
        return input;
    }

    @Override
    public double derivative (double input) {
        return 1;
    }
}
