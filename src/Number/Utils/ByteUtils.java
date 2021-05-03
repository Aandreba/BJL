package Number.Utils;

public class ByteUtils {
    public static byte getByte (short value, int pos) {
        return (byte) (value >> (8 * pos));
    }

    public static byte getByte (char value, int pos) {
        return (byte) (value >> (8 * pos));
    }

    public static byte getByte (int value, int pos) {
        return (byte) (value >> (8 * pos));
    }

    public static byte getByte (long value, int pos) {
        return (byte) (value >> (8 * pos));
    }

    public static byte[] getBytes (short value) {
        return new byte[] { getByte(value, 0), getByte(value, 1) };
    }

    public static byte[] getBytes (char value) {
        return new byte[] { getByte(value, 0), getByte(value, 1) };
    }

    public static byte[] getBytes (int value) {
        byte[] bytes = new byte[4];
        for (int i=0;i<4;i++) {
            bytes[i] = getByte(value, i);
        }

        return bytes;
    }

    public static byte[] getBytes (long value) {
        byte[] bytes = new byte[4];
        for (int i=0;i<4;i++) {
            bytes[i] = getByte(value, i);
        }

        return bytes;
    }
}
