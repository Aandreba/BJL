package OpenGL.Primitives;

import OpenGL.Mesh;

public class Square extends Mesh {
    /*public Square() throws Exception {
        super(new float[] {
                -1, 0, 0,
                -1, -1, 0,
                0, -1, 0,
                0, 0, 0
        }, new int[] {
                0, 1, 3,
                3, 1, 2
        });

        this.draw();
    }*/

    public Square() throws Exception {
        super(new float[] {
                -1, 1, 0,
                -1, -1, 0,
                0, -1, 0,
                0, 0, 0
        }, new int[] {
                0, 1, 3,
                3, 1, 2
        });

        this.draw();
    }
}
