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

    @Override
    public StatVector3 toStatic() {
        return new StatVector3(get(0), get(1), get(2));
    }
}
