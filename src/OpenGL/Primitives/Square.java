package OpenGL.Primitives;

import OpenGL.Mesh;

import java.util.Arrays;

public class Square extends Mesh {
    public Square() {
        super(new float[] {
                -1, 1, 0,
                -1, -1, 0,
                0, -1, 0,
                0, 0, 0
        }, new int[] {
                0, 1, 3,
                3, 1, 2
        });

        this.calculateNormals();
        this.calculateTexCoords();
        this.draw();
    }

    public Square (int points) {
        super(points * points, 2 * (points - 1) * (points - 1));

        float step = 2f / points;
        int k = 0;

        for (int i=0;i<points;i++) {
            for (int j=0;j<points;j++) {
                int pos = (i * points) + j;
                int up = (i * points) + j + 1;
                int left = ((i - 1) * points) + j;
                int down = (i * points) + j - 1;
                int right = ((i + 1) * points) + j;

                this.setVertex(pos, i * step, j * step, 1);

                if (i > 0 && j+1 < points) {
                    this.setTriangle(k, pos, up, left);
                    k++;
                }

                if (j > 0 && i+1 < points) {
                    this.setTriangle(k, pos, down, right);
                    k++;
                }
            }
        }

        this.calculateTexCoords();
        this.calculateNormals();
        this.draw();
    }
}
