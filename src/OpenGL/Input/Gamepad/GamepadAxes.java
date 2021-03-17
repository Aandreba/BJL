package OpenGL.Input.Gamepad;

import static OpenGL.Input.Gamepad.GamepadAxis.*;

public enum GamepadAxes {
    Left(LeftX, LeftY),
    Right(RightX, RightY),
    Triggers(LeftTrigger, RightTrigger);

    GamepadAxis x;
    GamepadAxis y;
    GamepadAxes (GamepadAxis x, GamepadAxis y) {
        this.x = x;
        this.y = y;
    }
}
