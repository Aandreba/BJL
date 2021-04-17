package Matrix;

import OpenCL.Context;
import OpenCL.Device.Device;
import Extras.Rand;
import Vector.Vector;
import Vector.StatVector;
import jcuda.jcublas.JCublas2;
import jcuda.jcublas.cublasHandle;
import jcuda.runtime.cudaMemcpyKind;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.blast.CLBlast;
import org.jocl.cl_event;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

import static jcuda.jcublas.JCublas2.*;
import static jcuda.jcublas.cublasOperation.CUBLAS_OP_N;
import static jcuda.runtime.JCuda.*;
import static org.jocl.CL.*;
import static org.jocl.blast.CLBlast.CLBlastClearCache;
import static org.jocl.blast.CLBlast.CLBlastSgemm;
import static org.jocl.blast.CLBlastLayout.CLBlastLayoutRowMajor;
import static org.jocl.blast.CLBlastTranspose.CLBlastTransposeNo;

/**
 * {@link Matrix} allows to easily do matrix operations
 * @author Alex Andreba
 * @since 0.1A
 */
public abstract class Matrix implements Iterable<Vector> {
    /**
     * Number of rows
     */
    final public int rows;

    /**
     * Number of columns
     */
    final public int cols;

    public Matrix (int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Create matrix that returns random numbers
     * @param rows Number of rows
     * @param cols Number of cols
     * @param from Minimum value
     * @param to Maximum value
     * @return Specified matrix
     * @see Rand
     */
    public static Matrix random (int rows, int cols, double from, double to) {
        return new Matrix (rows, cols) {
            @Override
            public double get(int row, int col) {
                return Rand.getDouble(from, to);
            }
        };
    }

    /**
     * Create matrix that returns random numbers between -1 and 1
     * @see #random(int, int, double, double)
     */
    public static Matrix random (int rows, int cols) {
        return random(rows, cols, -1, 1);
    }

    /**
     * Returns number of rows
     * @deprecated Used for legacy content. Preferably use {@link #rows}
     * @return Number of rows
     */
    @Deprecated
    public int getRows() {
        return rows;
    }

    /**
     * Returns number of columns
     * @deprecated Used for legacy content. Preferably use {@link #cols}
     * @return Number of columns
     */
    @Deprecated
    public int getCols() {
        return cols;
    }

    /**
     * Call to retrieve value from {@link Matrix}
     * @param row Value's row
     * @param col Value's col
     * @return Value at specified position
     */
    public abstract double get (int row, int col);

    /**
     * Retrieve specified value as a {@link Float}
     * @see #get(int, int)
     */
    public float getFloat (int row, int col) {
        return (float)get(row, col);
    }

    /**
     * Retrieve a full row
     * @param row Desired row
     * @return Specified row as a {@link Vector}
     */
    public Vector get (int row) {
        return new Vector(cols) {
            @Override
            public double get(int pos) {
                return Matrix.this.get(row, pos);
            }
        };
    }

    /**
     * Retrieve all values
     * @return Array containing matrix's values
     */
    public double[][] toArray () {
        double[][] array = new double[rows][cols];

        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                array[i][j] = get(i,j);
            }
        }

