import OpenGL.*;
import OpenGL.Colliders.BoxCollider;
import OpenGL.Colliders.SphereCollider;
import OpenGL.Extras.Move.KeyMouse;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Light.DirectionalLight;
import OpenGL.Light.PointLight;
import OpenGL.Light.SpotLight;
import OpenGL.Primitives.Cube;
import OpenGL.Primitives.Sphere;
import OpenGL.Window;
import Units.Angle;
import Units.Time;
import java.awt.*;

public class Main {
    public static Window window;

    public static void main (String[] args) throws Exception {
        PointLight point = new PointLight(new StatVector3(0, 0, 0), Color.WHITE, 1f);

        window = new Window("Hello world", 1024, 1024, false) {
            KeyMouse move = new KeyMouse(this);

            @Override
            public void update (Time delta) {
                float sec = (float) delta.getValue();
                if (this.input.isPressed(KeyCode.Escape)) {
                    System.exit(1);
                }
                move.update(delta);

                GameObject sun = getFirstWithTag("sun");
                GameObject earth = getFirstWithTag("planet");

                if (input.isPressed(KeyCode.Up)) {
                    earth.transform.position.addZ(-sec);
                }

                if (input.isPressed(KeyCode.Down)) {
                    earth.transform.position.addZ(sec);
                }

                if (input.isPressed(KeyCode.Right)) {
                    earth.transform.position.addX(sec);
                }

                if (input.isPressed(KeyCode.Left)) {
                    earth.transform.position.addX(-sec);
                }

                if (input.isPressed(KeyCode.KpAdd)) {
                    sun.transform.rotation.addY(sec);
                    earth.transform.rotation.addY(sec);
                }

                if (input.isPressed(KeyCode.KpSubtract)) {
                    sun.transform.rotation.addY(-sec);
                    earth.transform.rotation.addY(-sec);
                }

                System.out.println(earth.transform.position);
                earth.collider.isCollidingAt(new StatVector3(0, 0, -2));
            }
        };
        window.points[0] = point;

        GameObject sun = createSun(Color.YELLOW);
        GameObject earth = createPlanet(Color.BLUE);

        earth.transform.setPosition(1, 0, -2);
        window.run();
    }

    public static GameObject createSun (Color color) {
        GameObject sphere = new GameObject(new Sphere(50, 50), new Material(color, 1f));
        sphere.tags.add("sun");
        sphere.transform.setScale(0.25f);
        sphere.transform.position.set(2, -2);

        sphere.collider = new SphereCollider(new Vector3() {
            @Override
            public double get(int pos) {
                return sphere.transform.position.get(pos);
            }
        }, 0.25f);

        window.add(sphere);
        return sphere;
    }

    public static GameObject createPlanet (Color color) {
        GameObject sphere = new GameObject(new Cube(), new Material(color, 1f));
        sphere.tags.add("planet");
        sphere.transform.setScale(0.25f);
        sphere.transform.position.set(2, -2);
        sphere.createCollider();

        window.add(sphere);
        return sphere;
    }
}
