package Extras;
import Units.ByteSize;

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
        System.out.println(v);
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
        /*
         for (int i=0;i<4;i++) {
            bytes[i] = (byte) (v >> (8 * i));
        }
         */
        return bytes[0] | (bytes[1] << 8) | (bytes[2] << 16) | (bytes[3] << 24);
    }

    public float getFloat (int pos) {
        return Float.intBitsToFloat(getInt(pos));
    }

    public long getLong (int pos) {
        byte[] bytes = getBytes(pos, 8);
        long ret = 0;

        for (int i=0;i<8;i++) {
            ret |= ((long) bytes[i] << (8 * (3 - i)));
        }

        return ret;
    }

    public double getDouble (int pos) {
        return Double.longBitsToDouble(getLong(pos));
    }

    public boolean[] array () {
        return bits;
    }

    public byte[] byteArray () {
        return getBytes(0, bits.length / 8);
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
