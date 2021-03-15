package OpenGL.Extras.Matrix;

import Matrix.Matrix;

public abstract class Matrix4 extends Matrix {
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

    public Matrix4 subtr (Matrix4 b) {
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
}
