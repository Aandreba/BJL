package Number;

public class UInt extends UBigInteger {
    final public static UInt MIN_VALUE = new UInt(0);
    final public static UInt MAX_VALUE = new UInt(-1);

    public UInt (int raw) {
        super(raw);
    }
}
