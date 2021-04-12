package OpenGL.Extras.Matrix;

import Matrix.Matrix;

public abstract class Matrix4 extends Matrix {
    final public static StatMatrix4 identity = new StatMatrix4(new double[][]{ {1,0,0,0}, {0,1,0,0}, {0,0,1,0}, {0,0,0,1} });

    public Matrix4 () {
        super(4,4);
    }

    public Matrix4 sum (Matrix4 b) {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                return Matrix4.this.get(row, col) + b.get(row, col);
            }
        };
    }

    public Matrix4 sum (StatMatrix4 b) {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                return Matrix4.this.get(row, col) + b.get(row, col);
            }
        };
    }

    public Matrix4 subtr (Matrix4 b) {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                return Matrix4.this.get(row, col) - b.get(row, col);
            }
        };
    }

    public Matrix4 subtr (StatMatrix4 b) {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                return Matrix4.this.get(row, col) - b.get(row, col);
            }
        };
    }

    public Matrix4 mul (Matrix4 b) {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                double sum = 0;

                for (int k=0;k<b.rows;k++) {
                    sum += Matrix4.this.get(row,k) * b.get(k,col);
                }

                return sum;
            }
        };
    }

    public Matrix4 mul (StatMatrix4 b) {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                double sum = 0;

                for (int k=0;k<4;k++) {
                    sum += Matrix4.this.get(row,k) * b.get(k,col);
                }

                return sum;
            }
        };
    }

    @Override
    public Matrix4 scalarMul (double b) {
        return new Matrix4 () {
            @Override
            public double get(int row, int col) {
                return Matrix4.this.get(row,col) * b;
            }
        };
    }

    @Override
    public <T extends Number> Matrix4 scalarMul (T b) {
        return new Matrix4 () {
            @Override
            public double get(int row, int col) {
                return Matrix4.this.get(row,col) * b.doubleValue();
            }
        };
    }

    public Matrix4 transposed () {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                return Matrix4.this.get(col, row);
            }
        };
    }

    // Identity
    @Override
    public StatMatrix4 identity () {
        return identity;
    }

    // Determinnat
    @Override
    public double determinant() {
        double result = 0;

        for (int i=0;i<4;i++) {
            int k = i;
            Matrix3 matrix = new Matrix3() {
                @Override
                public double get(int row, int col) {
                    return Matrix4.this.get(row + 1, col < k ? col : col + 1);
                }
            };

            double mul = get(0, i);
            if (i == 1 || i == 3) {
                mul = -mul;
            }

            result += mul * matrix.determinant();
        }

        return result;
    }

    // Inverted
    public Matrix4 inverted () {
        return scalarMul(1 / determinant());
    }

    @Override
    public StatMatrix4 toStatic() {
        StatMatrix4 ret = new StatMatrix4();
        for (int i=0;i<4;i++) {
            for (int j=0;j<4;j++) {
                ret.set(i, j, get(i, j));
            }
        }

        return ret;
    }

    public Matrix3 toMatrix3 () {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return Matrix4.this.get(row, col);
            }
        };
    }
}
