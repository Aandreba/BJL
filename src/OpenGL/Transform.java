package OpenGL;

import OpenGL.Extras.Matrix.Matrix4;
import OpenGL.Extras.Vector.StatVector3;
import Units.Angle;

public class Transform {
    final public StatVector3 position, scale, rotation;
    protected Matrix4 translationMatrix, rotationMatrix, scaleMatrix;

    public Transform () {
        super();

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

        Matrix4 rotationXMatrix = new Matrix4() {
            @Override
            public double get(int row, int col) {
                if ((row == 0 && col == 0) || (row == 3 && col == 3)) {
                    return 1;
                } else if ((row == 1 && col == 1) || (row == 2 && col == 2)) {
                    return Math.cos(rotation.get(0));
                } else if (row == 2 && col == 1) {
                    return Math.sin(rotation.get(0));
                } else if (row == 1 && col == 2) {
                    return -Math.sin(rotation.get(0));
                }

                return 0;
            }
        };

        Matrix4 rotationYMatrix = new Matrix4() {
            @Override
            public double get(int row, int col) {
                if ((row == 1 && col == 1) || (row == 3 && col == 3)) {
                    return 1;
                } else if ((row == 0 && col == 0) || (row == 2 && col == 2)) {
                    return Math.cos(rotation.get(1));
                } else if (row == 0 && col == 2) {
                    return Math.sin(rotation.get(1));
                } else if (row == 2 && col == 0) {
                    return -Math.sin(rotation.get(1));
                }

                return 0;
            }
        };

        Matrix4 rotationZMatrix = new Matrix4() {
            @Override
            public double get(int row, int col) {
                if ((row == 2 && col == 2) || (row == 3 && col == 3)) {
                    return 1;
                } else if ((row == 0 && col == 0) || (row == 1 && col == 1)) {
                    return Math.cos(rotation.get(2));
                } else if (row == 0 && col == 1) {
                    return Math.sin(rotation.get(2));
                } else if (row == 1 && col == 0) {
                    return -Math.sin(rotation.get(2));
                }

                return 0;
            }
        };

        this.rotationMatrix = rotationXMatrix.mul(rotationYMatrix).mul(rotationZMatrix);
    }

    public void setScale (float scale) {
        this.scale.set(0, scale);
        this.scale.set(1, scale);
        this.scale.set(2, scale);
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

    public Matrix4 getMatrix () {
        return translationMatrix.mul(rotationMatrix).mul(scaleMatrix);
    }
}
