import Extras.ANSI;
import Extras.Benchmark;
import Extras.Mathx;
import Extras.Rand;
import Units.Time;

import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;

public class TestRand {
    public static void main (String[] args) throws IOException {
        System.out.println(Math.pow(1 + 9, 10));
        System.out.println(Mathx.stirlingBinomial(9, 10));
        System.out.println(Mathx.binomial(9, 10));
    }
}
