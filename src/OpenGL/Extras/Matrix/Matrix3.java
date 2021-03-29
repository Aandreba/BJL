package OpenGL.Extras.Matrix;

import Matrix.Matrix;

public abstract class Matrix3 extends Matrix {
    final public static StatMatrix3 identity = new StatMatrix3(new double[][]{ {1,0,0}, {0,1,0}, {0,0,1} });

    public Matrix3 () {
        super(3, 3);
    }

    public Matrix3 sum (Matrix3 b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return Matrix3.this.get(row, col) + b.get(row, col);
            }
        };
    }

    public Matrix3 sum (StatMatrix3 b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return Matrix3.this.get(row, col) + b.get(row, col);
            }
        };
    }

    public Matrix3 subtr (Matrix3 b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return Matrix3.this.get(row, col) - b.get(row, col);
            }
        };
    }

    public Matrix3 subtr (StatMatrix3 b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return Matrix3.this.get(row, col) - b.get(row, col);
            }
        };
    }

    public Matrix3 mul (Matrix3 b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                double sum = 0;

                for (int k=0;k<b.rows;k++) {
                    sum += Matrix3.this.get(row,k) * b.get(k,col);
                }

                return sum;
            }
        };
    }

    public Matrix3 mul (StatMatrix3 b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                double sum = 0;

                for (int k=0;k<4;k++) {
                    sum += Matrix3.this.get(row,k) * b.get(k,col);
                }

                return sum;
            }
        };
    }

    @Override
    public Matrix3 scalarMul (double b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return Matrix3.this.get(row,col) * b;
            }
        };
    }

    @Override
    public <T extends Number> Matrix3 scalarMul (T b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return Matrix3.this.get(row,col) * b.doubleValue();
            }
        };
    }

    public Matrix3 transposed () {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return Matrix3.this.get(col, row);
            }
        };
    }

    // Identity
    @Override
    public StatMatrix3 identity () {
        return identity;
    }

    // Determinant
    @Override
    public double determinant() {
        double res = get(0,0) * (get(1, 1) * get(2, 2) - get(1, 2) * get(2, 1));
        res -= get(0, 1) * (get(1, 0) * get(2, 2) - get(1, 2) * get(2, 0));
        res += get(0, 2) * (get(1, 0) * get(2, 1) - get(1, 1) * get(2,0));

        return res;
    }

    // Inverted
    public Matrix3 inverted () {
        return scalarMul(1 / determinant());
    }

    @Override
    public StatMatrix3 toStatic() {
        StatMatrix3 ret = new StatMatrix3();
        for (int i=0;i<4;i++) {
            for (int j=0;j<4;j++) {
                ret.set(i, j, get(i, j));
            }
        }

        return ret;
    }
}
