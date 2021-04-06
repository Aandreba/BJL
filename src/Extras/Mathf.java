package Extras;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

public class Mathf {
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

    public static float[] compossition (float value) {
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
        float pow = (float)Math.pow(10,decimals);
        return Math.round(val * pow) / pow;
    }

    public static long factorial (long value) {
        long v = 1;
        for (long i=2;i<=value;i++) {
            v *= i;
        }

        return v;
    }

    public static int factorial (int value) {
        int v = 1;
        for (int i=2;i<=value;i++) {
            v *= i;
        }

        return v;
    }

    public static float factorial (float value, int sums) {
        float[] parts = compossition(value);
        float sum = 0;

        for (int k=0;k<sums;i++) {
            float v = 0;
        }

        return 0;
    }

    public static float factorial (int dividend, int divisor) {

    }
}
