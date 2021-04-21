package OpenGL.Primitives.Objects;

import Extras.Rand;
import Matrix.Matrix;
import Matrix.RelMatrix;
import Matrix.StatMatrix;
import OpenGL.Extras.Vector.StatVector2;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.GameObject;
import OpenGL.Material;
import OpenGL.Mesh;
import OpenGL.Primitives.Cube;
import OpenGL.Primitives.Sphere;
import OpenGL.Primitives.Square;
import OpenGL.Texture;
import Units.Angle;
import Units.Mass;
import Vector.RelVector;
import Vector.Vector;

import java.awt.*;

public class Terrain extends GameObject {
    final int precision;

    public Terrain (int precision, Material texture) {
        super(createMesh(precision), texture);
        this.precision = precision;
        init();
    }

    public Terrain (int precision, Color color) {
        super(createMesh(precision), color);
        this.precision = precision;
        init();
    }

    public Terrain (int precision, Texture texture) {
        super(createMesh(precision), texture);
        this.precision = precision;
        init();
    }

    public void updateNoise (long seed) {
        RelMatrix vertices = this.mesh.vertexMatrix();

        for (int i=0;i<precision;i++) {
            for (int j=0;j<precision;j++) {
                int pos = (i * precision) + j;

                RelVector vertex = vertices.get(pos);
                vertex.set(1, Rand.noise2D(i, j));
            }
        }

        Vector values = vertices.transposed().get(1);
        System.out.println(values);

        this.mesh.cleanup();
        this.mesh.draw();
    }

    private void init () {
        this.createBoxCollider();
        this.createRigidbody(new Mass(Float.MAX_VALUE));

        this.rb.cor = 0;
        this.rb.applyGravity = false;
    }

    private static Mesh createMesh (int points) {
        Mesh mesh = new Mesh(points * points, 2 * (points - 1) * (points - 1));

        float step = 2f / points;
        int k = 0;

        for (int i=0;i<points;i++) {
            for (int j=0;j<points;j++) {
                int pos = (i * points) + j;
                int up = (i * points) + j + 1;
                int left = ((i - 1) * points) + j;
                int down = (i * points) + j - 1;
                int right = ((i + 1) * points) + j;

                mesh.setVertex(pos, i * step, 1, j * step);

                if (i > 0 && j+1 < points) {
                    mesh.setTriangle(k, pos, up, left);
                    k++;
                }

                if (j > 0 && i+1 < points) {
                    mesh.setTriangle(k, pos, down, right);
                    k++;
                }
            }
        }

        mesh.calculateTexCoords();
        mesh.calculateNormals();
        mesh.draw();

        return mesh;
    }
}
