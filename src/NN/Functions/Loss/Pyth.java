package NN.Functions.Loss;

import NN.Functions.LossFunction;

public class Pyth implements LossFunction {
    final public static Pyth pyth = new Pyth();

    @Override
    public double loss(double output, double target) {
        return Math.sqrt(1 + Math.pow(target - output, 2)) - 1;
    }

    @Override
    public double derivative(double output, double target) {
        double ot = output - target;
        return ot / Math.sqrt(ot * ot + 1);
    }
}
