package OpenGL.Extras.Move;

import Extras.Mathf;
import OpenGL.Extras.Vector.StatVector2;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector2;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Transform;
import OpenGL.Window;
import Units.Time;
import Vector.Vector;

import java.security.Key;

public class KeyMouse extends Movement {
    public KeyCode forward, backward, right, left, up, down;
    public float speed;

    public KeyMouse(Window window, Transform transform, float speed, KeyCode forward, KeyCode backward, KeyCode right, KeyCode left, KeyCode up, KeyCode down) {
        super(window, transform);
        this.speed = speed;
        this.forward = forward;
        this.backward = backward;
        this.right = right;
        this.left = left;
        this.up = up;
        this.down = down;
    }

    public KeyMouse (Window window, Transform transform, float speed) {
        this(window, transform, speed, KeyCode.W, KeyCode.S, KeyCode.D, KeyCode.A, KeyCode.Space, KeyCode.LeftShift);
    }

    public KeyMouse (Window window, float speed) {
        this(window, window.mainCamera, speed);
    }

    public KeyMouse (Window window) {
        this(window, 1);
    }

    @Override
    public Vector3 movementTranslate(Time delta) {
        return new StatVector3(getValue(right, left), getValue(up, down), getValue(backward, forward)).toRelative().mul(delta.getValue());
    }

    @Override
    public void rotate(Time delta) {
        Vector2 rot = window.input.getMouseRel().toRelative().subtr(0.5f).mul(-2 * Math.PI);
        transform.setRotation(new StatVector3(rot.y(), rot.x(), 0).toRelative());
    }

    private int getValue (KeyCode plus, KeyCode minus) {
        return (window.input.isPressed(plus) ? 1 : 0) - (window.input.isPressed(minus) ? 1 : 0);
    }
}
