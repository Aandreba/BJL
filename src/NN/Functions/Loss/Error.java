package NN.Functions.Loss;

import NN.Functions.LossFunction;

public class Error implements LossFunction {
    final public static Error error = new Error();

    @Override
    public double loss(double output, double target) {
        return target - output;
    }

    @Override
    public double derivative(double output, double target) {
        return -1;
    }
}
