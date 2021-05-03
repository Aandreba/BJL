package Number;

import Extras.Rand;
import Number.Utils.BitUtils;
import Number.Utils.ByteUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

public class UInteger extends Number implements Comparable<UInteger>, Iterable<Boolean> {
    final private boolean[] raw;

    public UInteger (boolean... raw) {
        this.raw = removeTrailing(raw);
    }

    public UInteger (byte... raw) {
        this(BitUtils.getBits(raw));
    }

    public UInteger (short raw) {
        this(ByteUtils.getBytes(raw));
    }

    public UInteger (char raw) {
        this(ByteUtils.getBytes(raw));
    }

    public UInteger (int raw) {
        this(ByteUtils.getBytes(raw));
    }

    public UInteger (long raw) {
        this(ByteUtils.getBytes(raw));
    }

    public UInteger (Number raw) {
        this(raw.longValue());
    }

    public boolean getBit (int pos) {
        return raw[pos];
    }

    public byte getByte (int pos) {
        int k = 8 * pos;
        return BitUtils.getByte(Arrays.copyOfRange(raw, k, k + 8));
    }

    public int getNumOfBits () {
        return raw.length;
    }

    public UInteger and (UInteger y) {
        int len = getNumOfBits();
        boolean[] value = new boolean[len];
        for (int i=0;i<len;i++) {
            value[i] = raw[i] && y.raw[i];
        }

        return new UInteger(value);
    }

    public UInteger nand (UInteger y) {
        int len = getNumOfBits();
        boolean[] value = new boolean[len];
        for (int i=0;i<len;i++) {
            value[i] = !(raw[i] && y.raw[i]);
        }

        return new UInteger(value);
    }

    public UInteger or (UInteger y) {
        int len = getNumOfBits();
        boolean[] value = new boolean[len];
        for (int i=0;i<len;i++) {
            value[i] = raw[i] | y.raw[i];
        }

        return new UInteger(value);
    }

    public UInteger nor (UInteger y) {
        int len = getNumOfBits();
        boolean[] value = new boolean[len];
        for (int i=0;i<len;i++) {
            value[i] = !(raw[i] | y.raw[i]);
        }

        return new UInteger(value);
    }

    public UInteger xor (UInteger y) {
        int len = getNumOfBits();
        boolean[] value = new boolean[len];
        for (int i=0;i<len;i++) {
            value[i] = raw[i] ^ y.raw[i];
        }

        return new UInteger(value);
    }

    public UInteger not () {
        return new UInteger(BitUtils.flipped(raw));
    }

    public UInteger leftShift (int bits) {
        int len = getNumOfBits();

        boolean[] values = new boolean[len + bits];
        System.arraycopy(raw, 0, values, bits, len);

        return new UInteger(values);
    }

    public UInteger rightShift (int bits) {
        int len = getNumOfBits();
        int nlen = len - bits;

        boolean[] values = new boolean[nlen];
        System.arraycopy(raw, bits, values, 0, nlen);

        return new UInteger(values);
    }

    public UInteger paddedRightShift (int bits) {
        int len = getNumOfBits();
        int nlen = len - bits;

        boolean[] values = new boolean[len];
        System.arraycopy(raw, bits, values, bits, nlen);

        return new UInteger(values);
    }

    public boolean isOdd () {
        if (getNumOfBits() == 0) {
            return true;
        }

        return raw[0];
    }

    public boolean isEven () {
        return !isOdd();
    }

    public boolean isBiggerThan (UInteger y) {
        return compareTo(y) > 0;
    }

    public boolean isBiggerThan (Number y) {
        return compareTo(y) > 0;
    }

    public boolean isSmallerThan (UInteger y) {
        return compareTo(y) < 0;
    }

    public boolean isSmallerThan (Number y) {
        return compareTo(y) < 0;
    }

    public UInteger sum (UInteger y) {
        return new UInteger(sum(raw, y.raw));
    }

    public UInteger sum (Number y) {
        return sum(new UInteger(y.longValue()));
    }

