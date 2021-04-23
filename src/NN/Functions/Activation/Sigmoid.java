package NN.Functions.Activation;

import NN.Functions.ActivationFunction;

public class Sigmoid implements ActivationFunction {
    final public static Sigmoid sigmoid = new Sigmoid();

    @Override
    public double activate(double input) {
        return 1 / (1 + Math.exp(-input));
    }

    @Override
    public double derivative(double input) {
        double sig = activate(input);
        return sig * (1 - sig);
    }
}
