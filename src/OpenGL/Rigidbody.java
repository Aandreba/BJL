package OpenGL;

import Extras.Sys;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import Units.*;
import Vector.StatVector;
import Vector.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rigidbody {
    class Collision {
        GameObject a;
        GameObject b;

        public Collision(GameObject a, GameObject b) {
            this.a = a;
            this.b = b;
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

    final public static float G = 6.674e-11f;
    final public static double COR = 0.9f;
    final public static StatVector3 gravity = new StatVector3(0, -9.81, 0);

    // Earth
    final public static Temp temperature = new Temp(15, Temp.Type.Celsius);
    final public static Pressure pressure = new Pressure(1, Pressure.Type.Atmosphere);
    final public static double airDensity = airDensity(pressure, temperature);
    final public static ArrayList<Collision> collisions = new ArrayList<>();

    final public GameObject object;
    final public Mass mass;
    public float drag;
    public float angularDrag;
    public boolean applyGravity;
    public boolean isKinematic;
    public boolean isTerrain;
    public boolean isFreezeX, isFreezeY, isFreezeZ;

    final public StatVector3 velocity; // in m/s
    final public StatVector3 angularVelocity; // in rad/s
    final public Vector3 momentum;

    final private StatVector3 lastVelocity;
    final private StatVector3 lastAngularVelocity;

    public Rigidbody(GameObject object, Mass mass) {
        this.object = object;
        this.mass = mass;

        this.drag = 1;
        this.angularDrag = 0.5f;
        this.applyGravity = true;
        this.isKinematic = true;
        this.isTerrain = false;

        this.lastVelocity = StatVector3.zero.toStatic();
        this.lastAngularVelocity = StatVector3.zero.toStatic();

        this.velocity = StatVector3.zero.toStatic();
        this.angularVelocity = StatVector3.zero.toStatic();
        this.momentum = velocity.mul(mass.getValue());
    }

    public void setFreeze (boolean val) {
        this.isFreezeX = val;
        this.isFreezeY = val;
        this.isFreezeZ = val;
    }

    // Lineal
    public void setVelocity (double x, double y, double z) {
        this.velocity.set(0, x);
        this.velocity.set(1, y);
        this.velocity.set(2, z);
    }

    public void setVelocity (Vector3 v) {
        setVelocity(v.x(), v.y(), v.z());
    }

    public void setVelocity (StatVector3 v) {
        setVelocity(v.x(), v.y(), v.z());
    }

    public void addForce (Vector3 newton, Time delta) {
        this.addAcceleration(newton.div(mass.getValue()), delta);
    }

    public void addForce (StatVector3 newton, Time delta) {
        this.addAcceleration(newton.div(mass.getValue()), delta);
    }

    public void addAcceleration (Vector3 mss, Time delta) {
        this.addVelocity(mss.mul(delta.getValue()));
    }

    public void addAcceleration (StatVector3 mss, Time delta) {
        this.addVelocity(mss.mul(delta.getValue()));
    }

    public void addVelocity (Vector3 ms) {
        this.setVelocity(this.velocity.sum(ms));
    }

    public void addVelocity (StatVector3 ms) {
        this.setVelocity(this.velocity.sum(ms));
    }

    public void setLastVelocity (StatVector3 velocity) {
        this.lastVelocity.set(0, velocity.x());
        this.lastVelocity.set(1, velocity.y());
        this.lastVelocity.set(2, velocity.z());
    }

    // Angular
    public void setAngularVelocity(double x, double y, double z) {
        this.angularVelocity.set(0, x);
        this.angularVelocity.set(1, y);
        this.angularVelocity.set(2, z);
    }

    public void setAngularVelocity (Vector3 v) {
        setAngularVelocity(v.x(), v.y(), v.z());
    }

    public void setAngularVelocity (StatVector3 v) {
        setAngularVelocity(v.x(), v.y(), v.z());
    }

    public void addAngularForce (Vector3 newton, Time delta) {
        this.addAngularAcceleration(newton.div(mass.getValue()), delta);
    }

    public void addAngularForce (StatVector3 newton, Time delta) {
        this.addAngularAcceleration(newton.div(mass.getValue()), delta);
    }

    public void addAngularAcceleration (Vector3 radss, Time delta) {
        this.addAngularVelocity(radss.mul(delta.getValue()));
    }

    public void addAngularAcceleration (StatVector3 radss, Time delta) {
        this.addAngularVelocity(radss.mul(delta.getValue()));
    }

    public void addAngularVelocity (Vector3 rads) {
        this.setAngularVelocity(this.angularVelocity.sum(rads));
    }

    public void addAngularVelocity (StatVector3 rads) {
        this.setAngularVelocity(this.angularVelocity.sum(rads));
    }

    public void setLastAngularVelocity (StatVector3 velocity) {
        this.angularVelocity.set(0, velocity.x());
        this.angularVelocity.set(1, velocity.y());
        this.angularVelocity.set(2, velocity.z());
    }

    public void applyChange(GameObject object, Time delta) {
        object.transform.position.add(this.velocity.mul(delta.getValue()));
        object.transform.rotation.add(angularVelocity.mul(delta.getValue()));
    }

    public float getSpeed () {
        return (float) this.velocity.getSqrtMagnitude();
    }

    public float getAngularSpeed () {
        return (float) this.angularVelocity.getSqrtMagnitude();
    }

    public float getLastSpeed() {
        return (float) this.lastVelocity.getSqrtMagnitude();
    }

    protected double dragForce () {
        return 0.5f * airDensity * velocity.getMagnitude() * 6 * Math.pow(2 * object.transform.scale.getMean(), 2) * drag;
    }

    protected double angularDragForce () {
        return 0.5f * airDensity * velocity.getMagnitude() * 6 * Math.pow(2 * object.transform.scale.getMean(), 2) * angularDrag;
    }

    public void update (Window window, Time delta) {
        if (isKinematic) {
            Vector3 dir1 = lastVelocity.getNormalized();
            double m1 = mass.getValue();
            double v1i = getLastSpeed();
            double p1i = m1 * v1i;

            for (GameObject windowObj : window) {
                if (windowObj.collider == null || windowObj.rb == null || windowObj.equals(object)) {
                    continue;
                }

                Collision collision = new Collision(object, windowObj);
                if (collisions.contains(collision)) {
                    continue;
                }

                if (windowObj.collider.isCollidingWith(object.collider)) {
                    Rigidbody windowRb = windowObj.rb;
                    Vector3 dir2 = windowRb.lastVelocity.getNormalized();

                    double m2 = windowRb.mass.getValue();
                    double v2i = windowRb.getLastSpeed(); // Initial velocity #2
                    double p2i = m2 * v2i; // Initial momentum #2
                    double m1m2 = m1 + m2;

                    double angleCos = (p1i * dir1.x() + p2i * dir2.x()) / (p1i + p2i);
                    double angleSin = Math.sin(Math.acos(angleCos));

                    double angleZCos = (p1i * dir1.z() + p2i * dir2.z()) / (p1i + p2i);

                    double v1f = (COR * m2 * (v2i - v1i) + p1i + p2i)

                    System.out.println(angleCos * v1f+", "+angleSin * v1f+", "+angleZCos * v1f);
                    System.out.println(angleCos * v2f+", "+angleSin * v2f+", "+angleZCos * v2f);
                    System.out.println();

                    this.setVelocity(angleCos * v1f, angleSin * v1f, angleZCos * v1f);
                    windowRb.setVelocity(angleCos * v2f, angleSin * v2f, angleZCos * v2f);
                }

                collisions.add(collision);
            }
        }

        this.addForce(velocity.getNormalized().mul(-dragForce()), delta);
        this.addAngularForce(angularVelocity.getNormalized().mul(-angularDragForce()), delta);

        if (isFreezeX) {
            this.velocity.set(0, 0);
        }

        if (isFreezeY) {
            this.velocity.set(1, 0);
        }

        if (isFreezeZ) {
            this.velocity.set(2, 0);
        }
    }

    public static double airDensity (Pressure pressure, Temp temperature) {
        return pressure.getValue(Pressure.Type.Pascal) / (287.058 * temperature.getValue(Temp.Type.Kelvin));
    }
}
