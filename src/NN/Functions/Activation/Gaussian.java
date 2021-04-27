package NN.Functions.Activation;

import NN.Functions.ActivationFunction;

public class Gaussian implements ActivationFunction {
    final public static Gaussian gaussian = new Gaussian();

    @Override
    public double activate(double input) {
        return Math.exp(input * input / -2);
    }

    @Override
    public double derivative(double input) {
        return -input / Math.sqrt(Math.exp(input * input));
    }
}
