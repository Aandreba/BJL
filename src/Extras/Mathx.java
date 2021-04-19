package Extras;

import Extras.Tuples.Couple;
import Units.Angle;
import Units.ByteSize;
import Vector.Vector;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public class Mathx {
    final public static int standardAccuracy = 1000000;
    final public static float PI = (float) Math.PI;
    final public static float E = (float) Math.E;

    final public static double sqrt5 = Math.sqrt(5);
    final public static double GR = (1 + sqrt5) / 2;
    final public static float GRf = (float) GR;
    final public static double GRlog = Math.log(GR);

    public interface SimpleFunction {
        double apply (double value);
    }

    public interface SummationFunction {
        double apply (int position);
    }

    public static float sqrt (float x) { return (float) Math.sqrt(x); }

    public static float pow (float x, float n) { return (float) Math.pow(x, n); }

    public static double root (double x, double n) { return Math.pow(x, 1/n); }

    public static float root (float x, float n) { return pow(x, 1/n); }

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

    public static Angle asin (double x) { return new Angle(Math.asin(x)); }

    public static Angle acos (double x) { return new Angle(Math.acos(x)); }

    public static Angle atan (double x) { return new Angle(Math.atan(x)); }

    public static double log (double value, double base) {
        return Math.log10(value) / Math.log10(base);
    }

    public static int roundToInt (double val) {
        return (int) Math.round(val);
    }

    public static int ceil (float value) {
        return (int) Math.ceil(value);
    }

    public static long ceil (double value) {
        return (long) Math.ceil(value);
    }

    public static int floor (float value) {
        return (int) Math.floor(value);
    }

    public static long floor (double value) {
        return (long) Math.floor(value);
    }

    public static int getExponent (float value) {
        final ByteSize size = new ByteSize(4);
        final BitBuffer buffer = new BitBuffer(size).set(0, value).flipped();
        byte exp = buffer.getByte(23);

        return Byte.toUnsignedInt(exp) - 127;
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

        double sum = summation(1, n / 3, j -> {
            double x = function.apply(js[3 * j - 3]);
            double y = 3 * function.apply(js[3 * j - 2]);
            double z = 3 * function.apply(js[3 * j - 1]);
            double w = function.apply(js[3 * j]);

            return x + y + z + w;
        });

        return sum * 3 * h / 8;
    }

    public static double summation (int from, int to, SummationFunction function) {
        double v = 0;
        for (int n=from;n<=to;n++) {
            v += function.apply(n);
        }

        return v;
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

    public static double fibonacci (int index) {
        if (index >= 0) {
            double res = Math.pow(GR, index) / sqrt5;
            return index < 91 ? Math.round(res) : (index <= 1476 ? res : Double.NaN);
        }

        return Math.round((Math.pow(GR, index) - Math.pow(1 - GR, index)) / sqrt5);
    }

    public static double alexFibonacci (int index) {
        double res = Math.pow(GR, index - GR);
        return index < 91 ? Math.round(res) : (index <= 1476 ? res : Double.NaN);
    }

    public static double pascalTriangle (int row, int col) {
        return factorial(row) * 1d / (factorial(col) * factorial(row - col));
    }

    public static double stirlingPascalTriangle (int row, int col) {
        return stirlingFactorial(row) / (stirlingFactorial(col) * stirlingFactorial(row - col));
    }

    public static double binomial (double x, double y, int n) {
        double nFactorial = factorial(n);

        return summation(0, n, k -> {
            double kFactorial = factorial(k);
            double pascal = nFactorial / (kFactorial * factorial(n - k));

            return pascal * Math.pow(x, n-k) * Math.pow(y,k);
        });
    }

    public static double stirlingBinomial (double x, double y, int n) {
        double nFactorial = stirlingFactorial(n);

        return summation(0, n, k -> {
            double kFactorial = stirlingFactorial(k);
            double pascal = nFactorial / (kFactorial * stirlingFactorial(n - k));

            return pascal * Math.pow(x, n-k) * Math.pow(y,k);
        });
    }

    public static double binomial (double x, int n) {
        double nFactorial = factorial(n);

        return summation(0, n, k -> {
            double kFactorial = factorial(k);
            double pascal = nFactorial / (kFactorial * factorial(n - k));

            return pascal * Math.pow(x, k);
        });
    }

    public static double stirlingBinomial (double x, int n) {
        double nFactorial = stirlingFactorial(n);

        return summation(0, n, k -> {
            double kFactorial = stirlingFactorial(k);
            double pascal = nFactorial / (kFactorial * stirlingFactorial(n - k));

            return pascal * Math.pow(x, k);
        });
    }

    private static void writeGraphLimits (Image image, double minX, double maxX, double minY, double maxY) {
        int width = image.getWidth();
        int height = image.getHeight();

        int fontSize = roundToInt(root(pow(width,2) + pow(height,2), 4)) / 2;

        String minYString = toString(minY);
        String maxYString = toString(maxY);
        int xHeight = (height + fontSize) / 2;
        String maxXString = toString(maxX);

        image.addText(width / 2 - (fontSize * maxYString.length()) / 4, fontSize, maxYString, Image.defaultFont, Color.BLACK, fontSize);
        image.addText(width / 2 - (fontSize * minYString.length()) / 4, height - fontSize, minYString, Image.defaultFont, Color.BLACK, fontSize);
        image.addText(fontSize / 2, xHeight, toString(minX), Image.defaultFont, Color.BLACK, fontSize);
        image.addText(width - (maxXString.length() + 1) * fontSize / 2, xHeight, maxXString, Image.defaultFont, Color.BLACK, fontSize);
    }

    public static Image plotGraph (int width, int height, Color bckg, Color color, int spacing, Couple<? extends Number, ? extends Number>... values) {
        Image image = new Image(width, height);

        // Extract values into vector
        Vector x = Vector.forEachIndex(values.length, i -> values[i].one.doubleValue());
        Vector y = Vector.forEachIndex(values.length, i -> values[i].two.doubleValue());

        double minX = x.min();
        double maxX = x.max();
        double minY = y.min();
        double maxY = y.max();

        // Write limits
        writeGraphLimits(image, minX, maxX, minY, maxY);

        // Plot values
        double minmaxX = minX - maxX;
        double maxminY = maxY - minY;
        ArrayList<Couple<Integer,Integer>> points = new ArrayList<>();

        for (int i=0;i<values.length;i++) {
            double X = x.get(i);
            double Y = y.get(i);

            int w = Math.min(width - 1, roundToInt(width * (minX - X) / (minmaxX)));
            int h = Math.min(height - 1, roundToInt(height * (maxY - Y) / (maxminY)));

            for (int k=-spacing;k<=spacing;k++) {
                points.add(new Couple<>(w, h+k));
            }
        }

        for (int i=0;i<width;i++) {
            for (int j=0;j<height;j++) {
                Couple<Integer, Integer> point = new Couple<>(i, j);
                image.setPixel(i, j, points.contains(point) ? color : bckg);
            }
        }

        return image;
    }

    public static Image plotGraph (int width, int height, double minX, double maxX, double minY, double maxY, Color bckg, Color color, int spacing, SimpleFunction formula) {
        Image image = new Image(width, height);

        // Write limits
        writeGraphLimits(image, minX, maxX, minY, maxY);

        // Plot values
        double minmaxX = minX - maxX;
        double maxminY = maxY - minY;

        for (int i=0;i<width;i++) {
            double x = (minmaxX * -i) / width + minX;
            double y = formula.apply(x);
            int h = Math.min(height - 1, roundToInt(height * (maxY - y) / (maxminY)));

            for (int j=0;j<height;j++) {
                image.setPixel(i, j, Math.abs(j - h) <= spacing ? color : bckg);
            }
        }

        return image;
    }

    public static Image plotGraph (int width, int height, double from, double to, double step, Color bckg, Color color, int spacing, SimpleFunction formula) {
        ArrayList<Couple<Double,Double>> values = new ArrayList<>();
        double x = from;

        while (x <= to) {
            double y = formula.apply(x);
            if (Double.isNaN(y) || Double.isInfinite(y)) {
                x += step;
                continue;
            }

            values.add(new Couple<>(x,y));
            x += step;
        }

        return plotGraph(width, height, bckg, color, spacing, values.toArray(Couple[]::new));
    }

    public static Image plotGraph (int width, int height, double from, double to, Color bckg, Color color, int spacing, SimpleFunction formula) {
        return plotGraph(width, height, from, to, (to - from) / width, bckg, color, spacing, formula);
    }

    public static String toString (double x) {
        double abs = Math.abs(x);
        boolean sign = x < 0;

        if (abs == Math.PI || abs == Mathx.PI) {
            return sign ? "-π" : "π";
        } else if (abs == Math.E || abs == Mathx.E) {
            return sign ? "-e" : "e";
        } else if (abs == GR || abs == GRf) {
            return sign ? "-φ" : "φ";
        }

        final NumberFormat format = NumberFormat.getNumberInstance();
        String result = format.format(x);

        return result.equals("-0") ? "0" : result;
    }
}
