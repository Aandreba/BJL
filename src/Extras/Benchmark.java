package Extras;

import Units.Time;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Benchmark extends ArrayList<Runnable> {
    ArrayList<Runnable> preRun;

    public Benchmark(Runnable... runs) {
        super();
        this.preRun = new ArrayList<>();
        Collections.addAll(this, runs);
    }

    public Benchmark(Collection<Runnable> runs) {
        super(runs);
        this.preRun = new ArrayList<>();
    }

    /**
     * Benchmarks runnable inside class
     * @param epochs Amount of times runnable has to be run
     * @param index Runnable's index
     * @return Time to execute {@param epochs} {@param run} in nanoseconds
     */
    public Time benchmark (int epochs, int index) {
        Runnable prerun = null;
        try {
            prerun = preRun.get(index);
        } catch (Exception e) {};

        Runnable run = get(index);
        long time = 0;

        for (int i=0;i<epochs;i++) {
            if (prerun != null) {
                prerun.run();
            }

            long start = System.nanoTime();
            run.run();
            long end = System.nanoTime();

            time += end - start;
        }

        return new Time(time, Time.Type.Nanoseconds);
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

    /**
     * Adds a pre run at specified index. A prerun is a runnable executed before each iteration.
     * @param index Index of the prerun
     * @param run Runnable to add
     */
    public void setPreRun(int index, Runnable run) {
        try {
            preRun.set(index, run);
        } catch (Exception e) {
            preRun.add(index, run);
        }
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
}
