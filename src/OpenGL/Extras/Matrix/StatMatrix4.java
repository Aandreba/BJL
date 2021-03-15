package OpenGL.Extras.Matrix;

import Matrix.Matrix;
import Matrix.StatMatrix;

public class StatMatrix4 extends StatMatrix {
    public StatMatrix4 () {
        super (4, 4);
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
