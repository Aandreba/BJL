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

    public double x () {
        return get(0);
    }

    public float xf () {
        return getFloat(0);
    }

    public double y () {
        return get(1);
    }

    public float yf () {
        return getFloat(1);
    }

    // Vector2
    public Vector2 sum (Vector2 b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos) + b.get(pos);
            }
        };
    }

    public Vector2 sum (double b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos) + b;
            }
        };
    }

    public Vector2 subtr (Vector2 b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos) - b.get(pos);
            }
        };
    }

    public Vector2 subtr (double b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos) - b;
            }
        };
    }

    public Vector2 mul (Vector2 b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos) * b.get(pos);
            }
        };
    }

    public Vector2 mul (double b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos) * b;
            }
        };
    }

    public Vector2 div (Vector2 b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos) / b.get(pos);
            }
        };
    }

    public Vector2 div (double b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos) / b;
            }
        };
    }

    // StatVector2
    public Vector2 sum (StatVector2 b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos) + b.get(pos);
            }
        };
    }

    public Vector2 subtr (StatVector2 b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos) - b.get(pos);
            }
        };
    }

    public Vector2 mul (StatVector2 b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos) * b.get(pos);
            }
        };
    }

    public Vector2 div (StatVector2 b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return StatVector2.this.get(pos) / b.get(pos);
            }
        };
    }

    // Pow
    public Vector2 pow (double b) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return Math.pow(StatVector2.this.get(pos), b);
            }
        };
    }

    @Override
    public Vector2 abs() {
        return new Vector2() {
            @Override
            public double get (int pos) {
                return Math.abs(StatVector2.this.get(pos));
            }
        };
    }

    public Vector2 forEachValue(ValueFunction function) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return function.apply(StatVector2.this.get(pos));
            }
        };
    }

    public Vector2 forEachIndex(IndexFunction function) {
        return new Vector2 () {
            @Override
            public double get(int pos) {
                return function.apply(pos);
            }
        };
    }

    @Override
    public Vector2 getNormalized() {
        return div(getSqrtMagnitude());
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
