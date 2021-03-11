package OpenGL.Primitives;

import OpenGL.Mesh.Mesh;
import OpenGL.Shader.Shader;

public class Cube extends Mesh {
    public Cube (Shader shader) {
        super(8, 12, shader);

        // Vertices
        this.setVertex(0, -0.5f, 0.5f, 0.5f);
        this.setVertex(1, -0.5f, -0.5f, 0.5f);
        this.setVertex(2, 0.5f, -0.5f, 0.5f);
        this.setVertex(3, 0.5f, 0.5f, 0.5f);
        this.setVertex(4, -0.5f, 0.5f, -0.5f);
        this.setVertex(5, 0.5f, 0.5f, -0.5f);
        this.setVertex(6, -0.5f, -0.5f, -0.5f);
        this.setVertex(7, 0.5f, -0.5f, -0.5f);

        // Triangles
        this.setTriangle(0, 0, 1, 3); // Front face
        this.setTriangle(1, 3, 1, 2);

        this.setTriangle(2, 4, 0, 3); // Top face
        this.setTriangle(3, 5, 4, 3);

        this.setTriangle(4, 3, 2, 7); // Right face
        this.setTriangle(5, 5, 3, 7);

        this.setTriangle(6, 6, 1, 0); // Left face
        this.setTriangle(7, 6, 0, 4);

        this.setTriangle(8, 2, 1, 6); // Bottom face
        this.setTriangle(9, 2, 6, 7);

        this.setTriangle(10, 7, 6, 4); // Back face
        this.setTriangle(11, 7, 4, 5);
    }
}
