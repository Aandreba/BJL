package Vector;

import Matrix.Matrix;
import Extras.Rand;

import java.nio.ByteBuffer;

public abstract class Vector {
    protected int length;

    public Vector (int length) {
        this.length = length;
    }

    public static Vector random (int length, double from, double to) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return Rand.getDouble(from, to);
            }
        };
    }

    public static Vector random (int length) {
        return random(length, -1, 1);
    }

    public int getLength() {
        return length;
    }

    public abstract double get (int pos);

    public double getFirst () {
        return get(0);
    }

    public double getLast () {
        return get(length - 1);
    }

    public double[] toArray () {
        double[] ret = new double[length];

        for (int i=0;i<length;i++) {
            ret[i] = get(i);
        }

        return ret;
    }

    public float[] toFloatArray () {
        float[] ret = new float[length];

        for (int i=0;i<length;i++) {
            ret[i] =(float)get(i);
        }

        return ret;
    }

    public Matrix toMatrix(int cols) {
        return new Matrix (this.length / cols, cols) {
            @Override
            public double get(int row, int col) {
                return Vector.this.get((row * cols) + col);
            }
        };
    }

    public StatVector toStatic () {
        StatVector ret = new StatVector(length);

        for (int i=0;i<length;i++) {
            ret.set(i, get(i));
        }

        return ret;
    }

    // Sums
    public Vector sum (Vector b) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return Vector.this.get(pos) + b.get(pos);
            }
        };
    }

    public Vector sum (double b) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return Vector.this.get(pos) + b;
            }
        };
    }

    public <T extends Number> Vector sum (T b) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return Vector.this.get(pos) + b.doubleValue();
            }
        };
    }

    // Subtrs
    public Vector subtr (Vector b) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return Vector.this.get(pos) - b.get(pos);
            }
        };
    }

    public Vector subtr (double b) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return Vector.this.get(pos) - b;
            }
        };
    }

    public <T extends Number> Vector subtr (T b) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return Vector.this.get(pos) - b.doubleValue();
            }
        };
    }

    // Inv subtr
    public Vector invSubtr (double b) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return b - Vector.this.get(pos);
            }
        };
    }

    public <T extends Number> Vector invSubtr (T b) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return b.doubleValue() - Vector.this.get(pos);
            }
        };
    }

    // Mul
    public Vector mul (Vector b) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return Vector.this.get(pos) * b.get(pos);
            }
        };
    }

    public Vector mul (double b) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return Vector.this.get(pos) * b;
            }
        };
    }

    public <T extends Number> Vector mul (T b) {
        return new Vector(length) {
            @Override
            public double get(int pos) {
                return Vector.this.get(pos) * b.doubleValue();
            }
        };
    }

    // Div
    public Vector div (Vector b) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return Vector.this.get(pos) / b.get(pos);
            }
        };
    }

    public Vector div (double b) {
        return new Vector (length) {
            @Override
            public double get(int pos) {
                return Vector.this.get(pos) / b;
            }
        };
    }

    public <T extends Number> Vector div (T b) {
        return new Vector(length) {
            @Override
            public double get(int pos) {
                return Vector.this.get(pos) / b.doubleValue();
            }
        };
    }

    // Pow
    public Vector pow (double b) {
        return new Vector(length) {
            @Override
            public double get(int pos) {
                return Math.pow(Vector.this.get(pos), b);
            }
        };
    }

    // Abs
    public Vector abs () {
        return new Vector(length) {
            @Override
            public double get(int pos) {
                return Math.abs(Vector.this.get(pos));
            }
        };
    }

    // Round
    public Vector round () {
        return new Vector(length) {
            @Override
            public double get(int pos) {
                return Math.round(Vector.this.get(pos));
            }
        };
    }

    public Vector round (int to) {
        double k = Math.pow(10,to);

        return this.mul(k).round().div(k);
    }

    // Max
    public double max () {
        double max = 0;

        for (int i=0;i<length;i++) {
            double v = get(i);
            if (i == 0 || v > max) {
                max = v;
            }
        }

        return max;
    }

    public int maxPos () {
        double max = 0;
        int pos = -1;

        for (int i=0;i<length;i++) {
            double v = get(i);
            if (i == 0 || v > max) {
                max = v;
                pos = 1;
            }
        }

        return pos;
    }

    public double min () {
        double min = 0;

        for (int i=0;i<length;i++) {
            double v = get(i);
            if (i == 0 || v < min) {
                min = v;
            }
        }

        return min;
    }

    public int minPos () {
        double min = 0;
        int pos = -1;

        for (int i=0;i<length;i++) {
            double v = get(i);
            if (i == 0 || v < min) {
                min = v;
                pos = i;
            }
        }

        return pos;
    }

    // Exp
    public Vector exp () {
        return new Vector(length) {
            @Override
            public double get(int pos) {
                return Math.exp(Vector.this.get(pos));
            }
        };
    }

    // Ln
    public Vector ln () {
        return new Vector(length) {
            @Override
            public double get(int pos) {
                return Math.log(Vector.this.get(pos));
            }
        };
    }

    // Clamp
    public Vector clamp (double min, double max) {
        return new Vector(length) {
            @Override
            public double get(int pos) {
                double v = Vector.this.get(pos);
                return Math.max(min, Math.min(v, max));
            }
        };
    }

    public Vector clamp (double value, boolean isMax) {
        return new Vector(length) {
            @Override
            public double get(int pos) {
                double v = Vector.this.get(pos);
                return isMax ? Math.min(v,value) : Math.max(v,value);
            }
        };
    }

    // Magnitude
    public double getMagnitude () {
        return pow(2).getSum();
    }

    public double getSqrtMagnitude () {
        return Math.sqrt(getMagnitude());
    }

    // Normalized
    public Vector getNormalized () {
        return div(getSqrtMagnitude());
    }

    // Self sum
    public double getSum () {
        double s = 0;

        for (int i=0;i<length;i++) {
            s += get(i);
        }

        return s;
    }

    // Mean
    public double getMean () {
        return getSum() / length;
    }

    // String
    @Override
    public String toString() {
        String r = "("+length+") { ";
        for (int i=0;i<length;i++) {
            if (i > 0){
                r += ", ";
            }
            r += get(i);
        }

        return r + " }";
    }

    // Bytes
    public ByteBuffer getByteBuffer() {
        ByteBuffer bb = ByteBuffer.allocate(8 * length);
        for (int i=0;i<length;i++) {
            bb = bb.putDouble(8*i, get(i));
        }

        return bb;
    }

    public byte[] getBytes() {
        return getByteBuffer().array();
    }
}
