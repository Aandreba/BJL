package Units;

import Extras.Mathf;

import java.text.NumberFormat;

public class Pressure {
    public enum Type {
        PSI(6894.757293168, "psi"),
        Atmosphere (101325, "atm"),
        Pascal (1, "Pa");

        double w;
        String s;
        private Type(double w, String s) {
            this.w = w;
            this.s = s;
        }
    }

    private double value;

    public Pressure(double pa) {
        this.value = pa;
    }

    public Pressure(double v, Type type) {
        this.value = v * type.w;
    }

    // Add
    public Pressure add (double pa) {
        return new Pressure(this.value + pa);
    }

    public Pressure add (Pressure angle) {
        return new Pressure(this.value + angle.value);
    }

    // Subtr
    public Pressure subtr (double pa) {
        return new Pressure(this.value - pa);
    }

    public Pressure subtr (Pressure angle) {
        return new Pressure(this.value - angle.value);
    }

    // Mul
    public Pressure mul (double pa) {
        return new Pressure(this.value * pa);
    }

    public Pressure mul (Pressure angle) {
        return new Pressure(this.value * angle.value);
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

        return format.format(getValue(type)) + " " + type.s;
    }

    @Override
    public String toString() {
        return toString(Type.Pascal);
    }
}
