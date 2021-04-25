import Matrix.StatMatrix;
import NN.Functions.Activation.Sigmoid;
import NN.Functions.Activation.Tanh;
import NN.Functions.Loss.MSE;
import NN.MLP;

public class TestRand {
    public static void main (String[] args) {
        StatMatrix targetX = new StatMatrix(new double[][] { {0, 0}, {0, 1}, {1, 0}, {1, 1} });
        StatMatrix targetY = new StatMatrix(new double[][] { {0}, {1}, {1}, {0} });

        MLP mlp = new MLP(Tanh.tanh, 2, 50, 50, 1);

        for (int i=0;i<100000;i++) {
            mlp.backprop(MSE.mse, targetX, targetY);
        }

        System.out.println(mlp.forward(targetX));
    }
}