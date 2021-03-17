package OpenGL.Extras.Vector;

import Vector.Vector;

public abstract class Vector2 extends Vector {
    public Vector2() {
        super(2);
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

    public Vector2 sum (Vector2 b) {
        return new Vector2() {
            @Override
            public double get(int pos) {
                return Vector2.this.get(pos) + b.get(pos);
            }
        };
    }

    public Vector2 sum (double b) {
        return new Vector2() {
            @Override
            public double get(int pos) {
                return Vector2.this.get(pos) + b;
            }
        };
    }

    public Vector2 subtr (Vector2 b) {
        return new Vector2() {
            @Override
            public double get(int pos) {
                return Vector2.this.get(pos) - b.get(pos);
            }
        };
    }

    public Vector2 subtr (double b) {
        return new Vector2() {
            @Override
            public double get(int pos) {
                return Vector2.this.get(pos) - b;
            }
        };
    }

    public Vector2 mul (Vector2 b) {
        return new Vector2() {
            @Override
            public double get(int pos) {
                return Vector2.this.get(pos) * b.get(pos);
            }
        };
    }

    public Vector2 mul (double b) {
        return new Vector2() {
            @Override
            public double get(int pos) {
                return Vector2.this.get(pos) * b;
            }
        };
    }

    public Vector2 div (Vector2 b) {
        return new Vector2() {
            @Override
            public double get(int pos) {
                return Vector2.this.get(pos) / b.get(pos);
            }
        };
    }

    public Vector2 div (double b) {
        return new Vector2() {
            @Override
            public double get(int pos) {
                return Vector2.this.get(pos) / b;
            }
        };
    }

    @Override
    public StatVector2 toStatic() {
        return new StatVector2(get(0), get(1));
    }
}
