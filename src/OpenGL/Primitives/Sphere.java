package OpenGL.Primitives;

import OpenGL.Mesh.Mesh;

import java.util.Arrays;

public class Sphere extends Mesh {
    public Sphere (int points) {
        super(2+points, points);

        this.setVertex(0, 0, 0, 0);
        this.setVertex(1, 1, 0, 0);

        float step = 2 * (float)Math.PI / points;
        for (int i=1;i<points;i++) {
            float angle = i * step;

            float x = (float) Math.cos(angle);
            float y = (float) Math.sin(angle);

            this.setVertex(i+1, x, y, 0);
            this.setTriangle(i-1, 0, i, i+1);
        }

        this.setTriangle(points-1, 0, points, 1);
        this.draw();
    }

    public Sphere () {
        this(255);
    }
}
