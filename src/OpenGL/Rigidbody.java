package OpenGL;

import OpenGL.Colliders.Collision;
import OpenGL.Extras.Gravity.Gravity;
import OpenGL.Extras.Gravity.Normal;
import OpenGL.Extras.Vector.StatVector2;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector2;
import OpenGL.Extras.Vector.Vector3;
import java.util.ArrayList;
import java.util.Vector;

import Units.*;

public class Rigidbody {
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
    final public Vector3 linearVelocity; // in m / s
    final public Vector3 momentum;

    final public StatVector3 lastVelocity;
    final public StatVector3 lastAngularVelocity;
    final public StatVector3 acceleration;
    final public StatVector3 angularAcceleration;

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
        this.acceleration = StatVector3.zero.toStatic();
        this.angularAcceleration = StatVector3.zero.toStatic();

        this.velocity = StatVector3.zero.toStatic();
        this.angularVelocity = StatVector3.zero.toStatic();

        this.linearVelocity = linearVelocity(angularVelocity, object.transform.rotation, object.transform.scale);
        this.momentum = velocity.mul(mass.getValue());
    }

    public void setFreeze (boolean val) {
        this.isFreezeX = val;
        this.isFreezeY = val;
        this.isFreezeZ = val;
    }

    public double getMomentOfInertia () {
        return mass.getValue() * object.transform.scale.getMagnitude();
    }

    public Vector3 getAngularMomentum () {
        return angularVelocity.mul(getMomentOfInertia());
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

    public void addImpulse (Vector3 ns, Time delta) {
        this.addForce(ns.div(delta.getValue()), delta);
    }

    public void addImpulse (StatVector3 ns, Time delta) {
        this.addForce(ns.div(delta.getValue()), delta);
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

    public void setLastVelocity (Time delta) {
        Vector3 acc = velocity.subtr(this.lastVelocity).div(delta.getValue());
        this.acceleration.set(0, acc.x());
        this.acceleration.set(1, acc.y());
        this.acceleration.set(2, acc.z());

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

    public void addAngularImpulse (Vector3 ns, Time delta) {
        this.addAngularForce(ns.div(delta.getValue()), delta);
    }

    public void addAngularImpulse (StatVector3 ns, Time delta) {
        this.addAngularForce(ns.div(delta.getValue()), delta);
    }

    public void addAngularForce (Vector3 newtons, Time delta) {
        this.addAngularAcceleration(newtons.div(getMomentOfInertia()), delta);
    }

    public void addAngularForce (StatVector3 newtons, Time delta) {
        this.addAngularAcceleration(newtons.div(getMomentOfInertia()), delta);
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

    public void setLastAngularVelocity (Time delta) {
        Vector3 acc = angularVelocity.subtr(this.lastAngularVelocity).div(delta.getValue());
        this.angularAcceleration.set(0, acc.x());
        this.angularAcceleration.set(1, acc.y());
        this.angularAcceleration.set(2, acc.z());

        this.lastAngularVelocity.set(0, angularVelocity.x());
        this.lastAngularVelocity.set(1, angularVelocity.y());
        this.lastAngularVelocity.set(2, angularVelocity.z());
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
            for (GameObject windowObj : window) {
                if (windowObj.collider == null || windowObj.rb == null || windowObj.equals(object)) {
                    continue;
                }

                Collision collision = new Collision(object, windowObj);
                if (collisions.contains(collision)) {
                    continue;
                }

                if (collision.areColliding) {
                    System.out.println(collision.a.tags+", "+collision.b.tags);
                    collision.calculateCollision(delta);
                    System.out.println();
                    //System.exit(1);
                }

                collisions.add(collision);
            }
        }

        this.addAcceleration(velocity.getNormalized().pow(2).mul(-drag), delta);
        this.addAngularAcceleration(angularVelocity.getNormalized().pow(2).mul(-angularDrag), delta);

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

    public static Vector3 linearVelocity (StatVector3 av, StatVector3 rotation, StatVector3 scale) {
        // Rotation on X axis (Z & Y position axis)
        Vector2 rotXDir = new Vector2() {
            @Override
            public double get(int pos) {
                if (pos == 0) {
                    return -Math.sin(rotation.x());
                }

                return Math.cos(rotation.x());
            }
        };

        Vector2 rotX = rotXDir.mul(av.x() * scale.z());

        // Rotation on Y axis (X & Z position axis)
        Vector2 rotYDir = new Vector2() {
            @Override
            public double get(int pos) {
                if (pos == 0) {
                    return Math.sin(rotation.y());
                }

                return Math.cos(rotation.y());
            }
        };

        Vector2 rotY = rotYDir.mul(av.y() * scale.x());

        // Rotation on Z axis (Y & X position axis)
        Vector2 rotZDir = new Vector2() {
            @Override
            public double get(int pos) {
                if (pos == 0) {
                    return Math.sin(rotation.z());
                }

                return Math.cos(rotation.z());
            }
        };

        Vector2 rotZ = rotZDir.mul(av.z() * scale.y());

        return new Vector3() {
            @Override
            public double get(int pos) {
                if (pos == 0) {
                    return rotY.x() + rotZ.y();
                } else if (pos == 1) {
                    return rotX.y() + rotZ.x();
                }

                return rotX.x() + rotY.y();
            }
        };
    }
}
