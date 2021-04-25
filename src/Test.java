import Extras.Rand;
import OpenGL.*;
import OpenGL.Extras.Move.KeyMouse;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Primitives.Cube;
import OpenGL.Primitives.Objects.Terrain;
import OpenGL.Primitives.Sphere;
import OpenGL.Window;
import Units.Mass;
import Units.Time;

import java.awt.*;

public class Test {
    public static void main (String[] args) throws Exception {
        Window window = new Window ("Hello world", 900, 900, false) {
            KeyMouse move = null;
            GameObject terrain = null;

            @Override
            public void start () {
                move = new KeyMouse(this, 10);
                terrain = get(0);
            }

            @Override
            public void update (Time deltaTime) {
                move.update(deltaTime);

                if (input.isPressed(KeyCode.Escape)) {
                    cleanup();
                    System.exit(1);
                }
            }
        };

        /*HUD hud = window.createHUD();
        Text text = new Text("Hi!", "Arial");
        text.transform.setScale(0.25f);
        text.transform.setPosition(0, 0, 0);
        hud.add(text);*/

        GameObject terrain = new GameObject(new Cube(), new Texture("terrain.jpg"));
        terrain.createBoxCollider();
        terrain.createRigidbody(new Mass(1, Mass.Astronomical.EarthMasses));

        //terrain.rb.setFreeze(true);
        terrain.rb.applyGravity = false;
        terrain.rb.setVelocity(0, 0.1, 0);
        terrain.transform.setScale(10, 10, 10);
        terrain.transform.setPosition(0, -2, 0);

        GameObject ball = createBall();
        //GameObject ball2 = createBall();
        //ball2.transform.position.set(1, 50);

        window.getMainCamera().setPosition(0, 50, 0);
        window.add(terrain);
        window.add(ball);
        //window.add(ball2);
        window.run();
    }

    public static GameObject createBall () {
        GameObject ball = new GameObject(new Sphere(), Rand.getColorRGB());
        ball.tags.add("Ball");
        ball.transform.setPosition(0, 20, 0);
        ball.transform.setScale(1);

        ball.createSphereCollider();
        ball.createRigidbody(new Mass(2));
        ball.rb.cor = 0.5f;

        return ball;
    }
}
