package Units;

import Extras.Mathx;
import OpenGL.Extras.Vector.StatVector2;
import OpenGL.Extras.Vector.Vector2;

import java.io.Serializable;
import java.text.NumberFormat;

public class Angle implements Serializable {
    public enum Type {
        Tau(2 * Math.PI, "τ"),
        Radians(1,"rad"),
        Degrees(Math.PI / 180, "deg");

        double w;
        String s;
        private Type(double w, String s) {
            this.w = w;
            this.s = s;
        }
    }

    private double value;

    public Angle (double rad) {
        this.value = rad;
    }

    public Angle (double v, Type type) {
        this.value = v * type.w;
    }

    // Add
    public Angle add (double rad) {
        return new Angle(this.value + rad);
    }

    public Angle add (Angle angle) {
        return new Angle(this.value + angle.value);
    }

    // Subtr
    public Angle subtr (double rad) {
        return new Angle(this.value - rad);
    }

    public Angle subtr (Angle angle) {
        return new Angle(this.value - angle.value);
    }

    // Mul
    public Angle mul (double rad) {
        return new Angle(this.value * rad);
    }

    public Angle mul (Angle angle) {
        return new Angle(this.value * angle.value);
    }

    // Sin
    public double sin () {
        return Math.sin(value);
    }

    public float sinf () {
        return Mathx.sin(value);
    }

    public double cos () {
        return Math.cos(value);
    }

    public float cosf () {
        return Mathx.cos(value);
    }

    public double tan () {
        return Math.tan(value);
    }

    public float tanf () {
        return Mathx.tan(value);
    }

    public Vector2 normal () {
        return new Vector2() {
            @Override
            public double get (int pos) {
                if (pos == 0) {
                    return Angle.this.cos();
                }

                return Angle.this.sin();
            }
        };
    }

    // Get value
    public double getValue (Type type) {
        return this.value / type.w;
    }

    public double getValue () {
        return this.value;
    }

    public String toString(Type type) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(1);

        double value = this.value % (2 * Math.PI);
        if (value < 0) {
            value = (2 * Math.PI) + value;
        }

        return format.format(value / type.w) + " " + type.s;
    }

    @Override
    public String toString() {
        return toString(Type.Degrees);
    }
}