        return array;
    }

    /**
     * Retrieve all values as {@link Float}
     * @see #toArray()
     */
    public float[][] toFloatArray () {
        float[][] array = new float[rows][cols];

        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                array[i][j] = (float)get(i,j);
            }
        }

        return array;
    }

    /**
     * @return {@link Matrix} as a {@link Vector} in row-major order
     */
    public Vector toVector () {
        return new Vector(rows * cols) {
            @Override
            public double get(int pos) {
                int row = pos / cols;
                int col = pos % cols;

                return Matrix.this.get(row, col);
            }
        };
    }

    /**
     * Stores current {@link Matrix} values inside a {@link StatMatrix}
     * @return Resulting {@link StatMatrix}
     */
    public StatMatrix toStatic () {
        StatMatrix ret = new StatMatrix(rows, cols);
        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                ret.values[i][j] = get(i,j);
            }
        }

        return ret;
    }

    @Override
    public Iterator<Vector> iterator() {
        return new Iterator<Vector>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i+i < cols;
            }

            @Override
            public Vector next() {
                return get(i++);
            }
        };
    }

    // Sums

    /**
     * Adds up two matrices
     * @param b Matrix to sum
     * @return Resulting {@link Matrix}
     */
    public Matrix sum (Matrix b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) + b.get(row, col);
            }
        };
    }

    /**
     * Sums
     * @param b Matrix to sum
     * @return Resulting {@link Matrix}
     */
    public Matrix sum (Vector b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) + b.get(col);
            }
        };
    }

    public Matrix sum (double b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) + b;
            }
        };
    }

    public <T extends Number> Matrix sum (T b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) + b.doubleValue();
            }
        };
    }

    // Subtrs
    public Matrix subtr (Matrix b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) - b.get(row, col);
            }
        };
    }

    public Matrix subtr (Vector b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) - b.get(col);
            }
        };
    }

    public Matrix subtr (double b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) - b;
            }
        };
    }

    public <T extends Number> Matrix subtr (T b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) - b.doubleValue();
            }
        };
    }

    // Inv Subtrs
    public Matrix invSubtr (Vector b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return b.get(col) - Matrix.this.get(row,col);
            }
        };
    }

    public Matrix invSubtr (double b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return b - Matrix.this.get(row,col);
            }
        };
    }

    public <T extends Number> Matrix invSubtr (T b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return b.doubleValue() - Matrix.this.get(row,col);
            }
        };
    }

    // Mul
    public Matrix mul (Matrix b) {
        return new Matrix (rows, b.cols) {
            @Override
            public double get(int row, int col) {
                double sum = 0;

                for (int k=0;k<b.rows;k++) {
                    sum += Matrix.this.get(row,k) * b.get(k,col);
                }

                return sum;
            }
        };
    }

    public StatMatrix mulGPU (Matrix b, Device device, double alpha, double beta) {
        if (device.isCudaCapable()) {
            return mulCUDA(b, alpha, beta);
        }

        return mulCL(b, device, (float)alpha, (float)beta);
    }

    public StatMatrix mulGPU (Matrix b, double alpha, double beta) {
        if (Device.isCudaAvailable) {
            return mulCUDA(b, alpha, beta);
        }

        return mulCL(b, Device.def, (float)alpha, (float)beta);
    }

    public StatMatrix mulGPU (Matrix b, Device device) {
        return mulGPU(b, device, 1, 0);
    }

    public StatMatrix mulGPU (Matrix b) {
        if (Device.isCudaAvailable) {
            return mulCUDA(b, 1, 0);
        }

        return mulCL(b, Device.def, 1, 0);
    }

    public StatMatrix mulCL(Matrix b, Device device, float alpha, float beta) {
        CLBlast.setExceptionsEnabled(true);

        Context ctx = device.getContext();

        Context.Memory A = ctx.allocateInput(this.toVector().toFloatArray(), false, true);
        Context.Memory B = ctx.allocateInput(b.toVector().toFloatArray(), false, true);
        Context.Memory C = ctx.allocateOutput(Sizeof.cl_float * rows * b.cols, true);

        cl_event event = new cl_event();
        CLBlastSgemm(CLBlastLayoutRowMajor, CLBlastTransposeNo, CLBlastTransposeNo, rows, b.cols, cols, alpha, A.id, 0, cols, B.id, 0, b.cols, beta, C.id, 0, b.cols, ctx.queue, event);

        clWaitForEvents(1, new cl_event[]{ event });
        clReleaseEvent(event);

        float[] r = new float[rows * b.cols];
        clEnqueueReadBuffer(ctx.queue, C.id, CL_TRUE, 0, C.size, Pointer.to(r), 0, null, null);

        ctx.release();
        CLBlastClearCache();

        return new StatVector(r).toMatrix(b.cols).toStatic();
    }

    public StatMatrix mulCUDA (Matrix b, double alpha, double beta) {
        JCublas2.setExceptionsEnabled(true);

        double[] A = toVector().toArray();
        double[] B = b.toVector().toArray();
        double[] C = new double[rows * b.cols];

        cublasHandle handle = new cublasHandle();
        cublasCreate(handle);

        jcuda.Pointer dA = new jcuda.Pointer();
        jcuda.Pointer dB = new jcuda.Pointer();
        jcuda.Pointer dC = new jcuda.Pointer();

        cudaMalloc(dA, A.length * jcuda.Sizeof.DOUBLE);
        cudaMalloc(dB, B.length * jcuda.Sizeof.DOUBLE);
        cudaMalloc(dC, C.length * jcuda.Sizeof.DOUBLE);

        cudaMemcpy(dA, jcuda.Pointer.to(A), jcuda.Sizeof.DOUBLE * A.length, cudaMemcpyKind.cudaMemcpyHostToDevice);
        cudaMemcpy(dB, jcuda.Pointer.to(B), jcuda.Sizeof.DOUBLE * B.length, cudaMemcpyKind.cudaMemcpyHostToDevice);

        jcuda.Pointer pAlpha = jcuda.Pointer.to(new double[]{ alpha });
        jcuda.Pointer pBeta = jcuda.Pointer.to(new double[]{ beta });

        cublasSgemm(handle, CUBLAS_OP_N, CUBLAS_OP_N, rows, b.cols, cols, pAlpha, dA, rows, dB, cols, pBeta, dC, rows);
        cudaDeviceSynchronize();

        cudaMemcpy(jcuda.Pointer.to(C), dC, jcuda.Sizeof.DOUBLE * C.length, cudaMemcpyKind.cudaMemcpyDeviceToHost);

        cudaFree(dA);
        cudaFree(dB);
        cudaFree(dC);
        cublasDestroy(handle);

        return new StatVector(C).toMatrix(b.cols).toStatic();
    }

    public Matrix smartMul (Matrix b, float alpha, float beta) {
        int size = rows * cols + (b.rows * b.cols);
        if (size > 30000) {
            return mulGPU(b, alpha, beta);
        } else {
            return toStatic().mul(b.toStatic()).scalarMul(alpha).sum(beta);
        }
    }

    public Matrix smartMul (Matrix b) {
        int size = rows * cols + (b.rows * b.cols);
        if (size > 30000) {
            return mulGPU(b, 1, 0);
        } else {
            return toStatic().mul(b.toStatic());
        }
    }

    // Scalar mul
    public Matrix scalarMul (Matrix b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) * b.get(row, col);
            }
        };
    }

    public Matrix scalarMul (Vector b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) * b.get(col);
            }
        };
    }

    public Matrix scalarMul (double b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) * b;
            }
        };
    }

    public <T extends Number> Matrix scalarMul (T b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) * b.doubleValue();
            }
        };
    }

    // Div
    public Matrix div (Matrix b) {
        return mul(b.inverted());
    }

    // Scalar div
    public Matrix scalarDiv (Matrix b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) / b.get(row, col);
            }
        };
    }

    public Matrix scalarDiv (Vector b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) / b.get(col);
            }
        };
    }

    public Matrix scalarDiv (double b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) / b;
            }
        };
    }

    public <T extends Number> Matrix scalarDiv (T b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) / b.doubleValue();
            }
        };
    }

    // Scalar inv div
    public Matrix scalarInvDiv (Vector b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return b.get(col) / Matrix.this.get(row,col);
            }
        };
    }

    public Matrix scalarInvDiv (double b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return b / Matrix.this.get(row,col);
            }
        };
    }

    public <T extends Number> Matrix scalarInvDiv (T b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return b.doubleValue() / Matrix.this.get(row,col);
            }
        };
    }

    // Pow
    public Matrix pow (int b) {
        Matrix r = this;
        for (int i=1;i<b;i++) {
            r = r.mul(r);
        }

        return r;
    }

    public Matrix scalarPow(double b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Math.pow(Matrix.this.get(row, col), b);
            }
        };
    }

    // Exp
    public Matrix exp () {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Math.exp(Matrix.this.get(row, col));
            }
        };
    }

    // ln
    public Matrix ln () {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Math.log(Matrix.this.get(row, col));
            }
        };
    }

    // Tanh
    public Matrix tanh () {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Math.tanh(Matrix.this.get(row, col));
            }
        };
    }

    // Abs
    public Matrix abs () {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Math.abs(Matrix.this.get(row, col));
            }
        };
    }

    // Round
    public Matrix round () {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Math.round(Matrix.this.get(row, col));
            }
        };
    }

    public Matrix round (int to) {
        double k = Math.pow(10,to);

        return scalarMul(k).round().scalarDiv(k);
    }

    // Clamp
    public Matrix clamp (double min, double max) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                double v = Matrix.this.get(row, col);
                return Math.max(min, Math.min(v, max));
            }
        };
    }

    public Matrix clamp (double value, boolean isMax) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                double v = Matrix.this.get(row, col);
                return isMax ? Math.min(v,value) : Math.max(v,value);
            }
        };
    }

    // Applying function
    public Matrix applying (Function<Double, Double> func) {
        return new Matrix (rows, cols) {
            @Override
            public double get(int row, int col) {
                return func.apply(Matrix.this.get(row, col));
            }
        };
    }

    public Matrix applyingVectors (Function<Vector, Vector> func) {
        Vector[] vectors = new Vector[rows];
        for (int i=0;i<rows;i++) {
            vectors[i] = func.apply(get(i));
        }

        return new Matrix (rows, cols) {
            @Override
            public double get(int row, int col) {
                return vectors[row].get(col);
            }
        };
    }

    // Transposed
    public Matrix transposed () {
        return new Matrix(cols, rows) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(col, row);
            }
        };
    }

    // Determinant
    public double determinant () {
        if (cols == 1) {
            return get(0, 0);
        } else if (cols == 2) {
            return get(0,0) * get(1, 1) - get(0, 1) * get(1, 0);
        }

        double result = 0;

        for (int i=0;i<cols;i++) {
            int k = i;
            Matrix matrix = new Matrix(rows-1, cols-1) {
                @Override
                public double get(int row, int col) {
                    return Matrix.this.get(row + 1, col < k ? col : col + 1);
                }
            };

            double mul = get(0, i);
            if (i%2 == 1) {
                mul = -mul;
            }

            result += mul * matrix.determinant();
        }

        return result;
    }

    // Inverted
    public Matrix inverted () {
        return scalarMul(1 / determinant());
    }

    // Identity
    public Matrix identity () {
        return new Matrix (rows, cols) {
            @Override
            public double get(int row, int col) {
                if (row == col) {
                    return 1;
                }

                return 0;
            }
        };
    }

    // Self sum
    public Vector getRowSum () {
        return new Vector (rows) {
            @Override
            public double get(int pos) {
                return Matrix.this.get(pos).getSum();
            }
        };
    }

    public double getSum () {
        return getRowSum().getSum();
    }

    // Mean
    public Vector getRowMean () {
        return new Vector (rows) {
            @Override
            public double get(int pos) {
                return Matrix.this.get(pos).getMean();
            }
        };
    }

    public double getMean () {
        return getSum() / (rows * cols);
    }

    // Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Matrix)) {
            return false;
        }

        Matrix mo = (Matrix)o;
        if (rows != mo.rows || cols != mo.cols) {
            return false;
        }

        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                if (get(i,j) != mo.get(i,j)) {
                    return false;
                }
            }
        }

        return true;
    }

    // String
    @Override
    public String toString() {
        String r = "("+rows+", "+cols+") { ";
        for (int i=0;i<rows;i++) {
            if (i > 0){
                r += ", ";
            }
            r += "{ ";

            for (int j=0;j<cols;j++) {
                if (j > 0){
                    r += ", ";
                }
                r += get(i,j);
            }

            r += " }";
        }

        return r + " }";
    }

    public ByteBuffer getByteBuffer() {
        byte[] vector = this.toVector().getByteBuffer().array();
        ByteBuffer bb = ByteBuffer.allocate(4 + vector.length);

        bb.putInt(0,cols);
        bb.put(4, vector);
        return bb;
    }

    public byte[] getBytes () {
        return getByteBuffer().array();
    }
}
