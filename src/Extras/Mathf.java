package Extras;

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
}
