package Units;

import java.io.Serializable;
import java.text.NumberFormat;

public class Mass implements Comparable<Mass>, Serializable {
    public interface Type {
        double getWeight();
        String getSymbol();
    };

    public enum SI implements Type {
        Tonnes(1000,"t"),
        Kilograms(1,"kg"),
        Grams(1e-3,"g"),
        Milligrams(1e-6,"mg");

        double w;
        String s;
        private SI (double w, String s) {
            this.w = w;
            this.s = s;
        }

        @Override
        public double getWeight() {
            return this.w;
        }

        @Override
        public String getSymbol() {
            return this.s;
        }
    }

    public enum Imperial implements Type {
        Pounds(0.45359237,"lb"),
        Ounces(0.028349523125,"oz"),
        Grains(0.00006479891,"gr");

        double w;
        String s;
        private Imperial (double w, String s) {
            this.w = w;
            this.s = s;
        }

        @Override
        public double getWeight() {
            return this.w;
        }

        @Override
        public String getSymbol() {
            return this.s;
        }
    }

    public enum Astronomical implements Type {
        SolarMasses(1.988e30,"M☉"),
        JupiterMasses(1.898e27,"Mj"),
        EarthMasses(5.9742e24,"M⊕");

        double w;
        String s;
        private Astronomical (double w, String s) {
            this.w = w;
            this.s = s;
        }

        @Override
        public double getWeight() {
            return this.w;
        }

        @Override
        public String getSymbol() {
            return this.s;
        }
    }

    public enum Others implements Type {
        Electronvolts(1.782e-36,"eV");

        double w;
        String s;
        private Others (double w, String s) {
            this.w = w;
            this.s = s;
        }

        @Override
        public double getWeight() {
            return this.w;
        }

        @Override
        public String getSymbol() {
            return this.s;
        }
    }

    final private double value;

    public Mass (double kg) {
        this.value = kg;
    }

    public Mass (double v, Type type) {
        this.value = v * type.getWeight();
    }

    // Add
    public Mass add (double kg) {
        return new Mass(this.value + kg);
    }

    public Mass add (Mass mass) {
        return new Mass(this.value + mass.value);
    }

    // Subtr
    public Mass subtr (double kg) {
        return new Mass(this.value - kg);
    }

    public Mass subtr (Mass mass) {
        return new Mass(this.value - mass.value);
    }

    // Mul
    public Mass mul (double kg) {
        return new Mass(this.value * kg);
    }

    public Mass mul (Mass mass) {
        return new Mass(this.value * mass.value);
    }

    // Div
    public Mass div (double kg) {
        return new Mass(this.value / kg);
    }

    public Mass div (Mass mass) {
        return new Mass(this.value / mass.value);
    }

    // Get value
    public double getValue (Type type) {
        return this.value / type.getWeight();
    }

    public double getValue () {
        return this.value;
    }

    public String toString (Class<? extends Type> units) {
        Type[] values = null;

        if (units.equals(SI.class)) {
            values = SI.values();
        } else if (units.equals(Imperial.class)) {
            values = Imperial.values();
        } else if (units.equals(Astronomical.class)) {
            values = Astronomical.values();
        } else {
            values = Others.values();
        }

        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(1);

        for (Type type: values) {
            double v = value / type.getWeight();

            if (v >= 1) {
                return format.format(v)+" "+type.getSymbol();
            }
        }

        Type last = values[values.length - 1];
        return format.format(value / last.getWeight())+" "+last.getSymbol();
    }

    @Override
    public int compareTo(Mass o) {
        return Double.compare(getValue(), o.getValue());
    }

    @Override
    public String toString() {
        return toString(SI.class);
    }
}
