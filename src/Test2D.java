import Extras.Benchmark;
import Extras.Mathx;
import Extras.Rand;
import Units.Time;

public class Test2D {
    public static void main (String[] args) {
        int num = 5;

        long real = Mathx.factorial(num);
        double stirling = Mathx.stirlingFactorial(Math.PI);
        double integral = Mathx.factorial(Math.PI, Mathx.standardAccuracy);

        System.out.println(real);
        System.out.println(stirling);
        System.out.println(integral);
    }
}
