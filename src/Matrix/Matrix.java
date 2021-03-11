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
import java.nio.ByteBuffer;

import static jcuda.jcublas.JCublas2.*;
import static jcuda.jcublas.cublasOperation.CUBLAS_OP_N;
import static jcuda.runtime.JCuda.*;
import static org.jocl.CL.*;
import static org.jocl.blast.CLBlast.CLBlastClearCache;
import static org.jocl.blast.CLBlast.CLBlastSgemm;
import static org.jocl.blast.CLBlastLayout.CLBlastLayoutRowMajor;
import static org.jocl.blast.CLBlastTranspose.CLBlastTransposeNo;

public abstract class Matrix {
    protected int rows, cols;

    public Matrix (int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public static Matrix random (int rows, int cols, double from, double to) {
        return new Matrix (rows, cols) {
            @Override
            public double get(int row, int col) {
                return Rand.getDouble(from, to);
            }
        };
    }

    public static Matrix random (int rows, int cols) {
        return random(rows, cols, -1, 1);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public abstract double get (int row, int col);

    public Vector get (int row) {
        return new Vector(cols) {
            @Override
            public double get(int pos) {
                return Matrix.this.get(row, pos);
            }
        };
    }

    public double[][] toArray () {
        double[][] array = new double[rows][cols];

        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                array[i][j] = get(i,j);
            }
        }

        return array;
    }

    public float[][] toFloatArray () {
        float[][] array = new float[rows][cols];

        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                array[i][j] = (float)get(i,j);
            }
        }

        return array;
    }

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

    public StatMatrix toStatic () {
        StatMatrix ret = new StatMatrix(rows, cols);
        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                ret.values[i][j] = get(i,j);
            }
        }

        return ret;
    }

    // Sums
    public Matrix sum (Matrix b) {
        return new Matrix(rows, cols) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(row,col) + b.get(row, col);
            }
        };
    }

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
            return mulCUDA(b, device, alpha, beta);
        }

        return mulCL(b, device, (float)alpha, (float)beta);
    }

    public StatMatrix mulGPU (Matrix b, double alpha, double beta) {
        if (Device.isCudaAvailable) {
            return mulCUDA(b, null, alpha, beta);
        }

        return mulCL(b, Device.def, (float)alpha, (float)beta);
    }

    public StatMatrix mulGPU (Matrix b, Device device) {
        return mulGPU(b, device, 1, 0);
    }

    public StatMatrix mulGPU (Matrix b) {
        if (Device.isCudaAvailable) {
            return mulCUDA(b, null, 1, 0);
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

    public StatMatrix mulCUDA (Matrix b, Device device, double alpha, double beta) {
        if (device != null && !device.isCudaCapable()) {
            return mulCL(b, device, (float)alpha, (float)beta);
        }

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
    public Matrix pow (double b) {
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

        return this.scalarMul(k).round().scalarMul(k);
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

    // Transposed
    public Matrix transposed () {
        return new Matrix(cols, rows) {
            @Override
            public double get(int row, int col) {
                return Matrix.this.get(col, row);
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

    public static Matrix fromImage (BufferedImage img) {
        return new Matrix (img.getHeight(), img.getWidth()) {
            @Override
            public double get(int row, int col) {
                int p = img.getRGB(row, col);

                //int a = (p>>24) & 0xff;
                int r = (p>>16) & 0xff;
                int g = (p>>8) & 0xff;
                int b = p & 0xff;

                return (r+g+b) / 3f;
            }
        };
    }

    public static Matrix fromImage (String img) throws IOException {
        return fromImage(ImageIO.read(new File(img)));
    }
}
