package Number;

import java.util.Arrays;

public class UBigInteger extends Number implements Comparable<UBigInteger> {
    final static long LONG_MASK = 0xffffffffL;
    final private byte[] raw;

    public UBigInteger (byte... raw) {
        this.raw = raw;
    }

    public UBigInteger (short raw) {
        this((byte) (raw >> 8), (byte) raw);
    }

    public UBigInteger (int raw) {
        this((byte) (raw >> 24), (byte) (raw >> 16), (byte) (raw >> 8), (byte) raw);
    }

    public UBigInteger (long raw) {
        this((byte) (raw >> 56), (byte) (raw >> 48), (byte) (raw >> 40), (byte) (raw >> 32), (byte) (raw >> 24), (byte) (raw >> 16), (byte) (raw >> 8), (byte) raw);
    }

    public UBigInteger sum (UBigInteger y) {
        return sum(this, y);
    }

    public byte getByte (int pos) {
        return raw[pos];
    }

    public boolean getBit (int bytePos, int pos) {
        return ((raw[bytePos] >> pos) & 1) == 1;
    }

    public boolean getBit (int pos) {
        int bytePos = pos / 8;
        int bitPos = pos % 8;
        return getBit(bytePos, bitPos);
    }

    public static UBigInteger sum (UBigInteger x, UBigInteger y) {
        return new UBigInteger(sum(x.raw, y.raw));
    }

    private static byte[] sum (byte[] x, byte[] y) {
        int len1 = x.length;
        int len2 = y.length;
        int length = Math.max(len1, len2);

        byte[] result = new byte[length];
        byte carry = 0;

        for (int i=length-1;i>=0;i--) {
            int sum = x[i] + y[i] + carry;
            result[i] = (byte) sum;
            carry = (byte) (sum >> 8);
        }

        return result;
    }

    @Override
    public byte byteValue() {
        return (byte) Byte.toUnsignedInt(raw[0]);
    }

    @Override
    public short shortValue() {
        short value = 0;
        int rlength = raw.length;
        int length = Math.min(2, rlength);
        int diff = rlength - length;

        for (int i=0;i<length;i++) {
            value <<= 8;
            value |= (int) raw[diff + i] & 0xFF;
        }

        return value;
    }

    @Override
    public int intValue() {
        int value = 0;
        int rlength = raw.length;
        int length = Math.min(4, rlength);
        int diff = length - rlength;

        for (int i=0;i<length;i++) {
            value <<= 8;
            value |= (int) raw[diff + i] & 0xFF;
        }

        return value;
    }

    @Override
    public long longValue() {
        int value = 0;
        int rlength = raw.length;
        int length = Math.min(8, rlength);
        int diff = length - rlength;

        for (int i=0;i<length;i++) {
            value <<= 8;
            value |= (int) raw[diff + i] & 0xFF;
        }

        return value;
    }

    @Override
    public float floatValue() {
        return (float) longValue();
    }

    @Override
    public double doubleValue() {
        return (double) longValue();
    }

    /**
     * Based on java.math.BigInteger#compareMagnitude(BigInteger)
     * @see Comparable#compareTo(Object)
     */
    @Override
    public int compareTo (UBigInteger val) {
        byte[] m1 = raw;
        int len1 = m1.length;
        byte[] m2 = val.raw;
        int len2 = m2.length;
        if (len1 < len2)
            return -1;
        if (len1 > len2)
            return 1;
        for (int i = 0; i < len1; i++) {
            byte a = m1[i];
            byte b = m2[i];
            if (a != b)
                return ((a & LONG_MASK) < (b & LONG_MASK)) ? -1 : 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "UBigInteger{" +
                "raw=" + Arrays.toString(raw) +
                '}';
    }
}
