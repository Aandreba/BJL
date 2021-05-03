import Number.UInteger;

import java.io.IOException;

public class TestRand {
    public static void main (String[] args) throws IOException {
        UInteger a = new UInteger(2000);
        UInteger b = new UInteger(52);
        UInteger pow = a.pow(b);

        //System.out.println(a.intValue()+", "+b.intValue());
        System.out.println(a.gcd(b).intValue());
        System.out.println(a.lcm(b).intValue());
    }
}