package Extras;
import Units.ByteSize;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;

public class BitBuffer {
    boolean[] bits;

    public BitBuffer (boolean[] bits) {
        this.bits = bits;
    }

    public BitBuffer (int bits) {
        this(new boolean[bits]);
    }

    public BitBuffer (ByteSize size) {
        this(Mathx.roundToInt(size.getValue(ByteSize.Base.Bits)));
    }

    public BitBuffer add (boolean bit) {
        bits = extendedArray(bit);
        return this;
    }

    public BitBuffer add (byte b) {
        bits = extendedArray(getBits(b));
        return this;
    }

    public BitBuffer add (char v) {
        byte[] bytes = new byte[2];
        for (int i=0;i<2;i++) {
            bytes[i] = (byte) (v >> (8 * i));
        }

        bits = extendedArray(getBits(bytes));
        return this;
    }

    public BitBuffer add (short v) {
        byte[] bytes = new byte[2];
        for (int i=0;i<2;i++) {
            bytes[i] = (byte) (v >> (8 * i));
        }

        bits = extendedArray(getBits(bytes));
        return this;
    }

    public BitBuffer add (int v) {
        byte[] bytes = new byte[4];
        for (int i=0;i<4;i++) {
            bytes[i] = (byte) (v >> (8 * i));
        }

        bits = extendedArray(getBits(bytes));
        return this;
    }

    public BitBuffer add (float d) {
        int v = Float.floatToIntBits(d);
        return add(v);
    }

    public BitBuffer add (long v) {
        byte[] bytes = new byte[8];
        for (int i=0;i<8;i++) {
            bytes[i] = (byte) (v >> (8 * i));
        }

        bits = extendedArray(getBits(bytes));
        return this;
    }

    public BitBuffer add (double d) {
        long v = Double.doubleToLongBits(d);
        return add(v);
    }

    public BitBuffer add (byte... bytes) {
        for (int i=0;i<bytes.length;i++) {
            add(bytes[i]);
        }

        return this;
    }

    public BitBuffer add (char... chars) {
        for (int i=0;i<chars.length;i++) {
            add(chars[i]);
        }

        return this;
    }

    public BitBuffer add (short... shorts) {
        for (int i=0;i<shorts.length;i++) {
            add(shorts[i]);
        }

        return this;
    }

    public BitBuffer add (int... ints) {
        for (int i=0;i<ints.length;i++) {
            add(ints[i]);
        }

        return this;
    }

    public BitBuffer add (float... floats) {
        for (int i=0;i<floats.length;i++) {
            add(floats[i]);
        }

        return this;
    }

    public BitBuffer add (long... longs) {
        for (int i=0;i<longs.length;i++) {
            add(longs[i]);
        }

        return this;
    }

    public BitBuffer add (double... doubles) {
        for (int i=0;i<doubles.length;i++) {
            add(doubles[i]);
        }

        return this;
    }

    public BitBuffer add (CharSequence text) {
        for (int i=0;i<text.length();i++) {
            add(text.charAt(i));
        }

        return this;
    }

    public boolean get (int pos) {
        return bits[pos];
    }

    public boolean[] get (int offset, int length) {
        boolean[] result = new boolean[length];
        for (int i=0;i<length;i++) {
            result[i] = bits[offset + i];
        }

        return result;
    }

    public byte getByte (int pos) {
        boolean[] bits = get(pos, 8);
        byte result = 0;

        for (int i=0;i<8;i++) {
            if (bits[i]) {
                byte b = (byte) (1 << i);
                result = (byte) (result | b);
            }
        }

        return result;
    }

    public byte[] getBytes (int pos, int size) {
        boolean[] bits = get(pos, 8 * size);
        byte[] result = new byte[size];

        for (int j=0;j<size;j++) {
            for (int i = 0; i < 8; i++) {
                if (bits[8 * j + i]) {
                    byte b = (byte) (1 << i);
                    result[j] = (byte) (result[j] | b);
                }
            }
        }

        return result;
    }

    public char getChar (int pos) {
        byte[] bytes = getBytes(pos, 2);
        int value = bytes[0] | (bytes[1] << 8);

        return (char) value;
    }

    public short getShort (int pos) {
        byte[] bytes = getBytes(pos, 2);
        int value = bytes[0] | (bytes[1] << 8);

        return (short) value;
    }

    public int getInt (int pos) {
        byte[] bytes = getBytes(pos, 4);
        int result = 0;

        for (int i=0;i<4;i++) {
            int j = 3 - i;
            result |= (bytes[i] & 0xFF) << (8 * j);
        }

        return result;
    }

    public float getFloat (int pos) {
        return Float.intBitsToFloat(getInt(pos));
    }

    public long getLong (int pos) {
        byte[] bytes = getBytes(pos, 8);
        long result = 0;

        for (int i=0;i<8;i++) {
            int j = 7 - i;
            result |= (long) (bytes[i] & 0xFF) << (8 * j);
        }

        return result;
    }

    public double getDouble (int pos) {
        return Double.longBitsToDouble(getLong(pos));
    }

    public BitBuffer set (int pos, boolean value) {
        bits[pos] = value;
        return this;
    }

    public BitBuffer set (int offset, byte value) {
        boolean[] bits = getBits(value);
        for (int i=0;i<8;i++) {
            set(offset + i, bits[i]);
        }

        return this;
    }

    public BitBuffer set (int offset, char value) {
        for (int i=0;i<2;i++) {
            set(offset + (8 * i), (byte) (value >> (8 * i)));
        }

        return this;
    }

    public BitBuffer set (int offset, short value) {
        for (int i=0;i<2;i++) {
            set(offset + (8 * i), (byte) (value >> (8 * i)));
        }

        return this;
    }

    public BitBuffer set (int offset, int value) {
        for (int i=0;i<4;i++) {
            set(offset + (8 * i), (byte) (value >> (8 * i)));
        }

        return this;
    }

    public BitBuffer set (int offset, float value) {
        return set(offset, Float.floatToIntBits(value));
    }

    public BitBuffer set (int offset, long value) {
        for (int i=0;i<8;i++) {
            set(offset + (8 * i), (byte) (value >> (8 * i)));
        }

        return this;
    }

    public BitBuffer set (int offset, double value) {
        return set(offset, Double.doubleToLongBits(value));
    }

    public boolean[] array () {
        return bits;
    }

    public byte[] byteArray () {
        return getBytes(0, Mathx.ceil(bits.length / 8f));
    }

    public static boolean getBit (int pos, byte b) {
        return ((b >> pos) & 1) == 1;
    }

    public static boolean[] getBits (byte b) {
        boolean[] bits = new boolean[8];
        for (int i = 0; i < 8; i++) {
            bits[i] = getBit(i, b);
        }

        return bits;
    }

    public static boolean[] getBits (byte... bytes) {
        boolean[] bits = new boolean[8 * bytes.length];
        for (int j=0;j<bytes.length;j++) {
            for (int i = 0; i < 8; i++) {
                int k = 8 * (bytes.length - 1 - j) + i;
                bits[k] = getBit(i, bytes[j]);
            }
        }

        return bits;
    }

    private boolean[] extendedArray (boolean... bits) {
        boolean[] result = new boolean[this.bits.length + bits.length];

        for (int i=0;i<this.bits.length;i++) {
            result[i] = this.bits[i];
        }
        for (int i=this.bits.length;i<bits.length;i++) {
            result[i] = bits[i];
        }

        return result;
    }
}
