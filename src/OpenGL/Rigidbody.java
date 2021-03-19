package OpenGL;

import Extras.Mathf;
import Extras.Sys;
import Matrix.Matrix;
import OpenGL.Extras.Matrix.Matrix4;
import OpenGL.Extras.Matrix.StatMatrix4;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import Units.Angle;
import Units.Mass;
import Units.Time;
import Vector.Vector;

public class Rigidbody {
    public GameObject gameObject;
    public Mass mass;
    public StatVector3 velocity;
    //public boolean useGravity;
    public boolean detectCollisions;

    public Rigidbody(GameObject gameObject, Mass mass) {
        this.gameObject = gameObject;
        this.mass = mass;
        this.velocity = new StatVector3();
        //this.useGravity = true;
        this.detectCollisions = true;
    }

    private Matrix getMatrix () {
        Matrix mat4 = new Matrix(gameObject.mesh.getVertexCount(), 4) {
            @Override
            public double get(int row, int col) {
                if (col == 3) {
                    return 1;
                }

                return gameObject.mesh.getVertex(row).get(col);
            }
        };

        return new Matrix (gameObject.mesh.getVertexCount(),4) {
            @Override
            public double get(int row, int col) {
                Vector pos = mat4.get(row);
                Matrix mul = gameObject.transform.getMatrix().mul(pos.toMatrix(1));
                return mul.toVector().get(col);
            }
        };
    }

    public void addForce (Vector3 force) {
        this.velocity.add(force.div(mass.getValue()));
    }

    public void addAcceleration (Vector3 vel) {
        this.velocity.add(vel);
    }

    public void update (Time delta) {
        gameObject.transform.position.add(this.velocity.mul(delta.getValue()));
    }

    public double speed () {
        return velocity.getSqrtMagnitude();
    }
}
