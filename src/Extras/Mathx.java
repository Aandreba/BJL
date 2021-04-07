package Extras;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.function.Function;

public class Mathx {
    final public static int standardAccuracy = 1000000;

    public interface SimpleFunction {
        double apply (double value);
    }

    final public static float PI = (float) Math.PI;

    public static float sin (float x) {
        return (float) Math.sin(x);
    }

    public static float sin (double x) {
        return (float) Math.sin(x);
    }

    public static float cos (float x) {
        return (float) Math.cos(x);
    }

    public static float cos (double x) {
        return (float) Math.cos(x);
    }

    public static float tan (float x) {
        return (float) Math.tan(x);
    }

    public static float tan (double x) {
        return (float) Math.tan(x);
    }

    public static float asin (float x) {
        if (x > 0 && x < 0.95f) {
            return 1.08f * x * x * x - 0.9f * x * x + 1.2f * x; // Max error: 0.0192
        } else if (x <= 0 && x >= -0.5f) {
            x += 1;
            return 0.6f * x * x * x - 1.2f * x * x + 1.8f * x - 1.2f; // Max error: 0.0195
        } else {
            return (float) Math.asin(x);
        }
    }

    public static double log (double value, double base) {
        return Math.log10(value) / Math.log10(base);
    }

    public static double integral (double a, double b, SimpleFunction function) {
        double h = (b - a) / 8;
        return h * (function.apply(a) + 3 * function.apply((2 * a + b) / 3) + 3 * function.apply((a + 2*b) / 3) + function.apply(b));
    }

    public static double integral (double a, double b, int n, SimpleFunction function) {
        n *= 3;
        double h = (b - a) / n;

        double [] js = new double[n+1];
        for (int i=0;i<=n;i++) {
            js[i] = a + h * i;
        }

        double sum = summation(1, n / 3d, 1, k -> {
            int j = (int) k;

            double x = function.apply(js[3 * j - 3]);
            double y = 3 * function.apply(js[3 * j - 2]);
            double z = 3 * function.apply(js[3 * j - 1]);
            double w = function.apply(js[3 * j]);

            return x + y + z + w;
        });

        return sum * 3 * h / 8;
    }

    public static double summation (double from, double to, double stepSize, SimpleFunction function) {
        double v = 0;
        for (double n=from;n<=to;n+=stepSize) {
            v += function.apply(n);
        }

        return v;
    }

    public static short toUnsigned (byte value) {
        return (short) (value + 128);
    }

    public static int toUnsigned (short value) {
        return value + 32768;
    }

    public static long toUnsigned (int value) {
        return value + 2147483648L;
    }

    public static boolean getBit (int pos, byte b) {
        return ((b >> pos) & 1) == 1;
    }

    public static int getUnsignedInteger (int from, int to, ByteBuffer bb) {
        int value = 0;

        for (int i=from;i<to;i++) {
            int bytePos = i / 8;
            int bitPos = i % 8;

            byte b = bb.get(bytePos);
            if (getBit(7 - bitPos, b)) {
                value += Math.pow(2, to - i - 1);
            }
        }

        return value;
    }

    public static float getUnsignedFraction (int from, int to, ByteBuffer bb) {
        float value = 1;

        for (int i=from;i<to;i++) {
            int bytePos = i / 8;
            int bitPos = i % 8;

            byte b = bb.get(bytePos);
            if (getBit(7 - bitPos, b)) {
                value += Math.pow(2, from - i - 1);
            }
        }

        return value;
    }

    public static float[] composition (float value) {
        ByteBuffer bb = ByteBuffer.allocate(16).putFloat(value);

        boolean isNegative = getBit(0, bb.get(0));
        int exponent = getUnsignedInteger(1, 9, bb);
        float significand = getUnsignedFraction(9, 31, bb);

        return new float[] { isNegative ? 1 : 0, exponent - 127, significand };
    }

    public static double clamp (double value, double min, double max) {
        return value < min ? min : (value > max ? max : value);
    }

    public static double roundTo (double val, int decimals) {
        double pow = Math.pow(10,decimals);
        return Math.round(val * pow) / pow;
    }

    public static float roundTo (float val, int decimals) {
        float pow = (float) Math.pow(10,decimals);
        return Math.round(val * pow) / pow;
    }

    public static long factorial (int value) {
        long v = 1;
        for (int i=2;i<=value;i++) {
            v *= i;
        }

        return v;
    }

    public static double stirlingFactorial (double value) {
        if (value < 0) {
            return 0;
        }

        return Math.sqrt(2 * Math.PI * value) * Math.pow(value / Math.E, value);
    }

    public static float factorial (double value) {
        return (float) factorial(value, standardAccuracy);
    }

    public static double factorial (double value, int accuracy) {
        if (value == 0) {
            return 1;
        } else if (value > 0) {
            return integral(0, 1, accuracy, x -> x == 0 ? 0 : Math.pow(Math.log(1 / x), value));
        } else {
            // TODO Negative factorial
            return 0;
        }
    }

    public static double gamma (double value) {
        return gamma(value, standardAccuracy);
    }

    public static double gamma (double value, int accuracy) {
        return factorial(value - 1, accuracy);
    }
}
