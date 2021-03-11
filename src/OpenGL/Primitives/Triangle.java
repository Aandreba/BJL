package OpenGL.Primitives;

import OpenGL.Mesh.Mesh;
import OpenGL.Shader.Shader;

import java.io.File;

public class Triangle extends Mesh {
    public Triangle (Shader shader) throws Exception {
        super(3,1, shader);

        this.setVertex(0, 1, 0, 0);
        this.setVertex(1, 0, 1, 0);
        this.setVertex(2, 0, 0, 1);

        this.setTriangle(0, 0, 1, 2);
        this.draw();
    }
}
