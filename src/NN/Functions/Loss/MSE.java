package NN.Functions.Loss;

import NN.Functions.LossFunction;
import Vector.Vector;

public class MSE implements LossFunction {
    final public static MSE mse = new MSE();

    @Override
    public double loss (double output, double target) {
        return 0.5 * Math.pow(target - output, 2);
    }

    @Override
    public double derivative (double output, double target) {
        return output - target;
    }
}
