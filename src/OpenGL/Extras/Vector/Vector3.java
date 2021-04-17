package OpenGL.Extras.Vector;

import Vector.Vector;

public abstract class Vector3 extends Vector {
    public Vector3 () {
        super(3);
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
                    return Vector3.this.x();
                }
                return Vector3.this.y();
            }
        };
    }

    public Vector2 zy () {
        return new Vector2() {
            @Override
            public double get(int pos) {
                if (pos == 0) {
                    return Vector3.this.z();
                }
                return Vector3.this.y();
            }
        };
    }

    public Vector2 xz () {
        return new Vector2() {
            @Override
            public double get(int pos) {
                if (pos == 0) {
                    return Vector3.this.x();
                }
                return Vector3.this.z();
            }
        };
    }

    // Vector3
    public Vector3 sum (Vector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Vector3.this.get(pos) + b.get(pos);
            }
        };
    }

    public Vector3 sum (double b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Vector3.this.get(pos) + b;
            }
        };
    }

    public Vector3 subtr (Vector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Vector3.this.get(pos) - b.get(pos);
            }
        };
    }

    public Vector3 subtr (double b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Vector3.this.get(pos) - b;
            }
        };
    }

    public Vector3 mul (Vector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Vector3.this.get(pos) * b.get(pos);
            }
        };
    }

    public Vector3 mul (double b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Vector3.this.get(pos) * b;
            }
        };
    }

    public Vector3 div (Vector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Vector3.this.get(pos) / b.get(pos);
            }
        };
    }

    public Vector3 div (double b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Vector3.this.get(pos) / b;
            }
        };
    }

    public Vector3 invDiv (Vector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return b.get(pos) / Vector3.this.get(pos);
            }
        };
    }

    public Vector3 invDiv (double b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return b / Vector3.this.get(pos);
            }
        };
    }

    // StatVector3
    public Vector3 sum (StatVector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Vector3.this.get(pos) + b.get(pos);
            }
        };
    }

    public Vector3 subtr (StatVector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Vector3.this.get(pos) - b.get(pos);
            }
        };
    }

    public Vector3 mul (StatVector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Vector3.this.get(pos) * b.get(pos);
            }
        };
    }

    public Vector3 div (StatVector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Vector3.this.get(pos) / b.get(pos);
            }
        };
    }

    public Vector3 invDiv (StatVector3 b) {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return b.get(pos) / Vector3.this.get(pos);
            }
        };
    }

    // Pow
    public Vector3 pow (double b) {
        return new Vector3() {
            @Override
            public double get(int pos) {
                return Math.pow(Vector3.this.get(pos), b);
            }
        };
    }

    @Override
    public Vector3 abs() {
        return new Vector3() {
            @Override
            public double get (int pos) {
                return Math.abs(Vector3.this.get(pos));
            }
        };
    }

    public Vector3 getNormalized () {
        return div(getSqrtMagnitude());
    }

    // New
    public Vector3 cross (Vector3 b) {
        return new Vector3() {
            @Override
            public double get(int pos) {
                switch (pos) {
                    case 0:
                        return (Vector3.this.get(1) * b.get(2)) - (Vector3.this.get(2) * b.get(1));
                    case 1:
                        return (Vector3.this.get(0) * b.get(2)) - (Vector3.this.get(2) * b.get(0));
                    default:
                        return (Vector3.this.get(0) * b.get(1)) - (Vector3.this.get(1) * b.get(0));
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
                        return (Vector3.this.get(1) * b.get(2)) - (Vector3.this.get(2) * b.get(1));
                    case 1:
                        return (Vector3.this.get(0) * b.get(2)) - (Vector3.this.get(2) * b.get(0));
                    default:
                        return (Vector3.this.get(0) * b.get(1)) - (Vector3.this.get(1) * b.get(0));
                }
            }
        };
    }

    // Round
    public Vector3 round () {
        return new Vector3 () {
            @Override
            public double get(int pos) {
                return Math.round(Vector3.this.get(pos));
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
                return function.apply(Vector3.this.get(pos));
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

    @Override
    public StatVector3 toStatic() {
        return new StatVector3(get(0), get(1), get(2));
    }

    public static Vector3 from (Vector vector) {
        return new Vector3() {
            @Override
            public double get(int pos) {
                return vector.get(pos);
            }
        };
    }
}
