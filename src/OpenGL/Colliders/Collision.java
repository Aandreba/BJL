package OpenGL.Colliders;

import Matrix.Matrix;
import OpenGL.Extras.Vector.StatVector2;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector2;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.GameObject;
import OpenGL.Rigidbody;
import Units.Time;
import Vector.Vector;

import java.util.Arrays;

public class Collision {
    final public static double COR = 1f;

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
    final private double ke1i, ke2i, kei; // Initial kinetic energy (2x)

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

            ke1i = 0;
            ke2i = 0;
            kei = 0;
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

        ke1i = m1 * v1i.getMagnitude();
        ke2i = m2 * v2i.getMagnitude();
        kei = ke1i + ke2i;
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

        double v1 = (m2 * (u2 - u1) + p1 + p2) / m1m2;
        double v2 = (m1 * (u1 - u2) + p1 + p2) / m1m2;

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

    private Matrix calculate2D (int xPos, int yPos) {
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

    public void calculate3D (Vector3 p0) {
        StatVector3 d = x2.subtr(collisionPoint).getNormalized().toStatic(); // Direction #2
        StatVector3 n = p0.getNormalized().subtr(d).getNormalized().toStatic(); // Direction #1
        //System.out.println(n+", "+d);
        //System.exit(1);

        StatVector3 n2 = n.pow(2).toStatic();
        StatVector3 d2 = d.pow(2).toStatic();

        StatVector3 phi = n2.mul(m1 * COR * kei).subtr(p0).div(m2).toStatic();
        StatVector3 alpha = n2.mul(m1).sum(d2.mul(m2)).toStatic();

        StatVector3 v2 = d.mul(alpha.mul(phi).sum(d2.mul(p0.pow(2))).pow(1/2d)).sum(d2.mul(p0)).div(alpha).toStatic();
        for (int i=0;i<3;i++) {
            if (Double.isNaN(v2.get(i))) {
                v2.set(i, 0);
            }
        }

        StatVector3 v1 = p0.subtr(v2.mul(m2)).div(m1).toStatic();
        rb1.setVelocity(v1.round(5));
        rb2.setVelocity(v2.round(5));

        System.out.println(n+", "+d);
        System.out.println(v1+", "+v2);
    }

    public StatVector3 calculateAngular (StatVector3 w, Vector3 d, StatVector3 scale) {
        /*
            rad --> (x, y)

            0 --> (0, 1)
            pi/2 --> (-1, 0)
            pi --> (0, -1)
            3pi/4 --> (1, 0)

            rad --> (-sin, cos)
         */
        StatVector3 norm = d.getNormalized().toStatic();

        StatVector2 nx = new StatVector2(-norm.z(), Math.sin(Math.acos(-norm.z()))).mul(w.x()).div(scale.zy()).toStatic(); // X axis [Z, Y] (-w)
        StatVector2 ny = new StatVector2(-Math.sin(Math.acos(-norm.x())), -norm.x()); // Y axis [X, Z] (-w)
        Vector2 nz = ny.mul(w.z()).div(scale.xy()); // Z axis [X, Y] (-w)
        ny = ny.mul(w.y()).div(scale.xz()).toStatic();

        StatVector3 n = new StatVector3(ny.x() + nz.x(), nx.y() + nz.y(), nx.x() + ny.y());
        return n;
    }

    public void calculateCollision (Time delta) {
        Vector3 d1 = collisionPoint.subtr(x1);
        StatVector3 v1i = this.v1i.sum(calculateAngular(av1i, d1, s1)).toStatic();
        Vector3 p1i = v1i.mul(m1);

        Vector3 d2 = collisionPoint.subtr(x2);
        StatVector3 v2i = this.v2i.sum(calculateAngular(av2i, d2, s2)).toStatic();
        Vector3 p2i = v2i.mul(m2);

        Vector3 p0 = p1i.sum(p2i);

        System.out.println(this.v1i+", "+this.v2i);
        System.out.println(v1i+", "+v2i);
        //System.exit(1);

        calculate3D(p0);
        System.exit(1); // TODO
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
