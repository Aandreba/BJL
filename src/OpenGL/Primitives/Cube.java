package OpenGL.Primitives;

import OpenGL.Mesh;

public class Cube extends Mesh {
    public Cube () {
        super(new float[] {
            -1, -1, 1,
            1, -1, 1,
            1, 1, 1,
            -1, 1, 1,
            -1, 1, -1,
            1, 1, -1,
            1, -1, -1,
            -1, -1, -1
        }, new int[] {
            1, 2, 0,
            2, 3, 0,
            4, 3, 2,
            5, 4, 2,
            5, 2, 1,
            6, 5, 1,
            4, 7, 0,
            3, 4, 0,
            7, 4, 5,
            6, 7, 5,
            7, 6, 0,
            6, 1, 0
        });

        this.calculateNormals();
        this.calculateTexCoords();
        this.draw();
    }
}
