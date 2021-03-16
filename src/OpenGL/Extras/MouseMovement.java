package OpenGL.Extras;

import OpenGL.Transform;
import OpenGL.Window;
import Units.Time;
import org.lwjgl.glfw.GLFW;

import java.awt.event.MouseEvent;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

public class MouseMovement {
    public int forward, backward, right, left, up, down;
    private boolean moveForward, moveBackward, moveRight, moveLeft, moveUp, moveDown;
    private float lastMousePosX, lastMousePosY;

    final public Window window;
    final public Transform transform;

    public float speed;

    public MouseMovement (int forward, int backward, int right, int left, int up, int down, float speed, Window window, Transform transform) {
        this.forward = forward;
        this.backward = backward;
        this.right = right;
        this.left = left;
        this.up = up;
        this.down = down;
        this.speed = speed;
        this.window = window;
        this.transform = transform;

        float pi = (float)Math.PI;
        glfwSetCursorPosCallback(window.id, (handle, xpos, ypos) -> {
            float x = (float)xpos - (window.getWidth() / 2f);
            float y = (float)ypos - (window.getHeight() / 2f);

            float deltaX = (x - lastMousePosX) * pi / window.getWidth();
            float deltaY = (y - lastMousePosY) * pi / window.getWidth();

            transform.rotation.addY(-deltaX);
            transform.rotation.addX(-deltaY);

            lastMousePosX = x;
            lastMousePosY = y;
        });

        glfwSetKeyCallback(window.id, (handle, key, scancode, action, mods) -> {
            boolean nw = true;
            if (action == GLFW_RELEASE) {
                nw = false;
            } else if (action != GLFW_PRESS) {
                return;
            }

            if (key == forward) {
                moveForward = nw;
            } else if (key == backward) {
                moveBackward = nw;
            } else if (key == right) {
                moveRight = nw;
            } else if (key == left) {
                moveLeft = nw;
            } else if (key == up) {
                moveUp = nw;
            } else if (key == down) {
                moveDown = nw;
            }
        });
    }

    public MouseMovement (Window window, Transform transform) {
        this(GLFW_KEY_W, GLFW_KEY_S, GLFW_KEY_D, GLFW_KEY_A, GLFW_KEY_SPACE, GLFW_KEY_LEFT_SHIFT, 1f, window, transform);
    }

    public MouseMovement (Window window) {
        this (window, window.mainCamera);
    }

    public void update (Time delta) {
        float sec = (float) delta.getValue();

        if (moveForward) {
            transform.position.addZ(-speed * sec);
        }

        if (moveBackward) {
            transform.position.addZ(speed * sec);
        }

        if (moveRight) {
            transform.position.addX(speed * sec);
        }

        if (moveLeft) {
            transform.position.addX(-speed * sec);
        }

        if (moveUp) {
            transform.position.addY(speed * sec);
        }

        if (moveDown) {
            transform.position.addY(-speed * sec);
        }
    }
}
