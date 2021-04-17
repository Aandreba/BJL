import Extras.BitBuffer;
import Extras.Mathx;
import OpenGL.Extras.Move.KeyMouse;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.GameObject;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Primitives.Cube;
import OpenGL.Primitives.Objects.Terrain;
import OpenGL.Primitives.Sphere;
import OpenGL.Rigidbody;
import OpenGL.Texture;
import OpenGL.Window;
import Units.Mass;
import Units.Time;

import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Test {
    public static void main (String[] args) throws Exception {
        Window window = new Window ("Hello world", 900, 900, false) {
            KeyMouse move = new KeyMouse(this);

            @Override
            public void update(Time deltaTime) {
                if (input.isPressed(KeyCode.Escape)) {
                    System.exit(1);
                }
                move.update(deltaTime);
            }
        };

        Terrain terrain = new Terrain(50, new Texture("sample.bmp"));
        terrain.updateNoise();
        //System.out.println(terrain.mesh.vertexMatrix());
        terrain.transform.setPosition(0, 0, -2);
        //terrain.transform.setScale(50f);

        /*GameObject object = new GameObject(new Cube(), Color.RED);
        object.transform.setScale(0.25f);
        object.transform.setPosition(0, 10, -2);
        object.createBoxCollider();

        Rigidbody rb = object.createRigidbody(new Mass(1));
        rb.cor = 0.5f;
        rb.applyGravity = false;
        rb.setAngularVelocity(0,0,0);
        rb.setVelocity(-0.5, 0, 0);*/

        window.getMainCamera().setPosition(0, 0, 2);
        window.add(terrain);
        //window.add(object);
        window.run();
    }
}
