package OpenGL.Extras.Vector;

import Vector.StatVector;

public class StatVector2 extends StatVector {
    final public static StatVector2 right = new StatVector2(1, 0);
    final public static StatVector2 left = new StatVector2(-1, 0);
    final public static StatVector2 up = new StatVector2(0, 1);
    final public static StatVector2 down = new StatVector2(0, -1);
    final public static StatVector2 zero = new StatVector2(0, 0);

    public StatVector2() {
        super(2);
    }

    public StatVector2(double x, double y) {
        super(2);
        set(0, x);
        set(1, y);
    }

    public Vector2 toRelative () {
        return new Vector2() {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos);
            }
        };
    }

    public void addX (double x) {
        set(0, get(0) + x);
    }

    public void addY (double y) {
        set(1, get(1) + y);
    }

    @Override
    public StatVector2 toStatic() {
        StatVector2 ret = new StatVector2();

        for (int i=0;i<3;i++) {
            ret.set(i, get(i));
        }

        return ret;
    }
}
