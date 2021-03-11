package Vector;

import Extras.Rand;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.jcurand.curandGenerator;
import jcuda.runtime.cudaMemcpyKind;

import java.nio.ByteBuffer;

import static jcuda.jcurand.JCurand.*;
import static jcuda.jcurand.curandRngType.CURAND_RNG_PSEUDO_DEFAULT;
import static jcuda.runtime.JCuda.cudaMalloc;
import static jcuda.runtime.JCuda.cudaMemcpy;

public class StatVector extends RelVector {
    protected double[] values;

    public StatVector (int length) {
        super(length);
        this.values = new double[length];
    }

    public StatVector (double... values) {
        super(values.length);
        this.values = values;
    }

    public StatVector (long... values) {
        super(values.length);
        this.values = new double[values.length];

        for (int i=0;i<values.length;i++) {
            this.values[i] = values[i];
        }
    }

    public StatVector (float... values) {
        super(values.length);
        this.values = new double[values.length];

        for (int i=0;i<values.length;i++) {
            this.values[i] = values[i];
        }
    }

    public StatVector (int... values) {
        super(values.length);
        this.values = new double[values.length];

        for (int i=0;i<values.length;i++) {
            this.values[i] = values[i];
        }
    }

    public static StatVector random (int length, double from, double to) {
        return Vector.random(length, from, to).toStatic();
    }

    public static StatVector random (int length) {
        return Vector.random(length).toStatic();
    }

    public static StatVector randomCUDA (int length, boolean isDouble) {
        int n = isDouble ? Sizeof.DOUBLE : Sizeof.FLOAT;

        Pointer p = new Pointer();
        cudaMalloc(p, length * n);

        curandGenerator generator = new curandGenerator();
        curandCreateGenerator(generator, CURAND_RNG_PSEUDO_DEFAULT);
        curandSetPseudoRandomGeneratorSeed(generator, Rand.getLong());

        curandGenerateUniform(generator, p, length);

        if (isDouble) {
            double[] r = new double[length];
            cudaMemcpy(Pointer.to(r), p, length * n, cudaMemcpyKind.cudaMemcpyDeviceToHost);

            return new StatVector(r);
        }

        float[] r = new float[length];
        cudaMemcpy(Pointer.to(r), p, length * n, cudaMemcpyKind.cudaMemcpyDeviceToHost);

        return new StatVector(r);
    }

    public static StatVector randomCUDA (int length) {
        return randomCUDA(length, true);
    }

    public static StatVector randomCUDA (int length, double from, double to) {
        return randomCUDA(length, true).mul(to - from).sum(from).toStatic();
    }

    public static StatVector randomCUDA (int length, float from, float to) {
        return randomCUDA(length, false).mul(to - from).sum(from).toStatic();
    }

    public static StatVector randomIntCUDA (int length, boolean isLong) {
        int n = isLong ? Sizeof.LONG : Sizeof.INT;

        Pointer p = new Pointer();
        cudaMalloc(p, length * n);

        curandGenerator generator = new curandGenerator();
        curandCreateGenerator(generator, CURAND_RNG_PSEUDO_DEFAULT);
        curandSetPseudoRandomGeneratorSeed(generator, Rand.getLong());

        curandGenerateUniform(generator, p, length);

        if (isLong) {
            long[] r = new long[length];
            cudaMemcpy(Pointer.to(r), p, length * n, cudaMemcpyKind.cudaMemcpyDeviceToHost);

            return new StatVector(r);
        }

        int[] r = new int[length];
        cudaMemcpy(Pointer.to(r), p, length * n, cudaMemcpyKind.cudaMemcpyDeviceToHost);

        return new StatVector(r);
    }

    public static StatVector randomIntCUDA (int length) {
        return randomIntCUDA(length, false);
    }

    public static StatVector randomIntCUDA (int length, long from, long to) {
        return randomCUDA(length, true).mul(to - from).sum(from).round().toStatic();
    }

    public static StatVector randomIntCUDA (int length, int from, int to) {
        return randomCUDA(length, false).mul(to - from).sum(from).round().toStatic();
    }

    @Override
    public double get(int pos) {
        return this.values[pos];
    }

    @Override
    public double[] toArray() {
        return values;
    }

    @Override
    public void set (int pos, double value) {
        this.values[pos] = value;
    }

    public <T extends Number> void set (int pos, T value) {
        this.values[pos] = value.doubleValue();
    }

    public static StatVector fromByteBuffer (ByteBuffer bb) {
        StatVector ret = new StatVector(bb.capacity() / 8);

        for (int i=0;i<ret.length;i++) {
            ret.set(i, bb.getDouble(8*i));
        }

        return ret;
    }

    public static StatVector fromBytes (byte[] bytes) {
        return fromByteBuffer(ByteBuffer.wrap(bytes));
    }

    public StatVector clone () {
        StatVector ret = new StatVector(length);

        for (int i=0;i<length;i++) {
            ret.set(i, get(i));
        }

        return ret;
    }
}
