package Extras.Tuples;

import java.util.Objects;

public class Couple<A,B> {
    public A one;
    public B two;

    public Couple (A one, B two) {
        this.one = one;
        this.two = two;
    }

    public Couple<B,A> flipped() {
        return new Couple<>(two, one);
    }

    @Override
    public String toString() {
        return "{ "+one+", "+two+" }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Couple<?, ?> couple = (Couple<?, ?>) o;
        return Objects.equals(one, couple.one) &&
                Objects.equals(two, couple.two);
    }

    public static <A,B> Couple<A,B> create (A one, B two) {
        return new Couple<>(one, two);
    }
}
