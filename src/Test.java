import Extras.BitBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Test {
    public static void main (String[] args) throws IOException {
        BitBuffer buffer = new BitBuffer(0);
        buffer.add(1.5f);

        System.out.println(Arrays.toString(buffer.byteArray()));
        System.out.println(buffer.getInt(0));
        System.out.println(buffer.getFloat(0));
        System.out.println(Arrays.toString(ByteBuffer.allocate(4).putFloat(1.5f).array()));
    }
}
