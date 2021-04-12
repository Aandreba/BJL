import Extras.Rand;
import OpenGL.*;
import OpenGL.Extras.Gravity.Newton;
import OpenGL.Extras.Move.KeyMouse;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Light.DirectionalLight;
import OpenGL.Light.SpotLight;
import OpenGL.Primitives.Cube;
import OpenGL.Primitives.Sphere;
import OpenGL.Window;
import Units.Angle;
import Units.Mass;
import Units.Time;
import java.awt.*;

public class Main {
    public static Window window;

    public static void main (String[] args) throws Exception {
        DirectionalLight light = new DirectionalLight(Color.WHITE, new Angle(135, Angle.Type.Degrees), 1f);
        SpotLight spot = new SpotLight(StatVector3.zero, StatVector3.forward, new Angle(45, Angle.Type.Degrees), Color.WHITE, 1f);

        window = new Window("Hello world", 1024, 1024, false) {
            KeyMouse move = new KeyMouse(this);
            boolean kill = false;

            @Override
            public void update (Time delta) {
                float sec = (float) delta.getValue();
                if (kill || input.isPressed(KeyCode.Escape)) {
                    System.exit(1);
                }
                move.update(delta);
                spot.position = getMainCamera().position.toRelative();

                if (input.isPressed(KeyCode.Left)) {
                    light.angle = light.angle.add(new Angle(10 * sec, Angle.Type.Degrees));
                }

                if (input.isPressed(KeyCode.Right)) {
                    light.angle = light.angle.add(new Angle(-10 * sec, Angle.Type.Degrees));
                }

                if (input.isPressed(KeyCode.Up)) {
                    get(0).transform.rotation.addY(sec);
                }

                if (input.isPressed(KeyCode.Down)) {
                    get(0).transform.rotation.addY(-sec);
                }
            }
        };

        window.directionals[0] = light;
        window.spots[0] = spot;

        GameObject terrain = createTerrain();
        GameObject sphere = createSphere(Color.RED);
        //GameObject box = createBox(Color.BLUE);
        //sphere2.tags.add("two");

        terrain.rb.setAngularVelocity(0, 0, 1);
        sphere.transform.setPosition(0, 12.5, -2);
        sphere.rb.setVelocity(-1, 0, 0);
        //sphere.rb.angularDrag = 0;
        //box.transform.setPosition(0, 15, -2);
        //box.rb.setVelocity(0, 0, 0);
        //box.rb.applyGravity = false;

        window.getMainCamera().setPosition(0, 15, 15);
        window.run();
    }

    public static GameObject createTerrain () throws Exception {
        GameObject terrain = new GameObject(new Sphere(50, 50), new Material(new Texture("sample.bmp"), 1f));
        terrain.transform.setScale(10f);
        terrain.transform.position.addY(0);
        terrain.transform.position.addZ(-2);
        //terrain.transform.scale.addZ(100f);
        terrain.createSphereCollider();

        terrain.createRigidbody(new Mass(1, Mass.Astronomical.EarthMasses));
        //terrain.rb.setFreeze(true);
        terrain.rb.angularDrag = 0;
        terrain.rb.applyGravity = false;

        window.add(terrain);
        return terrain;
    }

    public static GameObject createBox (Color color) {
        GameObject sphere = new GameObject(new Cube(), new Material(color, 1f));
        sphere.tags.add("box");
        sphere.transform.setScale(0.25f);
        sphere.transform.position.addZ(-2);

        sphere.createBoxCollider();
        sphere.createRigidbody(new Mass(2));
        sphere.rb.drag = 0.47f;

        window.add(sphere);
        return sphere;
    }

    public static GameObject createSphere (Color color) {
        GameObject sphere = new GameObject(new Sphere(50, 50), new Material(color, 1f));
        sphere.tags.add("sphere");
        sphere.transform.setScale(0.25f);
        sphere.transform.position.addZ(-2);

        sphere.createSphereCollider();
        sphere.createRigidbody(new Mass(2));
        sphere.rb.drag = 0.47f;

        window.add(sphere);
        return sphere;
    }

    public static GameObject createBunny (Color color) throws Exception {
        GameObject sphere = new GameObject(OBJLoader.loadMesh("bunny.obj"), new Material(color, 1f));
        sphere.tags.add("bunny");
        sphere.transform.setScale(0.25f);
        sphere.transform.position.addZ(-2);

        sphere.createBoxCollider();
        sphere.createRigidbody(new Mass(Rand.getDouble(2, 70)));
        sphere.rb.drag = 1;

        window.add(sphere);
        return sphere;
    }
}
