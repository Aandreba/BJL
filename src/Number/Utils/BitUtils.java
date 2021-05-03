package Number.Utils;
import java.util.Arrays;

public class BitUtils {
    public static void flip (boolean[] bits) {
        int len = bits.length;
        for (int i=0;i<len;i++) {
            bits[i] = !bits[i];
        }
    }

    public static boolean[] flipped (boolean... bits) {
        int len = bits.length;
        boolean[] newBits = new boolean[len];

        for (int i=0;i<len;i++) {
            newBits[i] = !bits[i];
        }

        return newBits;
    }

    public static boolean getBit (byte value, int pos) {
        return ((value >> pos) & 1) == 1;
    }

    public static boolean getBit (short value, int pos) {
        return ((value >> pos) & 1) == 1;
    }

    public static boolean getBit (char value, int pos) {
        return ((value >> pos) & 1) == 1;
    }

    public static boolean getBit (int value, int pos) {
        return ((value >> pos) & 1) == 1;
    }

    public static boolean getBit (long value, int pos) {
        return ((value >> pos) & 1) == 1;
    }

    public static boolean[] getBits (byte... values) {
        int vl = values.length;
        boolean[] bits = new boolean[8 * vl];

        for (int i=0;i<vl;i++) {
            for (int j=0;j<8;j++) {
                bits[i * 8 + j] = getBit(values[i], j);
            }
        }

        return bits;
    }

    public static byte getByte (boolean... bits) {
        int length = Math.min(8, bits.length);
        byte value = 0;

        for (int i=0;i<length;i++) {
            if (bits[length - (1 + i)]) {
                value |= 1 << i;
            }
        }

        return value;
    }

    public static byte[] getBytes (boolean... bits) {
        int bl = bits.length;
        int num = bl / 8;
        byte[] bytes = new byte[num];

        for (int i=0;i<num;i++) {
            int j = 8 * i;
            bytes[i] = getByte(Arrays.copyOfRange(bits, j, j + 8));
        }

        return bytes;
    }
}
