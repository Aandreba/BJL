package NN.Functions.Activation;

import NN.Functions.ActivationFunction;

public class InvSqrt implements ActivationFunction {
    @Override
    public double activate (double input) {
        return input * Math.sqrt(input * input + 1);
    }

    @Override
    public double derivative (double input) {
        return 1 / Math.pow(input * input + 1, 1.5);
    }
}
