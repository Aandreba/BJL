import Extras.Rand;
import Matrix.RelMatrix;
import OpenGL.Extras.Gravity.Gravity;
import OpenGL.Extras.Gravity.Newton;
import OpenGL.Extras.Gravity.Normal;
import OpenGL.Extras.Move.KeyMouse;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.GameObject;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Mesh.Mesh;
import OpenGL.Primitives.Sphere;
import OpenGL.Rigidbody;
import OpenGL.Shader.DefaultShader;
import OpenGL.Window;
import Units.Mass;
import Units.Time;
import Vector.RelVector;

import java.awt.*;
import java.util.ArrayList;

public class Main {
    public static void main (String[] args) throws Exception {
        ArrayList<Rigidbody> rbs = new ArrayList<>();
        Gravity gravity = new Newton();

        Window window = new Window("Hello world", 1024, 1024, false) {
            KeyMouse move = new KeyMouse(this);

            @Override
            public boolean add(GameObject gameObject) {
                Rigidbody rb = new Rigidbody(gameObject, new Mass(Rand.getDouble(0.5,2)));
                rb.velocity = new StatVector3(0, 0, 0);

                rbs.add(rb);
                gravity.add(rb);
                return super.add(gameObject);
            }

            @Override
            public void update (Time delta) {
                double time = delta.getValue();
                double fps = Math.pow(time, -1);
                System.out.println(fps);

                move.update(delta);
                gravity.update(delta);

                if (rbs.get(0).isCollidingWith(rbs.get(1))){
                    System.out.println("Collision!");
                }

                if (input.isPressed(KeyCode.Up)) {
                    get(0).transform.rotation.addX(time);
                }

                if (input.isPressed(KeyCode.Down)) {
                    get(0).transform.rotation.addX(-time);
                }

                if (input.isPressed(KeyCode.Right)) {
                    get(0).transform.rotation.addY(time);
                }

                if (input.isPressed(KeyCode.Left)) {
                    get(0).transform.rotation.addY(-time);
                }
            }
        };

        DefaultShader shader = new DefaultShader();
        GameObject planet1 = new GameObject(new Sphere(255, 255), shader);
        GameObject planet2 = new GameObject(new Sphere(), shader);

        planet1.transform.setScale(0.2f);
        planet2.transform.setScale(0.1f);

        planet1.transform.setPosition(0, 0, -2);
        planet2.transform.setPosition(0.5f, 0, -2);

        colorSphere(planet1.mesh, Color.RED, Color.GREEN, Color.BLUE, Color.WHITE);
        colorSphere(planet2.mesh, Color.ORANGE, Color.YELLOW, Color.MAGENTA, Color.PINK);

        planet1.mesh.draw();
        planet2.mesh.draw();

        window.add(planet1);
        window.add(planet2);

        window.run();
    }

    private static void colorSphere (Mesh sphere, Color from, Color to, Color left, Color right) {
        RelMatrix matrix = sphere.getMatrix();
        for (int i=0;i<matrix.getRows();i++) {
            RelVector vector = matrix.get(i);

            float pct = Math.abs(vector.getFloat(1));
            float pct2 = Math.abs(vector.getFloat(0));

            int r = Math.round(from.getRed() * pct + to.getRed() * (1 - pct));
            int g = Math.round(from.getGreen() * pct + to.getGreen() * (1 - pct));
            int b = Math.round(from.getBlue() * pct + to.getBlue() * (1 - pct));
            int a = Math.round(from.getAlpha() * pct + to.getAlpha() * (1 - pct));

            int r2 = Math.round(left.getRed() * pct2 + right.getRed() * (1 - pct2));
            int g2 = Math.round(left.getGreen() * pct2 + right.getGreen() * (1 - pct2));
            int b2 = Math.round(left.getBlue() * pct2 + right.getBlue() * (1 - pct2));
            int a2 = Math.round(left.getAlpha() * pct2 + right.getAlpha() * (1 - pct2));

            sphere.setColor(i, new Color((r + r2) / 2, (g + g2) / 2, (b + b2) / 2, (a + a2) / 2));
        }
    }
}
