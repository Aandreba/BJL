package OpenGL.Primitives;

import OpenGL.Mesh;

public class Cube extends Mesh {
    public Cube () {
        super(new float[] {
                // VO
                -1,  1,  1,
                // V1
                -1, -1,  1,
                // V2
                1, -1,  1,
                // V3
                1,  1,  1,
                // V4
                -1,  1, -1,
                // V5
                1,  1, -1,
                // V6
                -1, -1, -1,
                // V7
                1, -1, -1,
        }, new int[] {
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                4, 0, 3, 5, 4, 3,
                // Right face
                3, 2, 7, 5, 3, 7,
                // Left face
                6, 1, 0, 6, 0, 4,
                // Bottom face
                2, 1, 6, 2, 6, 7,
                // Back face
                7, 6, 4, 7, 4, 5,
        });
    }
}
