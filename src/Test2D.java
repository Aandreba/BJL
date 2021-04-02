import OpenGL.*;
import OpenGL.Extras.Move.KeyMouse;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Light.DirectionalLight;
import OpenGL.Primitives.*;
import OpenGL.Primitives.Objects.Pie;
import OpenGL.Window;
import Units.Angle;
import Units.Time;
import Vector.Vector;

import java.awt.*;

public class Test2D {
    static Window window;

    public static void main (String[] args) throws Exception {
        DirectionalLight light = new DirectionalLight(Color.WHITE, new Angle(90, Angle.Type.Degrees), 1f);
        System.out.println(Vector.class);

        window = new Window ("2D Test", 900, 900, false) {
            KeyMouse move = new KeyMouse(this);

            @Override
            public void update(Time deltaTime) {
                double time = deltaTime.getValue();
                long fps = Math.round(Math.pow(deltaTime.getValue(), -1));

                if (input.isPressed(KeyCode.Escape)) {
                    this.cleanup();
                    System.exit(1);
                }
                move.update(deltaTime);
            }
        };
        window.directionals[0] = light;

        GameObject cube = new GameObject(new Square(3), Color.RED);
        cube.transform.setRotationX(new Angle(90, Angle.Type.Degrees));
        cube.transform.setScale(0.5f);
        cube.transform.setPosition(0, 0, -2);
        window.add(cube);

        window.run();
    }
}
