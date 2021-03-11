package Units;

public class ByteSize {
    public interface Type {
        double getWeight();
        String getSymbol();
    };

    public enum Base implements Type {
        Bytes(1,"B"),
        Bits(1d/8,"b");

        double w;
        String s;
        private Base (double w, String s) {
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

    public enum SI implements Type {
        Yottabytes(1e24,"YB"),
        Zettabytes(1e21,"ZB"),
        Exabytes(1e18,"EB"),
        Petabytes(1e15,"PB"),
        Terabytes(1e12,"TB"),
        Gigabytes(1e9,"GB"),
        MegaBytes(1e6,"MB"),
        Kilobytes(1e3,"kB");

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

    public enum ISO implements Type {
        Yobibytes(Math.pow(2,80),"YiB"),
        Zebibytes(Math.pow(2,70),"ZiB"),
        Exbibytes(Math.pow(2,60),"EiB"),
        Pebibytes(Math.pow(2,50),"PiB"),
        Tebibytes(Math.pow(2,40),"TiB"),
        Gibibytes(Math.pow(2,30),"GiB"),
        MebiBytes(Math.pow(2,20),"MiB"),
        Kibibytes(Math.pow(2,10),"KiB");

        double w;
        String s;
        private ISO (double w, String s) {
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

    public ByteSize (double b) {
        this.value = b;
    }

    public ByteSize (double v, Type type) {
        this.value = v * type.getWeight();
    }

    public double getValue (Type type) {
        return this.value / type.getWeight();
    }

    public double getValue () {
        return value;
    }
}
