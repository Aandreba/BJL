package Number;

public class UInt extends Number implements Comparable<UInt> {
    final private boolean classLoaded = false;

    final public static UInt MIN_VALUE = new UInt(0);
    final public static UInt MAX_VALUE = new UInt(0xFFFFFFFF);

    final public int raw;

    public UInt (int bits) {
        if (!classLoaded) {
            System.loadLibrary("uint");
        }
        this.raw = bits;
    }

    public UInt (long value) {
        this.raw = (int) value;
    }

    @Override
    public int compareTo (UInt o) {
        return 0;
    }

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public long longValue() {
        return raw >= 0 ? raw : 0xFFFFFFFFL - raw;
    }

    @Override
    public float floatValue() {
        return (float) longValue();
    }

    @Override
    public double doubleValue() {
        return (double) longValue();
    }

    public static UInt parse (int value) {
        return new UInt((long) value);
    }

    private static native long toLong (int raw);
    private static native int toUint (long val);
}
