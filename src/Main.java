import Extras.Mathf;
import OpenGL.*;
import OpenGL.Extras.Move.KeyMouse;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Light.LightPoint;
import OpenGL.Primitives.Sphere;
import OpenGL.Window;
import Units.Time;
import java.awt.*;

public class Main {
    public static void main (String[] args) throws Exception {
        LightPoint light = new LightPoint(new StatVector3(0, 0, 0), Color.WHITE, 0.5f);
        LightPoint light2 = new LightPoint(new StatVector3(0.25f, 0, -2.5f), Color.YELLOW, 2.5f);

        Window window = new Window("Hello world", 1024, 1024, false) {
            KeyMouse move = new KeyMouse(this);
            float r, g, b, sum;

            @Override
            public void update (Time delta) {
                float sec = (float) delta.getValue();
                sum += sec;

                if (this.input.isPressed(KeyCode.Escape)) {
                    System.exit(1);
                }

                light.position = mainCamera.position.toRelative();
                light2.intensity = (1 + Mathf.sin(sum)) / 4;
                //colorChange(10 * sec);

                double time = delta.getValue();
                move.update(delta);

                if (input.isPressed(KeyCode.Up)) {
                    get(1).transform.rotation.addX(time);
                }

                if (input.isPressed(KeyCode.Down)) {
                    get(1).transform.rotation.addX(-time);
                }

                if (input.isPressed(KeyCode.Right)) {
                    get(1).transform.rotation.addY(-time);
                }

                if (input.isPressed(KeyCode.Left)) {
                    get(1).transform.rotation.addY(time);
                }
            }

            void colorChange (float sec) {
                r = light2.color.getRed() + sec;
                g = light2.color.getGreen() + sec;
                b = light2.color.getBlue() + sec;

                r = r >= 255 ? r - 255 : r;
                g = g >= 255 ? g - 255 : g;
                b = b >= 255 ? b - 255 : b;

                light2.color = new Color(Math.round(r), Math.round(g), Math.round(b));
            }
        };

        window.points[0] = light;
        window.points[1] = light2;

        Material material = new Material(Color.RED, 1f);
        Material material1 = new Material(Color.BLUE, 1f);

        GameObject planet1 = new GameObject(new Sphere(), material);
        GameObject planet2 = new GameObject(OBJLoader.loadMesh("bunny.obj"), material1);

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
