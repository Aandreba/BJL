package OpenGL.Colliders;

import Matrix.Matrix;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.GameObject;
import OpenGL.Rigidbody;
import Vector.StatVector;

public class Collision {
    final public static double COR = 0.9f;

    final public GameObject a, b;
    final private Rigidbody rb1, rb2;
    final public boolean areColliding;
    final public StatVector3 collisionPoint;

    final private double m1, m2; // Masses
    final private StatVector3 x1, x2; // Initial center of mass
    final private StatVector3 r1, r2; // Initial rotation
    final private StatVector3 s1, s2; // Initial scale
    final private StatVector3 v1i, v2i; // Initial velocity
    final private StatVector3 av1i, av2i; // Initial angular velocity
    final private StatVector3 p1i, p2i, pi; // Initial momentum

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

            r1 = null;
            r2 = null;

            s1 = null;
            s2 = null;

            v1i = null;
            v2i = null;

            av1i = null;
            av2i = null;

            p1i = null;
            p2i = null;
            pi = null;
            return;
        }

        collisionPoint = a.collider.pointOfCollisionWith(b.collider);

        rb1 = a.rb;
        rb2 = b.rb;

        m1 = rb1.mass.getValue();
        m2 = rb2.mass.getValue();

        x1 = a.transform.position;
        x2 = b.transform.position;

        r1 = a.transform.rotation;
        r2 = b.transform.rotation;

        s1 = a.transform.scale;
        s2 = b.transform.scale;

        v1i = rb1.lastVelocity;
        v2i = rb2.lastVelocity;

        av1i = rb1.lastAngularVelocity;
        av2i = rb2.lastAngularVelocity;

        p1i = v1i.mul(m1).toStatic();
        p2i = v2i.mul(m2).toStatic();
        pi = p1i.sum(p2i).toStatic();
    }

    /**
     * Calculates velocity at certain part ob object #1
     * @param point Point of velocity
     * @return Velocity at specified position
     */
    public Vector3 v1At (StatVector3 point) {
        StatVector3 dist = x1.subtr(point).toStatic(); // Distance from centre of mass
        return v1i.sum(av1i.cross(dist));
    }

    /**
     * Calculates velocity at certain part ob object #2
     * @param point Point of velocity
     * @return Velocity at specified position
     */
    public Vector3 v2At (StatVector3 point) {
        StatVector3 dist = x2.subtr(point).toStatic(); // Distance from centre of mass
        return v2i.sum(av2i.cross(dist));
    }

    private Matrix calculate1D (int pos) {
        double m1m2 = m1 + m2;

        double u1 = v1i.get(pos);
        double u2 = v2i.get(pos);
        double p1 = p1i.get(pos);
        double p2 = p2i.get(pos);

        double v1 = (COR * m2 * (u2 - u1) + p1 + p2) / m1m2;
        double v2 = (COR * m1 * (u1 - u2) + p1 + p2) / m1m2;

        return new Matrix (2, 3) {
            @Override
            public double get(int row, int col) {
                if (row == 0 && col == pos) {
                    return v1;
                } else if (row == 1 && col == pos) {
                    return v2;
                }

                return 0;
            }
        };
    }

    private Matrix calculate2D () {
        StatVector3 p0 = pi; // P1 initial if P2 static
        StatVector3 n0 = p0.getNormalized().toStatic();

        if (n0.abs().x() == 1) {
            return calculate1D(0);
        } else if (n0.abs().y() == 1) {
            return calculate1D(1);
        } else if (n0.abs().z() == 1) {
            return calculate1D(2);
        }

        StatVector3 direction = new StatVector3(n0.x(), -n0.y(), n0.z());

        double phi = Math.acos(n0.x());
        double beta = Math.acos(direction.x());
        double alpha = Math.PI - phi - beta;

        double cosA = Math.cos(alpha);
        double tanA = Math.tan(alpha);
        double cosB = direction.x();
        double sinB = direction.y();

        double v2f = (p0.x() - p0.y() * tanA) / (m2 * (sinB * tanA + cosB));
        double v1f = (p0.x() - m2 * v2f * cosB) / (m1 * cosA);

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

    public void calculateCollision () {
        Matrix vel = calculate2D().round(10);
        System.out.println(vel);

        rb1.setVelocity(vel.get(0).get(0), vel.get(0).get(1), vel.get(0).get(2));
        rb2.setVelocity(vel.get(1).get(0), vel.get(1).get(1), vel.get(1).get(2));
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
