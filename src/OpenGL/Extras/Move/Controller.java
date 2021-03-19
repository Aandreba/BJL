package OpenGL.Extras.Move;

import Extras.Mathf;
import OpenGL.Extras.Vector.StatVector2;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector2;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.Input.Buttons.GamepadButton;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Input.Gamepad.GamepadAxes;
import OpenGL.Input.Joystick;
import OpenGL.Transform;
import OpenGL.Window;
import Units.Time;

public class Controller extends Movement {
    public Joystick joystick;
    public GamepadAxes move, rotate;
    public GamepadButton up, down;

    float sensitivity;
    float speed;
    int accuracy = 2;

    public Controller(Joystick joystick, Window window, Transform transform, float speed, float sensitivity, GamepadAxes move, GamepadAxes rotate, GamepadButton up, GamepadButton down) {
        super(window, transform);
        this.speed = speed;
        this.sensitivity = sensitivity;
        this.joystick = joystick;
        this.move = move;
        this.rotate = rotate;
        this.up = up;
        this.down = down;
    }

    public Controller (Joystick joystick, Window window, Transform transform, float speed, float sensitivity) {
        this(joystick, window, transform, speed, sensitivity, GamepadAxes.Left, GamepadAxes.Right, GamepadButton.B, GamepadButton.A);
    }

    public Controller(Joystick joystick, Window window, float speed, float sensitivity) {
        this(joystick, window, window.mainCamera, speed, sensitivity);
    }

    public Controller(Joystick joystick, Window window) {
        this(joystick, window, 1, 0.5f);
    }

    @Override
    public Vector3 movementTranslate(Time delta) {
        StatVector2 trans = window.input.getJoystick(joystick, move);
        return new StatVector3(trans.get(0), 0, trans.get(1)).toRelative().mul(speed * delta.getValue());
    }

    @Override
    public void rotate(Time delta) {
        StatVector2 rot = window.input.getJoystick(joystick, rotate);
        StatVector3 target = new StatVector3(-Mathf.roundTo(rot.get(1), accuracy), -Mathf.roundTo(rot.get(0), accuracy), 0);
        Vector3 dt = target.mul(window.mainCamera.getFov().getValue()).subtr(transform.rotation.toRelative());

        transform.rotation.add(dt.mul(sensitivity));
    }
}
