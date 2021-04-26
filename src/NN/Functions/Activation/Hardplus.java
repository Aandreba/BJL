package NN.Functions.Activation;

import NN.Functions.ActivationFunction;

public class Hardplus implements ActivationFunction {
    final public static Hardplus hardplus = new Hardplus();

    @Override
    public double activate (double input) {
        return Math.sqrt(1 + Math.exp(input));
    }

    @Override
    public double derivative (double input) {
        double x = activate(input);
        return 2 * (x - Math.pow(Math.tanh(x), -1));
    }
}
