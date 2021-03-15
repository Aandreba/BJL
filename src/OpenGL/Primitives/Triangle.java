package OpenGL.Primitives;

import OpenGL.Mesh.Mesh;

public class Triangle extends Mesh {
    public Triangle () throws Exception {
        super(3,1);

        this.setVertex(0, 1, 0, 0);
        this.setVertex(1, 0, 1, 0);
        this.setVertex(2, 0, 0, 1);

        this.setTriangle(0, 0, 1, 2);
        this.draw();
    }
}
