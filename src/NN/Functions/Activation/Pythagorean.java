package NN.Functions.Activation;

import NN.Functions.ActivationFunction;

public class Pythagorean implements ActivationFunction {
    final public static Pythagorean pythagorean = new Pythagorean();

    @Override
    public double activate(double input) {
        return input < -1 || input > 1 ? 0 : Math.sqrt(1 - input * input);
    }

    @Override
    public double derivative(double input) {
        return input < -1 || input > 1 ? 0 : -input / Math.sqrt(1 - input * input);
    }
}
