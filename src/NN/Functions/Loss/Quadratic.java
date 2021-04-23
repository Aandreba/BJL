package NN.Functions.Loss;

import NN.Functions.LossFunction;

public class Quadratic implements LossFunction {
    final public static Quadratic quadratic = new Quadratic();

    @Override
    public double loss (double output, double target) {
        return Math.pow(output - target, 2);
    }

    @Override
    public double derivative (double output, double target) {
        return 2 * (output - target);
    }
}
