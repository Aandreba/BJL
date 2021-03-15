package OpenGL.Extras.Vector;

import Vector.Vector;

public abstract class Vector3 extends Vector {
    public Vector3 () {
        super(3);
    }

    public double x () {
        return get(0);
    }

    public double xf () {
        return getFloat(0);
    }

    public double y () {
        return get(1);
    }

    public double yf () {
        return getFloat(1);
    }

    public double z () {
        return get(2);
    }

    public double zf () {
        return getFloat(2);
    }

    @Override
    public StatVector3 toStatic() {
        return new StatVector3(get(0), get(1), get(2));
    }
}
