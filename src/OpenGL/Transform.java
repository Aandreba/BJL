package OpenGL;

import OpenGL.Extras.Matrix.Matrix4;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import Units.Angle;

public class Transform {
    final public StatVector3 position, scale, rotation;
    protected Matrix4 translationMatrix, rotationXMatrix, rotationYMatrix, rotationZMatrix, rotationMatrix, scaleMatrix, matrix;

    public Transform () {
        this.position = new StatVector3();
        this.scale = new StatVector3(1, 1, 1);
        this.rotation = new StatVector3();

        this.translationMatrix = new Matrix4() {
            @Override
            public double get(int row, int col) {
                if (row == col) {
                    return 1;
                } else if (col == 3) {
                    return position.get(row);
                }

                return 0;
            }
        };

        this.scaleMatrix = new Matrix4() {
            @Override
            public double get(int row, int col) {
                if (row < 3 && row == col) {
                    return scale.get(row);
                } else if (row == 3 && col == 3) {
                    return 1;
                }

                return 0;
            }
        };

        this.rotationXMatrix = new Matrix4() {
            @Override
            public double get(int row, int col) {
                if ((row == 0 && col == 0) || (row == 3 && col == 3)) {
                    return 1;
                } else if ((row == 1 && col == 1) || (row == 2 && col == 2)) {
                    return Math.cos(-rotation.get(0));
                } else if (row == 2 && col == 1) {
                    return Math.sin(-rotation.get(0));
                } else if (row == 1 && col == 2) {
                    return -Math.sin(-rotation.get(0));
                }

                return 0;
            }
        };

        this.rotationYMatrix = new Matrix4() {
            @Override
            public double get(int row, int col) {
                if ((row == 1 && col == 1) || (row == 3 && col == 3)) {
                    return 1;
                } else if ((row == 0 && col == 0) || (row == 2 && col == 2)) {
                    return Math.cos(-rotation.get(1));
                } else if (row == 0 && col == 2) {
                    return Math.sin(-rotation.get(1));
                } else if (row == 2 && col == 0) {
                    return -Math.sin(-rotation.get(1));
                }

                return 0;
            }
        };

        this.rotationZMatrix = new Matrix4() {
            @Override
            public double get(int row, int col) {
                if ((row == 2 && col == 2) || (row == 3 && col == 3)) {
                    return 1;
                } else if ((row == 0 && col == 0) || (row == 1 && col == 1)) {
                    return Math.cos(-rotation.get(2));
                } else if (row == 0 && col == 1) {
                    return Math.sin(-rotation.get(2));
                } else if (row == 1 && col == 0) {
                    return -Math.sin(-rotation.get(2));
                }

                return 0;
            }
        };

        this.rotationMatrix = rotationXMatrix.mul(rotationYMatrix).mul(rotationZMatrix);
        this.matrix = Matrix4.identity.mul(translationMatrix).mul(rotationMatrix).mul(scaleMatrix);
    }

    public void setPosition (double x, double y, double z) {
        this.position.set(0, x);
        this.position.set(1, y);
        this.position.set(2, z);
    }

    public void setPosition (Vector3 pos) {
        this.position.set(0, pos.get(0));
        this.position.set(1, pos.get(1));
        this.position.set(2, pos.get(2));
    }

    public void setRotation (Vector3 rot) {
        this.rotation.set(0, rot.get(0));
        this.rotation.set(1, rot.get(1));
        this.rotation.set(2, rot.get(2));
    }

    public void setRotation (double x, double y, double z) {
        this.rotation.set(0, x);
        this.rotation.set(1, y);
        this.rotation.set(2, z);
    }

    public Angle getRotationX () {
        return new Angle(this.rotation.get(0));
    }

    public void setRotationX (Angle angle) {
        this.rotation.set(0, angle.getValue());
    }

    public Angle getRotationY () {
        return new Angle(this.rotation.get(1));
    }

    public void setRotationY (Angle angle) {
        this.rotation.set(1, angle.getValue());
    }

    public Angle getRotationZ () {
        return new Angle(this.rotation.get(2));
    }

    public void setRotationZ (Angle angle) {
        this.rotation.set(2, angle.getValue());
    }

    public void setScale (float scale) {
        this.scale.set(0, scale);
        this.scale.set(1, scale);
        this.scale.set(2, scale);
    }

    public void setScale (float x, float y, float z) {
        this.scale.set(0, x);
        this.scale.set(1, y);
        this.scale.set(2, z);
    }

    public void setScale (Vector3 scale) {
        this.scale.set(0, scale.x());
        this.scale.set(1, scale.y());
        this.scale.set(2, scale.z());
    }

    public void setScale (StatVector3 scale) {
        this.scale.set(0, scale.x());
        this.scale.set(1, scale.y());
        this.scale.set(2, scale.z());
    }

    public StatVector3 translateVector (Vector3 trans) {
        float sinY = getRotationY().sinf();
        float cosY = getRotationY().cosf();

        Vector3 x = new StatVector3(cosY, 0, -sinY).mul(trans.x());
        Vector3 y = new StatVector3(0, 1, 0).mul(trans.y());
        Vector3 z = new StatVector3(sinY, 0, cosY).mul(trans.z());

        float X = x.xf() + y.xf() + z.xf();
        float Y = x.yf() + y.yf() + z.yf();
        float Z = x.zf() + y.zf() + z.zf();

        return new StatVector3(X, Y, Z);
    }

    public void translate (Vector3 trans) {
        this.position.add(translateVector(trans));
    }

    public void moveTowards (Vector3 position, float step) {
        this.position.add(translateVector(position.subtr(this.position).getNormalized().mul(step)));
    }

    public void moveTowards (Vector3 position) {
        this.position.add(translateVector(position.subtr(this.position).getNormalized()));
    }

    public void moveTowards (StatVector3 position, float step) {
        this.position.add(translateVector(position.subtr(this.position).getNormalized().mul(step)));
    }

    public void moveTowards (StatVector3 position) {
        this.position.add(translateVector(position.subtr(this.position).getNormalized()));
    }

    public static Matrix4 translationMatrixOf (Vector3 position) {
        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                if (row == col) {
                    return 1;
                } else if (col == 3) {
                    return position.get(row);
                }

                return 0;
            }
        };
    }

    @Override
    public String toString() {
        return "Transform {" +
                "position = " + position +
                ", scale = " + scale +
                ", rotation = " + rotation +
                '}';
    }
}
