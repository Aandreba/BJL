package OpenGL.Extras.Vector;

import OpenGL.Extras.Matrix.Matrix4;
import OpenGL.Extras.Matrix.StatMatrix4;
import Vector.StatVector;
import Vector.Vector;

public class StatVector3 extends StatVector {
    final public static StatVector3 forward = new StatVector3(0, 0, 1);
    final public static StatVector3 backward = new StatVector3(0, 0, -1);
    final public static StatVector3 right = new StatVector3(1, 0, 0);
    final public static StatVector3 left = new StatVector3(-1, 0, 0);
    final public static StatVector3 up = new StatVector3(0, 1, 0);
    final public static StatVector3 down = new StatVector3(0, -1, 0);
    final public static StatVector3 zero = new StatVector3(0, 0, 0);
    final public static StatVector3 one = new StatVector3(1, 1, 1);

    public StatVector3 () {
        super(3);
    }

    public StatVector3 (Vector2 v, float z) {
        super(3);
        set(0, v.x());
        set(1, v.y());
        set(2, z);
    }

    public StatVector3 (double x, double y, double z) {
        super(3);
        set(0, x);
        set(1, y);
        set(2, z);
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

    public double z () {
        return get(2);
    }

    public float zf () {
        return getFloat(2);
    }

    public Vector2 xy () {
        return new Vector2() {
            @Override
            public double get(int pos) {
                if (pos == 0) {
                    return StatVector3.this.x();
                }
                return StatVector3.this.y();
            }
        };
    }

    public Vector2 zy () {
        return new Vector2() {
            @Override
            public double get(int pos) {
                if (pos == 0) {
                    return StatVector3.this.z();
                }
                return StatVector3.this.y();
            }
        };
    }

    public Vector2 xz () {
        return new Vector2() {
            @Override
            public double get(int pos) {
                if (pos == 0) {
                    return StatVector3.this.x();
                }
                return StatVector3.this.z();
            }
        };
    }

    // Vector3
    public Vector3 sum (Vector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos) + b.get(pos);
            }
        };
    }

    public Vector3 sum (double b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos) + b;
            }
        };
    }

    public Vector3 subtr (Vector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos) - b.get(pos);
            }
        };
    }

    public Vector3 subtr (double b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos) - b;
            }
        };
    }

    public Vector3 mul (Vector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos) * b.get(pos);
            }
        };
    }

    public Vector3 mul (double b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos) * b;
            }
        };
    }

    public Vector3 div (Vector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos) / b.get(pos);
            }
        };
    }

    public Vector3 div (double b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos) / b;
            }
        };
    }

    public Vector3 invDiv (Vector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return b.get(pos) / StatVector3.this.get(pos);
            }
        };
    }

    public Vector3 invDiv (double b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return b / StatVector3.this.get(pos);
            }
        };
    }

    // StatVector3
    public Vector3 sum (StatVector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos) + b.get(pos);
            }
        };
    }

    public Vector3 subtr (StatVector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos) - b.get(pos);
            }
        };
    }

    public Vector3 mul (StatVector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos) * b.get(pos);
            }
        };
    }

    public Vector3 div (StatVector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return StatVector3.this.get(pos) / b.get(pos);
            }
        };
    }

    public Vector3 invDiv (StatVector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return b.get(pos) / StatVector3.this.get(pos);
            }
        };
    }

    // Pow
    public Vector3 pow (double b) {
        return new Vector3() {
            @Override
            public double get(int pos) {
                return Math.pow(StatVector3.this.get(pos), b);
            }
        };
    }

    @Override
    public Vector3 abs() {
        return new Vector3() {
            @Override
            public double get (int pos) {
                return Math.abs(StatVector3.this.get(pos));
            }
        };
    }

    public Vector3 getNormalized () {
        double sqrt = getSqrtMagnitude();
        if (Double.isNaN(sqrt) || sqrt == 0) {
            return new Vector3 () {
                @Override
                public double get(int pos) {
                    return 0;
                }
            };
        } else {
            return div(sqrt);
        }
    }

    // New
    public Vector3 cross (Vector3 b) {
        return new Vector3() {
            @Override
            public double get(int pos) {
                switch (pos) {
                    case 0:
                        return (StatVector3.this.get(1) * b.get(2)) - (StatVector3.this.get(2) * b.get(1));
                    case 1:
                        return (StatVector3.this.get(0) * b.get(2)) - (StatVector3.this.get(2) * b.get(0));
                    default:
                        return (StatVector3.this.get(0) * b.get(1)) - (StatVector3.this.get(1) * b.get(0));
                }
            }
        };
    }

    public Vector3 cross (StatVector3 b) {
        return new Vector3() {
            @Override
            public double get(int pos) {
                switch (pos) {
                    case 0:
                        return (StatVector3.this.get(1) * b.get(2)) - (StatVector3.this.get(2) * b.get(1));
                    case 1:
                        return (StatVector3.this.get(0) * b.get(2)) - (StatVector3.this.get(2) * b.get(0));
                    default:
                        return (StatVector3.this.get(0) * b.get(1)) - (StatVector3.this.get(1) * b.get(0));
                }
            }
        };
    }

    // Round
    public Vector3 round () {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Math.round(StatVector3.this.get(pos));
            }
        };
    }

    public Vector3 round (int to) {
        double k = Math.pow(10,to);
        return this.mul(k).round().div(k);
    }

    public Vector3 forEachValue(ValueFunction function) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return function.apply(StatVector3.this.get(pos));
            }
        };
    }

    public Vector3 forEachIndex(IndexFunction function) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return function.apply(pos);
            }
        };
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

    public void add (Vector3 v) {
        addX(v.x());
        addY(v.y());
        addZ(v.z());
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