    public UInteger subtr (UInteger y) {
        int len1 = getNumOfBits();
        int len2 = y.getNumOfBits();
        boolean[] X, Y;

        if (len1 == len2) {
            X = raw;
            Y = y.raw;
        } else if (len1 > len2) {
            X = raw;
            Y = new boolean[len1];
            System.arraycopy(y.raw, 0, Y, 0, len2);
        } else {
            Y = y.raw;
            X = new boolean[len2];
            System.arraycopy(raw, 0, X, 0, len1);
        }

        BitUtils.flip(Y);
        Y = sum(Y, true);
        boolean[] result = sum(X, Y);
        return new UInteger(Arrays.copyOf(result, result.length - 1));
    }

    public UInteger subtr (Number y) {
        return sum(new UInteger(y.longValue()));
    }

    public UInteger invSubtr (Number y) {
        return new UInteger(y.longValue()).subtr(this);
    }

    public UInteger mul (UInteger y) {
        return new UInteger(mul(raw, y.raw));
    }

    public UInteger mul (Number y) {
        return mul(new UInteger(y.longValue()));
    }

    public UInteger div (UInteger y) {
        UInteger X = this;
        UInteger result = new UInteger(0);

        while (X.compareTo(y) > 0) {
            X = X.subtr(y);
            result = result.sum((byte) 1);
        }

        return result;
    }

    public UInteger div (Number y) {
        return div(new UInteger(y));
    }

    public UInteger remainder (UInteger y) {
        UInteger X = this;
        UInteger result = new UInteger(0);

        while (X.compareTo(y) > 0) {
            X = X.subtr(y);
        }

        return X;
    }

    public UInteger remainder (Number y) {
        return remainder(new UInteger(y));
    }

    public UInteger[] divAndRemainder (UInteger y) {
        UInteger X = this;
        UInteger result = new UInteger(0);

        while (X.compareTo(y) > 0) {
            X = X.subtr(y);
            result = result.sum((byte) 1);
        }

        return new UInteger[] { result, y.subtr(X) };
    }

    public UInteger[] divAndRemainder (Number y) {
        return divAndRemainder(new UInteger(y));
    }

    public UInteger mod (UInteger y) {
        // TODO
        return null;
    }

    public UInteger pow (UInteger y) {
        UInteger result = this;
        UInteger i = new UInteger((byte) 1);

        while (y.compareTo(i) > 0) {
            result = result.mul(this);
            i = i.sum((byte) 1);
        }

        return result;
    }

    public UInteger gcd (UInteger y) {
        final UInteger two = new UInteger((byte) 2);

        if (equals(y)) {
            return this;
        } else if (equals((byte) 0)) {
            return y;
        } else if (y.equals((byte) 0)) {
            return this;
        }

        boolean odd1 = isOdd();
        boolean odd2 = y.isOdd();

        if (odd1) {
            if (!odd2) {
                return this.gcd(y.div(two));
            }

            if (isBiggerThan(y)) {
                return this.subtr(y).div(two).gcd(y);
            } else {
                return y.subtr(this).div(two).gcd(this);
            }
        } else {
            if (!odd2) {
                return this.div(two).gcd(y);
            } else {
                return this.div(two).gcd(y.div(two)).mul(two);
            }
        }
    }

    public UInteger lcm (UInteger y) {
        return mul(y).div(gcd(y));
    }

    public UInteger ceilDiv (UInteger y) { // aka. round up
        UInteger[] div = this.divAndRemainder(y);
        if (div[1].compareTo((byte) 0) > 0) {
            return div[0].sum((byte) 1);
        }

        return div[0];
    }

    public UInteger max (UInteger y) {
        return max(this, y);
    }

    public UInteger min (UInteger y) {
        return min(this, y);
    }

    public static UInteger max (UInteger x, UInteger y) {
        return x.compareTo(y) >= 0 ? x : y;
    }

    public static UInteger min (UInteger x, UInteger y) {
        return x.compareTo(y) <= 0 ? x : y;
    }

