package Extras;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class Functions {
    public static <I,O> O apply (I input, Function<I,O> function) {
        return function.apply(input);
    }

    public static <I,O> ArrayList<O> forEach (Collection<I> input, Function<I, O> function) {
        ArrayList<O> output = new ArrayList<>();
        for (I in: input) {
            output.add(function.apply(in));
        }

        return output;
    }

    public static <I,O> ArrayList<O> forEach (I[] input, Function<I, O> function) {
        ArrayList<O> output = new ArrayList<>();
        for (I in: input) {
            output.add(function.apply(in));
        }

        return output;
    }
}
