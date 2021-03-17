package OpenGL.Input;

import OpenGL.Extras.Vector.StatVector2;
import OpenGL.Input.Buttons.GamepadButton;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Input.Gamepad.GamepadAxes;
import OpenGL.Input.Gamepad.GamepadAxis;
import OpenGL.Input.Gamepad.GamepadState;
import OpenGL.Window;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    final private Window window;

    final private HashMap<KeyCode, Boolean> keyPressed;
    private StatVector2 currentMouse;
    final private HashMap<Joystick, HashMap<GamepadButton, Boolean>> buttonPressed;
    final private HashMap<Joystick, HashMap<GamepadAxes, StatVector2>> currentJoystick;

    public Input (Window window) {
        this.window = window;
        this.keyPressed = new HashMap<>();
        this.buttonPressed = new HashMap<>();
        this.currentMouse = new StatVector2();
        this.currentJoystick = new HashMap<>();

        // Keys
        glfwSetKeyCallback(window.id, (w, k, scancode, action, mods) -> {
            boolean val;

            if (action == GLFW_PRESS) {
                val = true;
            } else if (action == GLFW_RELEASE) {
                val = false;
            } else {
                return;
            }

            KeyCode key = KeyCode.getKey(k);
            keyPressed.put(key, val);
        });

        // Mouse
        glfwSetCursorPosCallback(window.id, (handle, xpos, ypos) -> {
            currentMouse = new StatVector2(xpos, ypos);
        });

        // Setup buttons
        for (Joystick joystick: Joystick.values()) {
            this.buttonPressed.put(joystick, new HashMap<>());

            this.currentJoystick.put(joystick, new HashMap<>());
            for (GamepadAxes axis: GamepadAxes.values()) {
                this.currentJoystick.get(joystick).put(axis, new StatVector2());
            }
        }
    }

    public void update () {
        for (Joystick joystick: Joystick.values()) {
            if (!joystick.isPresent() || !joystick.isGamepad()) {
                this.currentJoystick.put(joystick, new HashMap<>());
                this.buttonPressed.put(joystick, new HashMap<>());
                continue;
            }
            GamepadState state = joystick.getState();

            HashMap<GamepadAxes, StatVector2> axis = new HashMap<>();
            axis.put(GamepadAxes.Left, state.getLeftAxis());
            axis.put(GamepadAxes.Right, state.getRightAxis());
            axis.put(GamepadAxes.Triggers, new StatVector2(state.getAxisValue(GamepadAxis.LeftTrigger), state.getAxisValue(GamepadAxis.RightTrigger)));
            this.currentJoystick.put(joystick, axis);

            for (GamepadButton button: GamepadButton.values()) {
                this.buttonPressed.get(joystick).put(button, state.isPressed(button));
            }
        }
    }

    public boolean isPressed (KeyCode key) {
        return keyPressed.getOrDefault(key, false);
    }

    public boolean isPressed (Joystick joystick, GamepadButton key) {
        return buttonPressed.get(joystick).getOrDefault(key, false);
    }

    public StatVector2 getMouse () {
        return currentMouse;
    }

    public StatVector2 getMouseRel () {
        return new StatVector2(currentMouse.get(0) / window.getWidth(), currentMouse.get(1) / window.getHeight());
    }

    public StatVector2 getJoystick (Joystick joystick, GamepadAxes axis) {
        return this.currentJoystick.get(joystick).getOrDefault(axis, new StatVector2());
    }

    public float mouseAxisValue (MouseAxis axis) {
        if (axis == MouseAxis.X) {
            return currentMouse.getFloat(0) / window.getWidth();
        } else {
            return currentMouse.getFloat(1) / window.getHeight();
        }
    }

    public float joystickAxisValue(Joystick joystick, GamepadAxis axis) {
        HashMap<GamepadAxes, StatVector2> joy = this.currentJoystick.get(joystick);
        try {
            return switch (axis) {
                case LeftX -> joy.get(GamepadAxes.Left).getFloat(0);
                case LeftY -> joy.get(GamepadAxes.Left).getFloat(1);
                case RightX -> joy.get(GamepadAxes.Right).getFloat(0);
                case RightY -> joy.get(GamepadAxes.Right).getFloat(1);
                case LeftTrigger -> joy.get(GamepadAxes.Triggers).getFloat(0);
                case RightTrigger -> joy.get(GamepadAxes.Triggers).getFloat(1);
            };
        } catch (Exception e) {
            return 0;
        }
    }

    public float getValue (GeneralInput input, Joystick joystick) {
        if (input instanceof KeyCode) {
            return isPressed((KeyCode)input) ? 1 : 0;
        } else if (input instanceof GamepadButton) {
            return isPressed(joystick, (GamepadButton)input) ? 1 : 0;
        } else if (input instanceof GamepadAxis) {
            return joystickAxisValue(joystick, (GamepadAxis)input);
        } else if (input instanceof MouseAxis) {
            return mouseAxisValue((MouseAxis)input);
        }

        return 1f / 0;
    }
}