    public static UInteger random (int bits) {
        boolean[] values = new boolean[bits];
        for (int i=0;i<bits;i++) {
            values[i] = Rand.getBool();
        }

        return new UInteger(values);
    }

    private static boolean[] removeTrailing (boolean[] in) {
        for (int i=in.length-1;i>=0;i--) {
            if (in[i]) {
                return Arrays.copyOf(in, i+1);
            }
        }

        return new boolean[0];
    }

    private static boolean[] sum (boolean x, boolean y) {
        return new boolean[] { x ^ y, x && y };
    }

    private static boolean[] sum (boolean[] x, boolean... y) {
        int len1 = x.length;
        int len2 = y.length;

        int length = Math.max(len1, len2);
        boolean[] result = new boolean[length];

        boolean carry = false;
        for (int i=0;i<length;i++) {
            if (i >= len1) {
                result[i] = y[i] ^ carry;
                carry = y[i] && carry;
                continue;
            } else if (i >= len2) {
                result[i] = x[i] ^ carry;
                carry = x[i] && carry;
                continue;
            }

            result[i] = (x[i] && y[i] && carry) | (x[i] ^ y[i] ^ carry);
            carry = (x[i] && (y[i] | carry)) | (!x[i] && y[i] && carry);
        }

        if (carry) {
            result = Arrays.copyOf(result, length + 1);
            result[length] = true;
        }

        return result;
    }

    private static boolean[] mul (boolean[] x, boolean[] y) {
        int len1 = x.length;
        int len2 = y.length;
        boolean[] result = new boolean[len1];

        for (int i=0;i<len2;i++) {
            if (!y[i]) {
                continue;
            }

            boolean[] add = new boolean[len1 + i];
            System.arraycopy(x, 0, add, i, len1);
            result = sum(result, add);
        }

        return result;
    }

    @Override
    public byte byteValue() {
        return getByte(0);
    }

    @Override
    public short shortValue() {
        short value = 0;
        int length = Math.min(16, raw.length);

        for (int i=0;i<length;i++) {
            if (raw[i]) {
                value |= 1 << i;
            }
        }

        return value;
    }

    @Override
    public int intValue() {
        int value = 0;
        int length = Math.min(32, raw.length);

        for (int i=0;i<length;i++) {
            if (raw[i]) {
                value |= 1 << i;
            }
        }

        return value;
    }

    @Override
    public long longValue() {
        long value = 0;
        int length = Math.min(64, raw.length);

        for (int i=0;i<length;i++) {
            if (raw[i]) {
                value |= 1 << i;
            }
        }

        return value;
    }

    @Override
    public float floatValue() {
        return (float) intValue();
    }

    @Override
    public double doubleValue() {
        return (double) longValue();
    }

    @Override
    public int compareTo (UInteger val) {
        boolean[] m1 = raw;
        int len1 = m1.length;
        boolean[] m2 = val.raw;
        int len2 = m2.length;

        if (len1 > len2) {
            return 1;
        } else if (len2 > len1) {
            return -1;
        }

        for (int i=len1-1;i>=0;i--) {
            if (m1[i] && !m2[i]) {
                return 1;
            } else if (!m1[i] && m2[i]) {
                return -1;
            }
        }

        return 0;
    }

    public int compareTo (Number val) {
        return compareTo(new UInteger(val));
    }

    @Override
    public Iterator<Boolean> iterator() {
        return new Iterator<Boolean>() {
            int i=0;

            @Override
            public boolean hasNext() {
                return i < getNumOfBits();
            }

            @Override
            public Boolean next() {
                return raw[i++];
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UInteger uInteger = (UInteger) o;
        return Arrays.equals(raw, uInteger.raw);
    }

    public boolean equals(Number o) {
        return compareTo(o) == 0;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(raw);
    }

    @Override
    public String toString() {
        return "Integral {" +
                "bits =" + Arrays.toString(raw) +
                '}';
    }
}
