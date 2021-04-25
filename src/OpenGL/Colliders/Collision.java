package OpenGL.Colliders;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.GameObject;
import OpenGL.Rigidbody;
import Units.Time;

public class Collision {
    final public GameObject a, b;
    final private Rigidbody rb1, rb2;
    final public boolean areColliding;
    final public StatVector3 collisionPoint;

    final private double m1, m2; // Masses
    final private StatVector3 x1, x2; // Initial center of mass
    final private StatVector3 v1i, v2i; // Initial velocity
    final private StatVector3 av1i, av2i; // Initial angular velocity
    final private double cor1, cor2;

    public Collision(GameObject a, GameObject b) {
        this.a = a;
        this.b = b;
        this.areColliding = a.collider.isCollidingWith(b.collider);

        if (!areColliding) {
            collisionPoint = null;

            rb1 = null;
            rb2 = null;

            m1 = 0;
            m2 = 0;

            x1 = null;
            x2 = null;

            v1i = null;
            v2i = null;

            av1i = null;
            av2i = null;

            cor1 = 0;
            cor2 = 0;

            return;
        }

        collisionPoint = a.collider.pointOfCollisionWith(b.collider);

        rb1 = a.rb;
        rb2 = b.rb;

        m1 = rb1.mass.getValue();
        m2 = rb2.mass.getValue();

        x1 = a.transform.position;
        x2 = b.transform.position;

        v1i = rb1.lastVelocity;
        v2i = rb2.lastVelocity;

        av1i = rb1.lastAngularVelocity;
        av2i = rb2.lastAngularVelocity;

        cor1 = rb1.cor;
        cor2 = rb2.cor;
    }

    public StatVector3[] calculate3D (Vector3 n, StatVector3 p1, StatVector3 p2, Time delta) {
        double p1p2 = p1.getMagnitude();
        double p2p2 = p2.getMagnitude();

        double p1m = Math.sqrt(p1p2);
        double p2m = Math.sqrt(p2p2);

        double ke1 = p1p2 / m1;
        double ke2 = p2p2 / m2;
        double ke0 = (cor1 * cor1 * ke1) + (cor2 * cor2 * ke2);

        double k = (ke0 * m1 * m2) - (m2 * p1p2) - (m1 * p2p2);
        double force = Math.sqrt(m1 * m1 * p2p2 - 2 * m1 * m2 * p1m * p2m + m2 * m2 * p1p2 + k * m2 + k * m1);
        force += m1 * p2m - m2 * p1m;
        force /= (m1 + m2) * delta.getValue();

        Vector3 f1 = n.mul(force);
        Vector3 f2 = n.mul(-force);

        return new StatVector3[] { f1.round(5).toStatic(), f2.round(5).toStatic() };
    }

    public void calculateCollision (Time delta) {
        Vector3 d1 = collisionPoint.subtr(x1);
        StatVector3 v1i = this.v1i.sum(av1i.cross(d1)).toStatic();
        StatVector3 p1i = v1i.mul(m1).toStatic();

        Vector3 d2 = collisionPoint.subtr(x2);
        StatVector3 v2i = this.v2i.sum(av2i.cross(d2)).toStatic();
        StatVector3 p2i = v2i.mul(m2).toStatic();

        Vector3 p0 = p1i.sum(p2i);
        StatVector3[] result = calculate3D(p0.getNormalized(), p1i, p2i, delta);

        Vector3 velocity1 = v1i.sum(result[0].mul(delta.getValue() * delta.getValue()));
        Vector3 velocity2 = v2i.sum(result[1].mul(delta.getValue() * delta.getValue()));

        rb1.addForce(result[0], delta);
        rb2.addForce(result[1], delta);

        /*rb1.addTorque(d1.cross(result[0]), d1, delta);
        rb2.addTorque(d2.cross(result[1]), d2, delta);*/
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collision collision = (Collision) o;
        return (a.equals(collision.a) || a.equals(collision.b)) &&
                (b.equals(collision.b) || b.equals(collision.a));
    }
}
