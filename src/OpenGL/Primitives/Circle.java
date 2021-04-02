package OpenGL.Primitives;

import Extras.Mathf;
import OpenGL.Mesh;

public class Circle extends Mesh {
    public Circle(int points) {
        super(2+points, points);

        this.setVertex(0, 0, 0, 0);
        this.setVertex(1, 1, 0, 0);

        float step = 2 * Mathf.PI / points;
        for (int i=1;i<points;i++) {
            float angle = i * step;

            float x = Mathf.cos(angle);
            float y = Mathf.sin(angle);

            this.setVertex(i+1, x, y, 0);
            this.setTriangle(i-1, 0, i, i+1);
        }

        this.setTriangle(points-1, 0, points, 1);

        this.calculateTexCoords();
        this.calculateNormals();
        this.draw();
    }

    public Circle() {
        this(100);
    }
}
