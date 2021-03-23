import OpenGL.*;
import OpenGL.Extras.Move.KeyMouse;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Input.Buttons.KeyCode;
import OpenGL.Light.DirectionalLight;
import OpenGL.Light.PointLight;
import OpenGL.Light.SpotLight;
import OpenGL.Primitives.Sphere;
import OpenGL.Window;
import Units.Angle;
import Units.Time;
import java.awt.*;

public class Main {
    public static void main (String[] args) throws Exception {
        PointLight point = new PointLight(new StatVector3(0, 1, -2), Color.RED, 1f);
        PointLight pointBunny = new PointLight(new StatVector3(1, 0, -3), Color.GRAY, 1f);

        SpotLight player = new SpotLight(new StatVector3(0, 0, 0), new StatVector3(0, 0, 1), new Angle(5, Angle.Type.Degrees), Color.GREEN, 1f);
        DirectionalLight dir = new DirectionalLight(Color.BLUE, new Angle(0, Angle.Type.Degrees), 0.5f);

        Window window = new Window("Hello world", 1024, 1024, false) {
            KeyMouse move = new KeyMouse(this);

            @Override
            public void update (Time delta) {
                float sec = (float) delta.getValue();
                if (this.input.isPressed(KeyCode.Escape)) {
                    System.exit(1);
                }
                move.update(delta);

                player.position = mainCamera.position.toRelative();
                player.direction = mainCamera.translateVector(StatVector3.forward.toRelative());
                player.direction.addY(-mainCamera.getRotationX().sinf());

                for (int i=0;i<size();i++) {
                    get(i).transform.rotation.addX(0.05f * (i + 1) * sec);
                    get(i).transform.rotation.addY(0.1f * (i + 1) * sec);
                    get(i).transform.rotation.addZ(0.15f * (i + 1) * sec);
                }
            }
        };

        window.points[0] = point;
        window.points[1] = pointBunny;
        //window.directionals[0] = dir;
        window.spots[0] = player;

        Material material = new Material(new Texture("sample.bmp"), 0.75f);
        GameObject cube = new GameObject(OBJLoader.loadMesh("cube.obj"), material);
        cube.transform.setScale(0.25f);
        cube.transform.setPosition(0, 0, -2);
        window.add(cube);

        Material materialSphere = new Material(Color.RED, 1f);
        GameObject sphere = new GameObject(new Sphere(50, 50), materialSphere);
        sphere.transform.setScale(0.25f);
        sphere.transform.setPosition(0, 1, -2);
        window.add(sphere);

        Material materialBunny = new Material(Color.GREEN, 0.25f);
        GameObject bunny = new GameObject(OBJLoader.loadMesh("bunny.obj"), materialBunny);
        bunny.transform.setScale(0.25f);
        bunny.transform.setPosition(2, 0, -3);
        window.add(bunny);

        window.run();
    }
}
