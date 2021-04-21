package Units;

import java.io.Serializable;
import java.text.NumberFormat;

public class Dist implements Comparable<Dist>, Serializable {
    public interface Type {
        double getWeight();
        String getSymbol();
    };

    public enum SI implements Type {
        Kilometres(1000,"km"),
        Meters(1,"m"),
        Millimetres(1e-3,"mm"),
        Nanometres(1e-9,"nm");

        double w;
        String s;
        private SI(double w, String s) {
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
        Inches(0.0254,"in"),
        Feet(0.3048,"ft"),
        Yards(0.9144,"yd"),
        Miles(1609.344,"mi");

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
        Parsecs(3.0857e16,"pc"),
        LightYears(9.4607e15,"ly"),
        AstronomicalUnits(1.495978707e11,"au"),
        LightSeconds(299792458,"ls");

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
        Planks(1.616255e-35,"ℓP");

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

    private double value;

    public Dist (double m) {
        this.value = m;
    }

    public Dist (double v, Type type) {
        this.value = v * type.getWeight();
    }

    // Add
    public Dist add (double m) {
        return new Dist(this.value + m);
    }

    public Dist add (Dist dist) {
        return new Dist(this.value + dist.value);
    }

    // Subtr
    public Dist subtr (double m) {
        return new Dist(this.value - m);
    }

    public Dist subtr (Dist dist) {
        return new Dist(this.value - dist.value);
    }

    // Mul
    public Dist mul (double m) {
        return new Dist(this.value * m);
    }

    public Dist mul (Dist dist) {
        return new Dist(this.value * dist.getValue());
    }

    // Get value
    public double getValue (Type type) {
        return this.value / type.getWeight();
    }

    public double getValue () {
        return this.value;
    }

    public String toString (Type type) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(1);

        return format.format(getValue(type))+" "+type.getSymbol();
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
    public int compareTo(Dist o) {
        return Double.compare(getValue(), o.getValue());
    }

    @Override
    public String toString() {
        return toString(SI.class);
    }
}
