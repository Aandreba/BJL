import Extras.Rand;
import OpenGL.Extras.MouseMovement;
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
            MouseMovement camera = new MouseMovement(this);

            @Override
            public void update (Time delta) {
                camera.update(delta);

                double time = delta.getValue();
                GameObject square = get(0);

                square.transform.rotation.set(0, square.transform.rotation.get(0) + time);
                square.transform.rotation.set(1, square.transform.rotation.get(1) + time);
                square.transform.rotation.set(2, square.transform.rotation.get(2) + time);
            }
        };

        GameObject cube = initCube();
        window.add(cube);
        window.run();
    }

    private static GameObject initCube () throws Exception {
        DefaultShader shader = new DefaultShader();
        GameObject square = new GameObject(new Cube(), shader);

        square.transform.setScale(0.25f);
        square.transform.position.set(2, -1.5f);

        for (int i=0;i<square.mesh.getVertexCount();i++) {
            square.mesh.setColor(i, new Color(Rand.getInt(0, 255), Rand.getInt(0, 255), Rand.getInt(0, 255)));
        }

        square.mesh.draw();
        return square;
    }
}
