package OpenGL.Primitives.Objects;

import Extras.Rand;
import OpenGL.GameObject;
import OpenGL.Material;
import OpenGL.Mesh;
import OpenGL.Primitives.Square;
import OpenGL.Texture;
import Units.Angle;
import Units.Mass;
import Vector.RelVector;

import java.awt.*;

public class Terrain extends GameObject {
    final int precision;

    public Terrain (int precision, Material texture) {
        super(new Square(precision), texture);
        this.precision = precision;
        init();
    }

    public Terrain (int precision, Color color) {
        super(new Square(precision), color);
        this.precision = precision;
        init();
    }

    public Terrain (int precision, Texture texture) {
        super(new Square(precision), texture);
        this.precision = precision;
        init();
    }

    public void updateNoise () {
        for (int i=0;i<precision;i++) {
            for (int j=0;j<precision;j++) {
                int pos = (i * precision) + j;

                RelVector vertex = this.mesh.vertexMatrix().get(pos);
                /*System.out.println(Rand.noise2D(i, j));
                vertex.set(2, Rand.noise2D(i, j));*/
            }
        }

        this.mesh.cleanup();
        this.mesh.draw();
    }

    private void init () {
        //this.transform.setRotationZ(new Angle(-90, Angle.Type.Degrees));
        this.createBoxCollider();
        this.createRigidbody(new Mass(0));

        this.rb.cor = 0;
        this.rb.applyGravity = false;

    }
}
