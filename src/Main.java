import OpenGL.Extras.Move.KeyMouse;
import OpenGL.GameObject;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Primitives.Cube;
import OpenGL.Primitives.Sphere;
import OpenGL.Shader;
import OpenGL.Texture;
import OpenGL.Window;
import Units.Time;
import java.awt.*;

public class Main {
    public static void main (String[] args) throws Exception {
        Window window = new Window("Hello world", 1024, 1024, false) {
            KeyMouse move = new KeyMouse(this);

            @Override
            public void update (Time delta) {
                if (this.input.isPressed(KeyCode.Escape)) {
                    System.exit(1);
                }

                double time = delta.getValue();
                move.update(delta);

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

        Shader shader = new Shader();
        Texture tex = new Texture("sample.bmp");
        Texture tex2 = new Texture(new Color(255, 0, 0));

        GameObject planet1 = new GameObject(new Cube(), shader);
        GameObject planet2 = new GameObject(new Cube(), shader);

        planet1.texture = tex;
        planet2.texture = tex2;

        planet1.transform.setScale(0.2f);
        planet2.transform.setScale(0.1f);

        planet1.transform.setPosition(0, 0, -2);
        planet2.transform.setPosition(0.5f, 0, -2);

        window.add(planet1);
        window.add(planet2);

        window.run();
    }

    /*private static void colorSphere (Mesh sphere, Color from, Color to, Color left, Color right) {
        RelMatrix matrix = sphere.vertexMatrix();
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
    }*/
}
