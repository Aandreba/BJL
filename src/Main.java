import Extras.Rand;
import OpenGL.Extras.MouseMovement;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.GameObject;
import OpenGL.Primitives.Cube;
import OpenGL.Primitives.Sphere;
import OpenGL.Primitives.Square;
import OpenGL.Shader.DefaultShader;
import OpenGL.Window;
import Units.Time;

import java.awt.*;

public class Main {
    public static void main (String[] args) throws Exception {
        Window window = new Window("Hello world", 1024, 1024, false) {
            final MouseMovement camera = new MouseMovement(this);
            float sum = 0;

            @Override
            public void update (Time delta) {
                camera.update(delta);

                double time = delta.getValue();
                GameObject square = get(0);
                GameObject square2 = get(1);

                square.transform.rotation.set(0, square.transform.rotation.get(0) + time);
                square.transform.rotation.set(1, square.transform.rotation.get(1) + time);
                square.transform.rotation.set(2, square.transform.rotation.get(2) + time);
                square2.transform.position.set(0, Math.sin(sum));

                sum += time;
            }
        };

        DefaultShader shader = new DefaultShader();
        GameObject cube = new GameObject(new Cube(), shader, new StatVector3(0, 0, -1).toRelative(), null, 0.25f);
        GameObject cube2 = new GameObject(new Cube(), shader, new StatVector3(1, 0, -1).toRelative(), null, 0.5f);

        colorObject(cube);
        colorObject(cube2);

        window.add(cube);
        window.add(cube2);
        window.run();
    }

    private static void colorObject (GameObject obj) throws Exception {
        for (int i=0;i<obj.mesh.getVertexCount();i++) {
            obj.mesh.setColor(i, new Color(Rand.getInt(0, 255), Rand.getInt(0, 255), Rand.getInt(0, 255)));
        }

        obj.mesh.draw();
    }
}
