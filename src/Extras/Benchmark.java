package Extras;

import Units.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Benchmark extends ArrayList<Runnable> {
    public Benchmark(Runnable... runs) {
        super();
        Collections.addAll(this, runs);
    }

    public Benchmark(Collection<Runnable> runs) {
        super(runs);
    }

    /**
     * Benckmarks runnable
     * @param epochs Amount of times runnable has to be run
     * @param run Runnable to execute
     * @return Time to execute {@param epochs} {@param run} in nanoseconds
     */
    public static Time benchmark (int epochs, Runnable run) {
        long start = System.nanoTime();
        for (int i=0;i<epochs;i++) {
            run.run();
        }
        long end = System.nanoTime();

        return new Time(end - start, Time.Type.Nanoseconds);
    }

    /**
     * Benchmarks runnable inside class
     * @param epochs Amount of times runnable has to be run
     * @param run Runnable's index
     * @return Time to execute {@param epochs} {@param run} in nanoseconds
     */
    public Time benchmark (int epochs, int run) {
        return benchmark(epochs, this.get(run));
    }

    /**
     * Benchmarks all runnables inside class
     * @param epochs Amount of times runnable has to be run
     * @return Time to execute each runnable {@param epochs} times in nanoseconds
     */
    public Time[] benchmark (int epochs) {
        Time[] times = new Time[this.size()];

        for (int i=0;i<this.size();i++) {
            times[i] = benchmark(epochs, i);
        }

        return times;
    }

    /**
     * Prints benchmark results
     * @param epochs Amount of times runnable has to be run
     * @param names Names to represent each runnable
     */
    public void benchmark (int epochs, String... names) {
        for (int i=0;i<names.length;i++) {
            Time time = benchmark(epochs, i);
            System.out.println(names[i]+": "+time);
        }
    }
}
