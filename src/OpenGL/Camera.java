package OpenGL;

import Matrix.Matrix;
import OpenGL.Extras.Matrix.Matrix4;
import OpenGL.Extras.Matrix.StatMatrix4;
import Units.Angle;

public class Camera extends Transform {
    final private StatMatrix4 proj;
    final public Matrix4 view;

    private Angle fov;
    private float zFar;
    private float zNear;

    public Camera(Angle fov, float zFar, float zNear) {
        super();

        this.fov = fov;
        this.zFar = zFar;
        this.zNear = zNear;

        float tanFov = 1 / (float) Math.tan(fov.getValue() / 2);
        float zm = zFar - zNear;
        float zp = zFar + zNear;

        this.proj = new StatMatrix4();
        this.proj.set(1, 1, tanFov);
        this.proj.set(2, 2, -zp / zm);
        this.proj.set(3, 2, -1);
        this.proj.set(2, 3, -2 * zFar * zNear / zm);

        this.view = Matrix4.identity.toRelative().mul(rotationXMatrix).mul(rotationYMatrix).mul(translationMatrixOf(position.toRelative().mul(-1)));
    }

    public Camera () {
        this(new Angle(60, Angle.Type.Degrees), 0.01f, 1000);
    }

    public Angle getFov() {
        return fov;
    }

    public void setFov (Angle fov) {
        this.fov = fov;
        this.proj.set(1, 1, 1 / (float) Math.tan(fov.getValue() / 2));
    }

    public float getZFar() {
        return zFar;
    }

    public void setZFar(float zFar) {
        this.zFar = zFar;
        zUpdate();
    }

    public float getZNear() {
        return zNear;
    }

    public void setZNear(float zNear) {
        this.zNear = zNear;
        zUpdate();
    }

    private void zUpdate () {
        float zm = zFar - zNear;
        float zp = zFar + zNear;

        this.proj.set(2, 2, -zp / zm);
        this.proj.set(2, 3, -2 * zFar * zNear / zm);
    }

    public StatMatrix4 getProjectionMatrix (Window window) {
        StatMatrix4 clone = this.proj.clone();
        clone.set(0, 0, clone.get(1, 1) / (window.getWidth() * 1f / window.getHeight()));

        return clone;
    }
}
