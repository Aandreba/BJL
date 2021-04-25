package NN;

import Extras.Tuples.Triple;
import Matrix.Matrix;
import Matrix.RelMatrix;
import Matrix.StatMatrix;
import NN.Functions.ActivationFunction;
import NN.Functions.LossFunction;
import Vector.Vector;
import Vector.RelVector;

public class MLP {
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

    public void backprop (LossFunction loss, Matrix input, Matrix target) {
        StatMatrix[] net = new StatMatrix[layers.length + 1];
        StatMatrix[] out = new StatMatrix[layers.length + 1];

        net[0] = input.toStatic();
        out[0] = net[0];

        for (int i=0;i<layers.length;i++) {
            net[i+1] = layers[i].unactivatedForward(out[i]);
            out[i+1] = layers[i].activation.activate(net[i+1]).toStatic();
        }

        StatMatrix dEdO = loss.derivative(out[layers.length], target).toStatic();
        for (int i=layers.length-1;i>=0;i--) {
            RelMatrix weights = layers[i].weights;
            RelVector biases = layers[i].biases;
            StatMatrix dOdNET = layers[i].activation.derivative(net[i+1]).toStatic();

            Matrix dEdNET = dEdO.scalarMul(dOdNET);
            Matrix dEdW = out[i].transposed().mul(dEdNET);

            weights.add(dEdW.scalarMul(-lRate));
            biases.add(dEdNET.transposed().getRowMean().mul(-lRate));
            //System.exit(1);
        }
    }
}
