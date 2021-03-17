package OpenGL.Input;

import OpenGL.Input.Gamepad.GamepadState;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public enum Joystick {
    One,
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Ten,
    Eleven,
    Twelve,
    Thirteen,
    Fourteen,
    Fifteen,
    Sixteen;

    public boolean isPresent() {
        return glfwJoystickPresent(ordinal());
    }

    public String getName () {
        return glfwGetJoystickName(ordinal());
    }

    public String getGamepadName () {
        if (!isGamepad()) return null;
        return glfwGetGamepadName(ordinal());
    }

    public boolean isGamepad () {
        return glfwJoystickIsGamepad(ordinal());
    }

    public GamepadState getState () {
        ByteBuffer buttons = glfwGetJoystickButtons(ordinal());
        FloatBuffer axes = glfwGetJoystickAxes(ordinal());

        return new GamepadState(buttons, axes);
    }
}
