package NN.Functions.Activation;

import NN.Functions.ActivationFunction;

public class Tanh implements ActivationFunction {
    final public static Tanh tanh = new Tanh();

    @Override
    public double activate(double input) {
        return Math.tanh(input);
    }

    @Override
    public double derivative(double input) {
        return 1 / Math.pow(Math.cosh(input), 2);
    }
}
