package Units;

import Extras.Mathf;

import java.text.NumberFormat;
import java.util.function.Function;

public class Temp {
    public enum Type {
        Celsius (1, 0, "ºC"),
        Fahrenheit (5/9d, -32, "ºF"),
        Kelvin (1, -273.15, "K");

        double a;
        double b;
        String s;
        private Type (double a, double b, String s) {
            this.a = a;
            this.b = b;
            this.s = s;
        }
    }

    private double value;

    public Temp (double celsius) {
        this.value = celsius;
    }

    public Temp (double v, Type type) {
        this.value = type.a * (v + type.b);
    }

    // Add
    public Temp add (double celsius) {
        return new Temp(this.value + celsius);
    }

    public Temp add (Temp Temp) {
        return new Temp(this.value + Temp.value);
    }

    // Subtr
    public Temp subtr (double celsius) {
        return new Temp(this.value - celsius);
    }

    public Temp subtr (Temp Temp) {
        return new Temp(this.value - Temp.value);
    }

    // Mul
    public Temp mul (double celsius) {
        return new Temp(this.value * celsius);
    }

    public Temp mul (Temp Temp) {
        return new Temp(this.value * Temp.value);
    }

    // Get value
    public double getValue (Type type) {
        return value / type.a - type.b;
    }

    public double getValue () {
        return this.value;
    }

    public String toString(Type type) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(1);

        return format.format(getValue(type)) + " " + type.s;
    }

    @Override
    public String toString() {
        return toString(Type.Celsius);
    }
}
