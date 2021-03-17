import Matrix.RelMatrix;
import OpenGL.Extras.Move.KeyMouse;
import OpenGL.GameObject;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Mesh.Mesh;
import OpenGL.Primitives.Sphere;
import OpenGL.Shader.DefaultShader;
import OpenGL.Window;
import Units.Time;
import Vector.RelVector;

import java.awt.*;

public class Main {
    public static void main (String[] args) throws Exception {
        Window window = new Window("Hello world", 1024, 1024, false) {
            KeyMouse move = new KeyMouse(this);
            KeyMouse moveObj = new KeyMouse(this, get(0).transform, 1, KeyCode.Up, KeyCode.Down, KeyCode.Right, KeyCode.Left, null, null);

            @Override
            public void update (Time delta) {
                double time = delta.getValue();
                move.update(delta);
                moveObj.update(delta);
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
