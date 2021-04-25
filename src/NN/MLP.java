package NN;

import Extras.Tuples.Couple;
import Matrix.Matrix;
import Matrix.RelMatrix;
import Matrix.StatMatrix;
import NN.Functions.ActivationFunction;
import NN.Functions.LossFunction;
import Vector.RelVector;
import Vector.Vector;
import Vector.StatVector;

public class MLP {
    final public int inputs, outputs;
    final public ActivationFunction activation;
    final private Layer[] layers;

    final private RelMatrix[] weights;
    final private RelVector[] biases;

    public double lRate = 0.1;

    public MLP (ActivationFunction activation, Layer... layers) {
        this.layers = layers;
        this.inputs = layers[0].weights.rows;
        this.outputs = layers[layers.length - 1].outputs;
        this.activation = activation;

        Couple<RelMatrix[], RelVector[]> arrays = createArrays();
        this.weights = arrays.one;
        this.biases = arrays.two;
    }

    public MLP (ActivationFunction activation, int... layers) {
        this.inputs = layers[0];
        this.outputs = layers[layers.length - 1];
        this.layers = new Layer[layers.length - 1];
        this.activation = activation;

        for (int i=1;i<layers.length;i++) {
            this.layers[i-1] = new Layer(layers[i-1], layers[i]);
        }

        Couple<RelMatrix[], RelVector[]> arrays = createArrays();
        this.weights = arrays.one;
        this.biases = arrays.two;
    }

    public StatMatrix forward (Matrix input) {
        StatMatrix output = input.toStatic();
        for (int i=0;i<layers.length;i++) {
            output = layers[i].activate(output, activation).toStatic();
        }

        return output;
    }

    public StatVector forward (Vector input) {
        return forward(input.toMatrix(input.length)).get(0).toStatic();
    }

    public StatVector forward (double... input) {
        return forward(new StatVector(input));
    }

    public void backprop (LossFunction loss, Matrix input, Matrix target) {
        StatMatrix[] z = new StatMatrix[layers.length + 1]; // Unactivated layers
        StatMatrix[] a = new StatMatrix[layers.length + 1]; // Activated layers

        z[0] = input.toStatic(); // First layer is input
        a[0] = z[0];

        for (int i=0;i<layers.length;i++) { // Calculate rest of layers
            z[i+1] = this.layers[i].forward(z[i]).toStatic();
            a[i+1] = activation.activate(z[i+1]).toStatic();
        }

        // Backpropagation
        Matrix dEdA = loss.derivative(a[layers.length], target);

        for (int i=layers.length;i>0;i--) {
            Matrix dAdZ = activation.derivative(z[i]);
            StatMatrix biasGradient = dEdA.scalarMul(dAdZ).toStatic();
            StatMatrix weightGradient = a[i-1].transposed().mul(biasGradient).toStatic();

            Matrix dEdZ = dEdA.scalarMul(dAdZ);
            dEdA = dEdZ.mul(weights[i-1].transposed()).toStatic();
            /*System.out.println(dEdZ);
            System.out.println(weights[i-1]);
            System.out.println(dEdZ.mul(weights[i-1].transposed()));
            System.exit(1);*/

            weights[i-1].add(weightGradient.scalarMul(-lRate));
            biases[i-1].add(biasGradient.transposed().getRowMean().mul(-lRate));
        }
    }

    public Layer getLayer (int index) {
        return layers[index];
    }

    public RelMatrix[] getWeights () {
        return weights.clone();
    }

    public RelVector[] getBiases () {
        return biases.clone();
    }

    private Couple<RelMatrix[], RelVector[]> createArrays() {
        Couple<RelMatrix[], RelVector[]> output = new Couple<>();
        output.one = new RelMatrix[layers.length];
        output.two = new RelVector[layers.length];

        for (int i=0;i<layers.length;i++) {
            output.one[i] = layers[i].weights;
            output.two[i] = layers[i].biases;
        }

        return output;
    }
}
