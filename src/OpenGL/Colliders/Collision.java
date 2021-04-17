package OpenGL.Colliders;

import Matrix.Matrix;
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
    final private StatVector3 p1i, p2i, pi; // Initial momentum
    final private double cor1, cor2;
    final private double ke1i, ke2i; // Initial kinetic energy (2x)

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

            p1i = null;
            p2i = null;
            pi = null;

            ke1i = 0;
            ke2i = 0;
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

        p1i = v1i.mul(m1).toStatic();
        p2i = v2i.mul(m2).toStatic();
        pi = p1i.sum(p2i).toStatic();

        ke1i = m1 * v1i.getMagnitude();
        ke2i = m2 * v2i.getMagnitude();
    }

    /**
     * Calculates velocity at certain part of object #1
     * @param point Point of velocity
     * @return Velocity at specified position
     */
    public Vector3 v1At (StatVector3 point) {
        StatVector3 dist = x1.subtr(point).toStatic(); // Distance from centre of mass
        return v1i.sum(av1i.cross(dist));
    }

    /**
     * Calculates velocity at certain part of object #2
     * @param point Point of velocity
     * @return Velocity at specified position
     */
    public Vector3 v2At (StatVector3 point) {
        StatVector3 dist = x2.subtr(point).toStatic(); // Distance from centre of mass
        return v2i.sum(av2i.cross(dist));
    }

    @Deprecated
    private Matrix calculate2D (int xPos, int yPos) {
        StatVector3 p0 = pi; // P1 initial if P2 static
        StatVector3 n0 = p0.getNormalized().toStatic();

        StatVector3 direction = new StatVector3(n0.x(), -n0.y(), n0.z());

        double phi = Math.acos(n0.get(xPos));
        double beta = Math.acos(direction.get(xPos));
        double alpha = Math.PI - phi - beta;

        double cosA = Math.cos(alpha);
        double tanA = Math.tan(alpha);
        double cosB = direction.get(xPos);
        double sinB = direction.get(yPos);

        double v2f = (p0.get(xPos) - p0.get(yPos) * tanA) / (m2 * (sinB * tanA + cosB));
        double v1f = (p0.get(xPos) - m2 * v2f * cosB) / (m1 * cosA);

        Vector3 v1 = new StatVector3(cosA, Math.sin(alpha), 0).mul(v1f);
        Vector3 v2 = new StatVector3(cosB, sinB, 0).mul(v2f);

        return new Matrix (2, 3) {
            @Override
            public double get(int row, int col) {
                if (row == 0) {
                    return v1.get(col);
                }

                return v2.get(col);
            }
        };
    }

    public StatVector3[] calculate3D (Vector3 p0) {
        StatVector3 d = x2.subtr(x1).getNormalized().toStatic(); // Direction #2
        StatVector3 n = p0.getNormalized().subtr(d).getNormalized().toStatic(); // Direction #1

        StatVector3 n2 = n.pow(2).toStatic();
        StatVector3 d2 = d.pow(2).toStatic();

        Vector3 phi = n2.mul(m1 * (cor1 * ke1i + cor2 * ke2i)).subtr(p0).div(m2);
        StatVector3 alpha = n2.mul(m1).sum(d2.mul(m2)).toStatic();

        StatVector3 v2 = d.mul(alpha.mul(phi).sum(d2.mul(p0.pow(2))).pow(1/2d)).sum(d2.mul(p0)).div(alpha).toStatic();
        for (int i=0;i<3;i++) {
            if (Double.isNaN(v2.get(i))) {
                v2.set(i, 0);
            }
        }

        StatVector3 v1 = p0.subtr(v2.mul(m2)).div(m1).toStatic();
        return new StatVector3[] { v1, v2 };
    }

    public void calculateCollision (Time delta) {
        Vector3 d1 = collisionPoint.subtr(x1);
        StatVector3 v1i = this.v1i.sum(av1i.cross(d1)).toStatic();
        Vector3 p1i = v1i.mul(m1);

        Vector3 d2 = collisionPoint.subtr(x2);
        StatVector3 v2i = this.v2i.sum(av2i.cross(d2)).toStatic();
        Vector3 p2i = v2i.mul(m2);

        Vector3 p0 = p1i.sum(p2i);
        StatVector3[] result = calculate3D(p0);

        rb1.setAngularForce(result[0].mul(-m1), d1, delta);
        rb2.setAngularForce(result[1].mul(-m2), d2, delta);

        StatVector3 rv1f = rb1.angularVelocity.cross(d1).toStatic();
        StatVector3 rv2f = rb2.angularVelocity.cross(d2).toStatic();

        rb1.setVelocity(result[0].subtr(rv1f));
        rb2.setVelocity(result[1].subtr(rv2f));
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
