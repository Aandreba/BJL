package Extras;

import Matrix.Matrix;
import Matrix.StatMatrix;
import OpenGL.Extras.Vector.StatVector2;
import OpenGL.Extras.Vector.Vector2;
import Units.Angle;
import Vector.StatVector;
import Vector.Vector;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Rand {
    final private static Random random = new Random();
    final private static FastNoiseLite noise = new FastNoiseLite();

    // Double
    public static double getDouble () {
        return random.nextDouble();
    }

    public static double getDouble (double from, double to) {
        return (to - from) * random.nextDouble() + from;
    }

    public static double getDoubleFull () {
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);

        return ByteBuffer.wrap(bytes).getDouble();
    }

    // Float
    public static float getFloat () {
        return random.nextFloat();
    }

    public static float getFloat (float from, float to) {
        return (to - from) * random.nextFloat() + from;
    }

    public static float getFloatFull () {
        byte[] bytes = new byte[4];
        random.nextBytes(bytes);

        return ByteBuffer.wrap(bytes).getFloat();
    }

    // Long
    public static long getLong () {
        return random.nextLong();
    }

    public static long getULong () {
        long ulong;
        do {
            ulong = random.nextLong();
        } while (ulong < 0);

        return ulong;
    }

    public static long getLong (long from, long to) {
        return Math.round(getDouble(from, to));
    }

    // Int
    public static int getInt () {
        return random.nextInt();
    }

    public static int getUInt () {
        int uint;
        do {
            uint = random.nextInt();
        } while (uint < 0);

        return random.nextInt();
    }

    public static int getInt (int from, int to) {
        return Math.round(getFloat(from, to));
    }

    // Short
    public static short getShort () {
        byte[] bytes = new byte[2];
        random.nextBytes(bytes);

        return ByteBuffer.wrap(bytes).getShort();
    }

    public static short getShort (short from, short to) {
        return (short) getInt(from, to);
    }

    // Bool
    public static boolean getBool() {
        return random.nextBoolean();
    }

    public static boolean getBool (double chance) {
        return chance < random.nextDouble();
    }

    // Color
    public static Color getColorRGB () {
        return new Color(getInt(0, 255), getInt(0, 255), getInt(0, 255));
    }

    public static Color getColorRGBA () {
        return new Color(getInt(0, 255), getInt(0, 255), getInt(0, 255), getInt(0, 255));
    }

    // Choice
    public static int choice (double... weights) throws Exception {
        for (int i=1;i<weights.length;i++){
            weights[i] += weights[i-1];
        }

        double v = getDouble(0, weights[weights.length - 1]);
        for (int i=0;i<weights.length;i++) {
            if (v < weights[i]) {
                return i;
            }
        }

        System.out.println(Arrays.toString(weights));
        throw new Exception("No choice could be selected");
    }

    public static <T extends Number> int choice (List<T> w) throws Exception {
        double[] weights = new double[w.size()];

        for (int i=0;i<w.size();i++){
            weights[i] = w.get(i).doubleValue();
            if (i > 0) {
                weights[i] += weights[i - 1];
            }
        }

        double v = getDouble(0, weights[weights.length - 1]);
        for (int i=0;i<weights.length;i++) {
            if (v < weights[i]) {
                return i;
            }
        }

        System.out.println(Arrays.toString(weights));
        throw new Exception("No choice could be selected");
    }

    public static int choice (Vector weights) throws Exception {
        StatVector w = weights.toStatic();

        for (int i=1;i<weights.getLength();i++){
            w.set(i, w.get(i) + w.get(i-1));
        }

        double v = getDouble(0, w.getLast());

        for (int i=0;i<w.getLength();i++) {
            if (v < w.get(i)) {
                return i;
            }
        }

        throw new Exception("No choice could be selected");
    }

    public static <T> T choice (T... vals) {
        return vals[Rand.getInt(0, vals.length - 1)];
    }

    public static float noise2D (float x, float y, int seed, FastNoiseLite.NoiseType type, float frequency) {
        noise.SetSeed(seed);
        noise.SetNoiseType(type);
        noise.SetFrequency(frequency);

        return noise.GetNoise(x, y);
    }

    public static float noise2D (float x, float y) {
        return noise2D(x, y, 1337, FastNoiseLite.NoiseType.OpenSimplex2, 0.01f);
    }

    public static double pseudoFourier (Random random, double x, int size) {
        StatVector values = new StatVector(size);
        double n2 = size * size;

        for (int i=0;i<size;i++) {
            double amplitude = 2 * random.nextDouble() - 1;
            double frequency = size * random.nextDouble();

            values.set(i, amplitude * Math.cos(Mathx.pi2 * frequency * x));
        }

        double sum = values.getSum() + Math.E;
        double sumFactorial = Mathx.stirlingFactorial(sum) / size;
        double factorialSum = values.stirlingFactorial().getSum() / n2;

        return sum * sumFactorial * factorialSum / n2;
    }

    public static double pseudoFourier (long seed, double x, int size) {
        return pseudoFourier(new Random(seed), x, size);
    }

    public static double pseudoFourier (double x, int size) {
        return pseudoFourier(random, x, size);
    }

    public static double pseudoFourier2D (long seed, double x, double y, int size) {
        return pseudoFourier(seed, (x * Math.sin(y)) / Mathx.pi2, size) + pseudoFourier(seed, (y * Math.cos(x)) / Mathx.pi2, size);
    }
}
