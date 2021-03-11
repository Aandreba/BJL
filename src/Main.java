import OpenGL.Mesh.Mesh;
import OpenGL.Primitives.Cube;
import OpenGL.Primitives.Square;
import OpenGL.Primitives.Triangle;
import OpenGL.Shader.Primitives.ColorShader;
import OpenGL.Window;
import Units.Time;
import java.awt.*;

public class Main {
    public static void main (String[] args) throws Exception {
        Window window = new Window("Hello world", 1024, 1024, false) {
            float color = 0;

            @Override
            public void update (Time delta) {
                color += delta.getValue() * 10;
                int integer = Math.round(color);

                if (integer > 255) {
                    integer = 0;
                    color = 0;
                }

                //shader.setColor(new Color(integer, integer, integer, 255));
            }
        };

        ColorShader shader = new ColorShader(new Color(255, 0, 0, 255));
        Mesh mesh = new Triangle(shader);
        Mesh mesh2 = new Square(shader);

        window.add(mesh);
        window.add(mesh2);
        window.run();
    }
}
