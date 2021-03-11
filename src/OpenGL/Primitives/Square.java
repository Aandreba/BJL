package OpenGL.Primitives;

import OpenGL.Mesh.Mesh;
import OpenGL.Shader.Shader;

public class Square extends Mesh {
    public Square(Shader shader) throws Exception {
        super(new float[] {
                -1, 0, 0,
                -1, -1, 0,
                0, -1, 0,
                0, 0, 0
        }, new int[] {
                0, 1, 3,
                3, 1, 2
        }, shader);

        this.draw();
    }
}
