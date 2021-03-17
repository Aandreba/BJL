package OpenGL.Extras.Move;

import OpenGL.Extras.Vector.Vector3;
import OpenGL.Transform;
import OpenGL.Window;
import Units.Time;

public abstract class Movement {
    final public Window window;
    final public Transform transform;

    public Movement(Window window, Transform transform) {
        this.window = window;
        this.transform = transform;
    }

    public Movement(Window window) {
        this (window, window.mainCamera);
    }

    public abstract Vector3 movementTranslate(Time delta);
    public abstract void rotate (Time delta);

    public void update (Time delta) {
        rotate(delta);
        transform.translate(movementTranslate(delta));
    }
}
