import Extras.Rand;
import Extras.Tuples.Couple;
import Matrix.Matrix;
import Matrix.StatMatrix;
import NN.Functions.Activation.Sigmoid;
import NN.Functions.Loss.MSE;
import NN.MLP;
import NN.MNIST.MNIST;
import Vector.StatVector;
import Vector.Vector;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TestRand {
    public static void main (String[] args) throws IOException {
        Couple<StatVector, StatMatrix> training = MNIST.loadTrainingData();
        Couple<StatVector, StatMatrix> testing = MNIST.loadTrainingData();

        MLP mlp = new MLP(Sigmoid.sigmoid, 784, 800, 10);
        for (int i=0;i<10000;i++) {
            int[] indexes = new int[10];
            for (int j=0;j<10;j++) { indexes[j] = Rand.getInt(0, training.one.length - 1); }

            Matrix X = new Matrix (10, training.two.cols) {
                @Override
                public double get(int row, int col) {
                    return training.two.get(indexes[row], col);
                }
            };
            Matrix Y = new Matrix (10, 10) {
                @Override
                public double get(int row, int col) {
                    return training.one.get(indexes[row]) == col ? 1 : 0;
                }
            };

            mlp.backpropCUDA(MSE.mse, X, Y);
            System.out.println(i);
        }

        int[] indexes = new int[10];
        for (int j=0;j<10;j++) { indexes[j] = Rand.getInt(0, training.one.length - 1); }

        Matrix X = new Matrix (10, training.two.cols) {
            @Override
            public double get(int row, int col) {
                return training.two.get(indexes[row], col);
            }
        };
        Matrix Y = new Matrix (10, 10) {
            @Override
            public double get(int row, int col) {
                return indexes[row] == col ? 1 : 0;
            }
        };

        System.out.println(Y);
        System.out.println(mlp.forward(X));
    }
}