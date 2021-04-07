package OpenGL.Colliders;

import Matrix.Matrix;
import Matrix.VectorMatrix;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.GameObject;
import OpenGL.Rigidbody;
import Units.Time;
import Vector.StatVector;
import Vector.Vector;

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

    private Matrix calculate1DPoint (int pos, StatVector3 p0, StatVector3 n0, Vector3 v1i, Vector3 v2i) {
        double m1m2 = m1 + m2;

        double u1 = v1i.get(pos);
        double u2 = v2i.get(pos);
        double pi = p0.get(pos);

        double v1 = (m2 * (u2 - u1) + pi) / m1m2;
        double v2 = (m1 * (u1 - u2) + pi) / m1m2;

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

    private Matrix calculate2DPoint (int xPos, int yPos, StatVector3 p0, StatVector3 n0, Vector3 v1i, Vector3 v2i) {
        StatVector3 direction = new StatVector3(n0.x(), -n0.y(), n0.z());
        double sum = n0.abs().get(xPos) + n0.abs().get(yPos);

        if (n0.abs().get(xPos) == sum) {
            return calculate1DPoint(xPos, p0, n0, v1i, v2i);
        } else if (n0.abs().get(yPos) == sum) {
            return calculate1DPoint(yPos, p0, n0, v1i, v2i);
        }

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
                int pos = col == xPos ? 0 : (col == yPos ? 1 : 2);
                if (row == 0) {
                    return v1.get(pos);
                }

                return v2.get(pos);
            }
        };
    }

    public void calculateCollision (Time delta) {
        double sec = delta.getValue();

        Vector3 d1 = x1.subtr(collisionPoint);
        Vector3 v1i = this.v1i.sum(av1i.cross(d1));
        Vector3 p1i = v1i.mul(m1);

        Vector3 d2 = x2.subtr(collisionPoint);
        Vector3 v2i = this.v2i.sum(av2i.cross(d2));
        Vector3 p2i = v2i.mul(m2);

        StatVector3 p0 = p1i.sum(p2i).toStatic(); // P1 initial if P2 static
        StatVector3 n0 = p0.getNormalized().toStatic();

        Matrix vel1 = calculate2DPoint(0, 1, p0, n0, v1i, v2i);
        Matrix vel2 = calculate2DPoint(2, 1, p0, n0, v1i, v2i);

        Matrix vel = new Matrix(2, 3) {
            @Override
            public double get(int row, int col) {
                return Math.max(vel1.get(row, col), vel2.get(row, col));
            }
        }.round(5);

        Matrix impulse = new Matrix(2, 3) {
            @Override
            public double get(int row, int col) {
                if (row == 0) {
                    return (vel.get(row, col) - v1i.get(col)) * m1;
                }

                return (vel.get(row, col) - v2i.get(col)) * m2;
            }
        };

        System.out.println(Vector3.from(impulse.get(0))+", "+Vector3.from(impulse.get(1)));
        System.out.println(d1.cross(Vector3.from(impulse.get(0))).div(rb1.getMomentOfInertia())+", "+d2.cross(Vector3.from(impulse.get(1))));

        rb1.addImpulse(Vector3.from(impulse.get(0)), delta);
        rb2.addImpulse(Vector3.from(impulse.get(1)), delta);

        rb1.addAngularImpulse(d1.cross(Vector3.from(impulse.get(0))), delta);
        rb2.addAngularImpulse(d2.cross(Vector3.from(impulse.get(1))), delta);

        /*rb1.setAngularVelocity(Vector3.from(angular.get(0)));
        rb2.setAngularVelocity(Vector3.from(angular.get(1)));*/
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
