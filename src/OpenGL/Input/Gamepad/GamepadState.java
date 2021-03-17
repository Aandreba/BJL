package OpenGL.Input.Gamepad;

import OpenGL.Extras.Vector.StatVector2;
import OpenGL.Input.Buttons.GamepadButton;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class GamepadState {
    final public boolean[] buttons;
    final public float[] axes;

    public GamepadState(ByteBuffer buttons, FloatBuffer axes) {
        this.buttons = new boolean[15];
        for (int i=0;i<buttons.capacity();i++) {
            this.buttons[i] = buttons.get(i) == (byte)1;
        }

        this.axes = new float[6];
        for (int i=0;i<axes.capacity();i++) {
            this.axes[i] = axes.get(i);
        }
    }

    public boolean isPressed (GamepadButton button) {
        return this.buttons[button.ordinal()];
    }

    public float getAxisValue (GamepadAxis axis) {
        return this.axes[axis.ordinal()];
    }

    public StatVector2 getLeftAxis () {
        return new StatVector2(axes[0], axes[1]);
    }

    public StatVector2 getRightAxis () {
        return new StatVector2(axes[2], axes[3]);
    }
}
