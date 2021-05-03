import Number.UBigInteger;
import Number.UInt;
import java.io.IOException;

public class TestRand {
    public static void main (String[] args) throws IOException {
        UBigInteger a = new UBigInteger(2000);
        UBigInteger b = new UBigInteger(53);
        UBigInteger c = a.sum(b);

        System.out.println(a+", "+b+", "+c);
        System.out.println(a.intValue()+", "+b.intValue()+", "+c.intValue());

    }
}