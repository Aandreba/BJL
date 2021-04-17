import Extras.Image;
import Extras.Mathx;
import Extras.Rand;

import java.awt.*;
import java.io.IOException;

public class TestRand {
    public static void main (String[] args) throws IOException {
        Image noise = Mathx.plotGraph(500, 250, -20, 20, Color.WHITE, Color.RED, x -> Math.sin(x) + Math.cos(Math.sqrt(3) * x));
        noise.write("sin.png", "png");
    }
}
