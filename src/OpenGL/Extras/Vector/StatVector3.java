package OpenGL.Extras.Vector;

import OpenGL.Extras.Matrix.Matrix4;
import OpenGL.Extras.Matrix.StatMatrix4;
import Vector.StatVector;

public class StatVector3 extends StatVector {
    final public static StatVector3 forward = new StatVector3(0, 0, 1);
    final public static StatVector3 backward = new StatVector3(0, 0, -1);
    final public static StatVector3 right = new StatVector3(1, 0, 0);
    final public static StatVector3 left = new StatVector3(-1, 0, 0);
    final public static StatVector3 up = new StatVector3(0, 1, 0);
    final public static StatVector3 down = new StatVector3(0, -1, 0);
    final public static StatVector3 zero = new StatVector3(0, 0, 0);

    public StatVector3 () {
        super(3);
    }

    public StatVector3 (double x, double y, double z) {
        super(3);
        set(0, x);
        set(1, y);
        set(2, z);
    }

    public Vector3 toRelative () {
        return new Vector3() {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos);
            }
        };
    }

    public void addX (double x) {
        set(0, get(0) + x);
    }

    public void addY (double y) {
        set(1, get(1) + y);
    }

    public void addZ (double z) {
        set(2, get(2) + z);
    }

    @Override
    public StatVector3 toStatic() {
        StatVector3 ret = new StatVector3();

        for (int i=0;i<3;i++) {
            ret.set(i, get(i));
        }

        return ret;
    }
}
