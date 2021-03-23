package OpenGL.Extras.Matrix;

import Matrix.Matrix;
import Matrix.StatMatrix;

public class StatMatrix4 extends StatMatrix {
    public StatMatrix4 () {
        super (4, 4);
    }

    public StatMatrix4 (double[][] vals) {
        super (4, 4);
        this.values = vals;
    }

    public Matrix4 sum (Matrix4 b) {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                return StatMatrix4.this.get(row, col) + b.get(row, col);
            }
        };
    }

    public Matrix4 sum (StatMatrix4 b) {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                return StatMatrix4.this.get(row, col) + b.get(row, col);
            }
        };
    }

    public Matrix4 subtr (Matrix4 b) {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                return StatMatrix4.this.get(row, col) - b.get(row, col);
            }
        };
    }

    public Matrix4 subtr (StatMatrix4 b) {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                return StatMatrix4.this.get(row, col) - b.get(row, col);
            }
        };
    }

    public Matrix4 mul (Matrix4 b) {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                double sum = 0;

                for (int k=0;k<4;k++) {
                    sum += StatMatrix4.this.get(row,k) * b.get(k,col);
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
                    sum += StatMatrix4.this.get(row,k) * b.get(k,col);
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
                return StatMatrix4.this.get(row,col) * b;
            }
        };
    }

    @Override
    public <T extends Number> Matrix4 scalarMul (T b) {
        return new Matrix4 () {
            @Override
            public double get(int row, int col) {
                return StatMatrix4.this.get(row,col) * b.doubleValue();
            }
        };
    }

    public Matrix4 toRelative () {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                return StatMatrix4.this.get(row, col);
            }
        };
    }

    @Override
    public StatMatrix4 clone() {
        StatMatrix4 ret = new StatMatrix4();

        for (int i=0;i<4;i++) {
            for (int j=0;j<4;j++) {
                ret.set(i,j, get(i,j));
            }
        }

        return ret;
    }
}
