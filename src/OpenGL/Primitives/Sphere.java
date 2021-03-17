package OpenGL.Primitives;

import Extras.Mathf;
import OpenGL.Mesh.Mesh;

import java.util.Arrays;

public class Sphere extends Mesh {
    public Sphere(int rows, int cols) {
        super ((rows + 1) * cols, (rows + 1) * cols * 2);

        float angleStep = Mathf.PI / rows;
        float colStep = 2 * Mathf.PI / cols; // in radians

        for (int i=0;i<=rows;i++) {
            float angle = i * angleStep;
            float y = Mathf.cos(angle);
            float xz = Mathf.sin(angle);

            for (int j=0;j<cols;j++) {
                float colAngle = colStep * j;
                float z = xz * Mathf.sin(colAngle);
                float x = xz * Mathf.cos(colAngle);

                int self = (i * cols) + j;
                this.setVertex(self, x, y, z);

                // Triangles
                int down = ((i + 1) * cols) + j;
                int left, downLeft;

                if (j > 0) {
                    left = (i * cols) + j - 1;
                    downLeft = ((i + 1) * cols) + j - 1;
                } else {
                    left = (i * cols) + (cols - 1);
                    downLeft = ((i + 1) * cols) + (cols - 1);
                }

                this.setTriangle(2 * self, self, downLeft, down);
                this.setTriangle(2 * self + 1, self, left, downLeft);
            }
        }

        this.draw();
    }

    public Sphere () {
        this (20, 20);
    }
}
