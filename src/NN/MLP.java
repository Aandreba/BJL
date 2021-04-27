package NN;

import Extras.Tuples.Triple;
import Matrix.Matrix;
import Matrix.RelMatrix;
import Matrix.StatMatrix;
import NN.Functions.ActivationFunction;
import NN.Functions.LossFunction;
import Vector.StatVector;
import Vector.RelVector;
import Vector.Vector;

import java.io.Serializable;
import java.util.Arrays;

public class MLP implements Serializable {
    final private Layer[] layers;
    double lRate = 0.01;

    public MLP (ActivationFunction activation, int... layers) {
        this.layers = new Layer[layers.length - 1];

        for (int i=0;i<this.layers.length;i++) {
            this.layers[i] = new Layer(layers[i], layers[i+1], activation);
        }
    }

    public MLP (Triple<Integer, Integer, ActivationFunction>... layers) {
        this.layers = new Layer[layers.length];

        for (int i=0;i<layers.length;i++) {
            this.layers[i] = new Layer(layers[i].one, layers[i].two, layers[i].three);
        }
    }

    public StatMatrix forward (Matrix input) {
        StatMatrix output = input.toStatic();
        for (int i=0;i<layers.length;i++) {
            output = layers[i].forward(output);
        }

        return output;
    }

    public Vector forward (Vector input) {
        return forward(input.toMatrix(input.length)).get(0);
    }

    public Vector forward (double... input) {
        return forward(new StatVector(input));
    }

    /**
     * Backpropagate through specified inputs
     * @param loss Loss function to apply
     * @param input Input values
     * @param target Target values
     * @return Loss matrix
     */
    public Matrix backprop (LossFunction loss, Matrix input, Matrix target) {
        StatMatrix[] net = new StatMatrix[layers.length + 1];
        StatMatrix[] out = new StatMatrix[layers.length + 1];

        net[0] = input.toStatic();
        out[0] = net[0];

        for (int i=0;i<layers.length;i++) {
            net[i+1] = layers[i].unactivatedForward(out[i]);
            out[i+1] = layers[i].activation.activate(net[i+1]).toStatic();
        }

        Matrix dEdO = loss.derivative(out[layers.length], target);
        for (int i=layers.length-1;i>=0;i--) {
            RelMatrix weights = layers[i].weights;
            RelVector biases = layers[i].biases;

            Matrix dOdNET = layers[i].activation.derivative(net[i+1]);
            StatMatrix dEdNET = dEdO.scalarMul(dOdNET).toStatic();
            Matrix dEdW = out[i].transposed().mul(dEdNET);

            weights.add(dEdW.scalarMul(-lRate));
            biases.add(dEdNET.transposed().getRowMean().mul(-lRate));

            // Update error
            Matrix dEkdNET = dEdO.scalarMul(dOdNET);
            Matrix dNETdO = weights.transposed();

            dEdO = dEkdNET.mul(dNETdO);
        }

        return loss.loss(out[layers.length], target);
    }

    public Matrix backprop (LossFunction loss, Vector input, Vector target) {
        return backprop(loss, input.toMatrix(input.length), target.toMatrix(target.length));
    }

    public Matrix backprop (LossFunction loss, double[] input, double... target) {
        return backprop(loss, new StatVector(input), new StatVector(target));
    }

    public Matrix backpropCUDA (LossFunction loss, Matrix input, Matrix target) {
        StatMatrix[] net = new StatMatrix[layers.length + 1];
        StatMatrix[] out = new StatMatrix[layers.length + 1];

        net[0] = input.toStatic();
        out[0] = net[0];

        for (int i=0;i<layers.length;i++) {
            net[i+1] = layers[i].unactivatedForwardCUDA(out[i]);
            out[i+1] = layers[i].activation.activate(net[i+1]).toStatic();
        }

        Matrix dEdO = loss.derivative(out[layers.length], target);
        for (int i=layers.length-1;i>=0;i--) {
            RelMatrix weights = layers[i].weights;
            RelVector biases = layers[i].biases;

            Matrix dOdNET = layers[i].activation.derivative(net[i+1]);
            StatMatrix dEdNET = dEdO.scalarMul(dOdNET).toStatic();
            Matrix dEdW = out[i].transposed().mulCUDA(dEdNET, 1, 0);

            weights.add(dEdW.scalarMul(-lRate));
            biases.add(dEdNET.transposed().getRowMean().mul(-lRate));

            // Update error
            Matrix dEkdNET = dEdO.scalarMul(dOdNET);
            Matrix dNETdO = weights.transposed();

            dEdO = dEkdNET.mulCUDA(dNETdO, 1, 0);
        }

        return loss.loss(out[layers.length], target);
    }
}
