import Matrix.StatMatrix;
import NN.Functions.Activation.Sigmoid;
import NN.Functions.Loss.Quadratic;
import NN.MLP;

import java.util.Arrays;

public class TestRand {
    public static void main (String[] args) {
        StatMatrix targetX = new StatMatrix(new double[][] { {0, 0}, {0, 1}, {1, 0}, {1, 1} });
        StatMatrix targetY = new StatMatrix(new double[][] { {0}, {1}, {1}, {0} });

        MLP mlp = new MLP(Sigmoid.sigmoid, 2, 5, 1);
        mlp.backprop(Quadratic.quadratic, targetX, targetY);
    }
}