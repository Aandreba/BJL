package OpenGL;
import Matrix.StatMatrix;
import OpenGL.Extras.Matrix4;
import Units.Angle;

public class Camera {
    public Window window;
    private Angle fov;
    private float zNear, zFar;
    public Matrix4 projection;

    public Camera (Window window, Angle fov, float zNear, float zFar) {
        this.window = window;
        this.fov = fov;
        this.zNear = zNear;
        this.zFar = zFar;
        changeProjection();
    }

    private void changeProjection () {
        double fov2 = this.fov.getValue() / 2;
        double tanFov2 = 1 / Math.tan(fov2);
        float aspect = window.getWidth() * 1f / window.getHeight();
        float zm = zFar - zNear;
        float zp = zFar + zNear;

        this.projection = new Matrix4();
        this.projection.set(0, 0, tanFov2 / aspect);
        this.projection.set(1, 1, tanFov2);
        this.projection.set(2, 2, -zp / zm);
        this.projection.set(3, 2, -1);
        this.projection.set(2, 3, -(2 * zFar * zNear) / zm);
    }
}
