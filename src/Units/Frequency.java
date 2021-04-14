package Units;

import java.text.NumberFormat;

public class Frequency implements Comparable<Frequency> {
    public enum Type {
        TeraHertz(1e12, "THz"),
        GigaHertz(1e9, "GHz"),
        MegaHertz(1e6, "MHz"),
        KiloHertz(1e3, "kHz"),
        Hertz(1, "Hz");

        double w;
        String symbol;
        private Type (double w, String symbol) {
            this.w = w;
            this.symbol = symbol;
        }
    }

    final private double value;

    public Frequency(double hz) {
        this.value = hz;
    }

    public Frequency(double freq, Type type) {
        this.value = freq * type.w;
    }

    // Add
    public Frequency add (double hz) {
        return new Frequency(this.value + hz);
    }

    public Frequency add (Frequency freq) {
        return new Frequency(this.value + freq.value);
    }

    // Subtr
    public Frequency subtr (double hz) {
        return new Frequency(this.value - hz);
    }

    public Frequency subtr (Frequency freq) {
        return new Frequency(this.value - freq.value);
    }

    // Mul
    public Frequency mul (double hz) {
        return new Frequency(this.value * hz);
    }

    public Frequency mul (Frequency freq) {
        return new Frequency(this.value * freq.value);
    }

    // Div
    public Frequency div (double hz) {
        return new Frequency(this.value / hz);
    }

    public Frequency div (Frequency freq) {
        return new Frequency(this.value / freq.value);
    }

    // Get value
    public double getValue (Type type) {
        return value / type.w;
    }

    public double getValue () {
        return value;
    }

    @Override
    public int compareTo(Frequency o) {
        return Double.compare(getValue(), o.getValue());
    }

    @Override
    public String toString() {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(1);

        for (Type type: Type.values()) {
            double v = value / type.w;

            if (v >= 1) {
                return format.format(v)+" "+type.symbol;
            }
        }

        return format.format(value)+" "+Type.Hertz.symbol;
    }
}
