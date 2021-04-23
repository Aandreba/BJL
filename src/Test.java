import Extras.Rand;
import OpenGL.*;
import OpenGL.Extras.Move.KeyMouse;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Primitives.Sphere;
import OpenGL.Window;
import Units.Mass;
import Units.Time;

import java.awt.*;

public class Test {
    public static void main (String[] args) throws Exception {
        Window window = new Window ("Hello world", 900, 900, false) {
            KeyMouse move = null;

            @Override
            public void start () {
                move = new KeyMouse(this, 10);
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

        GameObject terrain = new GameObject(new Sphere(100, 100), Color.WHITE);
        terrain.createSphereCollider();
        terrain.createRigidbody(new Mass(Float.MAX_VALUE));
        terrain.rb.applyGravity = false;

        terrain.transform.setScale(10);
        terrain.transform.setPosition(0, -2, 0);

        GameObject ball = createBall();
        GameObject ball2 = createBall();
        ball2.transform.position.set(1, 30);

        window.getMainCamera().setPosition(0, 25, -5);
        window.add(terrain);
        window.add(ball);
        window.add(ball2);
        window.run();
    }

    public static GameObject createBall () {
        GameObject ball = new GameObject(new Sphere(), Rand.getColorRGB());
        ball.transform.setPosition(0, 20, 0);
        ball.transform.setScale(Rand.getFloat(0.5f, 2));

        ball.createSphereCollider();
        ball.createRigidbody(new Mass(Rand.getDouble(0.5, 10)));
        ball.rb.cor = 0.9f;

        return ball;
    }
}
