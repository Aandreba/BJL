package NN.Functions.Activation;

import NN.Functions.ActivationFunction;

public class Softplus implements ActivationFunction {
    final public static Softplus softplus = new Softplus();

    @Override
    public double activate(double input) {
        return Math.log(1 + Math.exp(input));
    }

    @Override
    public double derivative(double input) {
        return 1 / (1 + Math.exp(-input));
    }
}
