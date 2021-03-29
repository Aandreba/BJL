package OpenGL.Extras.Matrix;

import Matrix.Matrix;
import Matrix.StatMatrix;

public class StatMatrix3 extends StatMatrix {
    public StatMatrix3() {
        super (3, 3);
    }

    public StatMatrix3(double[][] vals) {
        super (3, 3);
        this.values = vals;
    }

    public Matrix3 sum (Matrix3 b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return StatMatrix3.this.get(row, col) + b.get(row, col);
            }
        };
    }

    public Matrix3 sum (StatMatrix3 b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return StatMatrix3.this.get(row, col) + b.get(row, col);
            }
        };
    }

    public Matrix3 subtr (Matrix3 b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return StatMatrix3.this.get(row, col) - b.get(row, col);
            }
        };
    }

    public Matrix3 subtr (StatMatrix3 b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return StatMatrix3.this.get(row, col) - b.get(row, col);
            }
        };
    }

    public Matrix3 mul (Matrix3 b) {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                double sum = 0;

                for (int k=0;k<4;k++) {
                    sum += StatMatrix3.this.get(row,k) * b.get(k,col);
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
                    sum += StatMatrix3.this.get(row,k) * b.get(k,col);
                }

                return sum;
            }
        };
    }

    @Override
    public Matrix3 scalarMul (double b) {
        return new Matrix3 () {
            @Override
            public double get(int row, int col) {
                return StatMatrix3.this.get(row,col) * b;
            }
        };
    }

    @Override
    public <T extends Number> Matrix3 scalarMul (T b) {
        return new Matrix3 () {
            @Override
            public double get(int row, int col) {
                return StatMatrix3.this.get(row,col) * b.doubleValue();
            }
        };
    }

    public Matrix3 transposed () {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return StatMatrix3.this.get(col, row);
            }
        };
    }

    @Override
    public double determinant() {
        double res = get(0,0) * (get(1, 1) * get(2, 2) - get(1, 2) * get(2, 1));
        res -= get(0, 1) * (get(1, 0) * get(2, 2) - get(1, 2) * get(2, 0));
        res += get(0, 2) * (get(1, 0) * get(2, 1) - get(1, 1) * get(2,0));

        return res;
    }

    public Matrix3 inverted () {
        return scalarMul(1 / determinant());
    }

    public Matrix3 toRelative () {
        return new Matrix3() {
            @Override
            public double get(int row, int col) {
                return StatMatrix3.this.get(row, col);
            }
        };
    }

    @Override
    public StatMatrix3 clone() {
        StatMatrix3 ret = new StatMatrix3();

        for (int i=0;i<4;i++) {
            for (int j=0;j<4;j++) {
                ret.set(i,j, get(i,j));
            }
        }

        return ret;
    }
}
