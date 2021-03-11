package Units;

import java.text.NumberFormat;

public class Time implements Comparable<Time> {
    public enum Type {
        Years(31557600, "y"),
        Months(1036800, "M"),
        Weeks(604800, "w"),
        Days(86400, "d"),
        Hours(3600, "h"),
        Minutes(60, "m"),
        Seconds(1, "s"),
        Milliseconds(1e-3, "ms"),
        Nanoseconds(1e-9, "ns");

        double w;
        String symbol;
        private Type (double w, String symbol) {
            this.w = w;
            this.symbol = symbol;
        }
    }

    final private double time;

    public Time (double sec) {
        this.time = sec;
    }

    public Time (double time, Type type) {
        this.time = time * type.w;
    }

    // Add
    public Time add (double sec) {
        return new Time(this.time + sec);
    }

    public Time add (Time time) {
        return new Time(this.time + time.time);
    }

    // Subtr
    public Time subtr (double sec) {
        return new Time(this.time - sec);
    }

    public Time subtr (Time time) {
        return new Time(this.time - time.time);
    }

    // Get value
    public double getValue (Type type) {
        return time / type.w;
    }

    public double getValue () {
        return time;
    }

    @Override
    public int compareTo(Time o) {
        return Double.compare(getValue(), o.getValue());
    }

    @Override
    public String toString() {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(1);

        double value = this.time;
        String out = "";

        for (Type type: Type.values()) {
            int v = (int)(value / type.w);

            if (v >= 1) {
                out += " "+format.format(v)+" "+type.symbol;
                value -= v * type.w;
            }
        }

        if (out.equals("")) {
            return format.format(getValue(Type.Nanoseconds))+" ns";
        }

        return out.substring(1);
    }
}
