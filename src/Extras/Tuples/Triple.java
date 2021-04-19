package Extras.Tuples;

import java.util.Objects;

public class Triple<A,B,C> {
    public A one;
    public B two;
    public C three;

    public Triple (A one, B two, C three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }

    public Couple<A,B> ab () {
        return new Couple<>(one, two);
    }

    public Couple<A,C> ac () {
        return new Couple<>(one, three);
    }

    public Couple<B,A> ba () {
        return new Couple<>(two, one);
    }

    public Couple<B,C> bc () {
        return new Couple<>(two, three);
    }

    public Couple<C,A> ca () {
        return new Couple<>(three, one);
    }

    public Couple<C,B> cb () {
        return new Couple<>(three, two);
    }

    public Triple<C,B,A> flipped() {
        return new Triple<>(three, two, one);
    }

    @Override
    public String toString() {
        return "{ "+one+", "+two+", "+three+" }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(one, triple.one) &&
                Objects.equals(two, triple.two) &&
                Objects.equals(three, triple.three);
    }

    public static <A,B,C> Triple<A,B,C> create (A one, B two, C three) {
        return new Triple<>(one, two, three);
    }
}
