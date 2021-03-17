package OpenGL;

import Matrix.Matrix;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
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

    private Matrix vertexPos () {
        return gameObject.mesh.getMatrix().mulGPU(gameObject.transform.getMatrix());
    }

    public void addForce (Vector3 force) {
        this.velocity.add(force.div(mass.getValue()));
    }

    public void addAcceleration (Vector3 vel) {
        this.velocity.add(vel);
    }

    public boolean isCollidingWith (Rigidbody rb) {
        // TODO
        Matrix posA = vertexPos();
        Matrix posB = rb.vertexPos();

        for (int i=0;i<posA.getRows();i++) {
            Vector vertexA = posA.get(i);
            for (int j=0;j<posB.getRows();j++) {
                Vector vertexB = posB.get(j);
                boolean bIsFurther = this.gameObject.transform.position.getMagnitude() < rb.gameObject.transform.position.getMagnitude();
                boolean bVertexIsFurther = vertexA.getMagnitude() < vertexB.getMagnitude();
                if (!bIsFurther && bVertexIsFurther) {
                    return true;
                }
            }
        }

        return false;
    }

    public void update (Time delta) {
        gameObject.transform.position.add(this.velocity.mul(delta.getValue()));
    }

    public double speed () {
        return velocity.getSqrtMagnitude();
    }
}
