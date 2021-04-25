package OpenGL;

import OpenGL.Colliders.Collision;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import java.util.ArrayList;

import Units.*;

public class Rigidbody {
    // Earth
    final public static ArrayList<Collision> collisions = new ArrayList<>();

    final public GameObject object;
    final public Mass mass;
    public float drag;
    public float angularDrag;
    public float cor;
    public boolean applyGravity;
    public boolean isKinematic;
    public boolean isTerrain;
    public boolean isFreezeX, isFreezeY, isFreezeZ;

    final public StatVector3 velocity; // in m/s
    final public StatVector3 angularVelocity; // in rad/s
    final public Vector3 momentum;

    final public StatVector3 lastVelocity;
    final public StatVector3 lastAngularVelocity;
    final public StatVector3 acceleration;
    final public StatVector3 angularAcceleration;

    public Rigidbody(GameObject object, Mass mass) {
        this.object = object;
        this.mass = mass;

        this.drag = 0f;
        this.angularDrag = 0;
        this.cor = 1;
        this.applyGravity = true;
        this.isKinematic = true;
        this.isTerrain = false;

        this.lastVelocity = StatVector3.zero.toStatic();
        this.lastAngularVelocity = StatVector3.zero.toStatic();
        this.acceleration = StatVector3.zero.toStatic();
        this.angularAcceleration = StatVector3.zero.toStatic();

        this.velocity = StatVector3.zero.toStatic();
        this.angularVelocity = StatVector3.zero.toStatic();
        this.momentum = velocity.mul(mass.getValue());
    }

    public void setFreeze (boolean val) {
        this.isFreezeX = val;
        this.isFreezeY = val;
        this.isFreezeZ = val;
    }

    public double getMomentOfInertia (Vector3 relPos) {
        return mass.getValue() * relPos.getMagnitude();
    }

    public double getMomentOfInertia (StatVector3 relPos) {
        return mass.getValue() * relPos.getMagnitude();
    }

    public Vector3 getAngularMomentum (Vector3 relPos) {
        return angularVelocity.mul(getMomentOfInertia(relPos));
    }

    public Vector3 getAngularMomentum (StatVector3 relPos) {
        return angularVelocity.mul(getMomentOfInertia(relPos));
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
    public void setTorque (Vector3 torque, Vector3 relPos, Time delta) {
        this.setAngularAcceleration(torque.div(getMomentOfInertia(relPos)).forEachValue(x -> Double.isNaN(x) || Double.isInfinite(x) ? 0 : x), delta);
    }

    public void setTorque (StatVector3 torque, Vector3 relPos, Time delta) {
        this.setAngularAcceleration(torque.div(getMomentOfInertia(relPos)).forEachValue(x -> Double.isNaN(x) || Double.isInfinite(x) ? 0 : x), delta);
    }

    public void setTorque (Vector3 torque, StatVector3 relPos, Time delta) {
        this.setAngularAcceleration(torque.div(getMomentOfInertia(relPos)).forEachValue(x -> Double.isNaN(x) || Double.isInfinite(x) ? 0 : x), delta);
    }

    public void setTorque (StatVector3 torque, StatVector3 relPos, Time delta) {
        this.setAngularAcceleration(torque.div(getMomentOfInertia(relPos)).forEachValue(x -> Double.isNaN(x) || Double.isInfinite(x) ? 0 : x), delta);
    }

    public void setAngularForce (Vector3 newton, Vector3 relPos, Time delta) {
        this.setTorque(relPos.cross(newton), relPos, delta);
    }

    public void setAngularForce (StatVector3 newton, Vector3 relPos, Time delta) {
        this.setTorque(relPos.cross(newton), relPos, delta);
    }

    public void setAngularForce (Vector3 newton, StatVector3 relPos, Time delta) {
        this.setTorque(relPos.cross(newton), relPos, delta);
    }

    public void setAngularForce (StatVector3 newton, StatVector3 relPos, Time delta) {
        this.setTorque(relPos.cross(newton), relPos, delta);
    }

    public void setAngularAcceleration (Vector3 radss, Time delta) {
        this.setAngularVelocity(radss.mul(delta.getValue()));
    }

    public void setAngularAcceleration (StatVector3 radss, Time delta) {
        this.setAngularVelocity(radss.mul(delta.getValue()));
    }

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

    public void addTorque (Vector3 torque, Vector3 relPos, Time delta) {
        this.addAngularAcceleration(torque.div(getMomentOfInertia(relPos)).forEachValue(x -> Double.isNaN(x) || Double.isInfinite(x) ? 0 : x), delta);
    }

    public void addTorque (StatVector3 torque, Vector3 relPos, Time delta) {
        this.addAngularAcceleration(torque.div(getMomentOfInertia(relPos)).forEachValue(x -> Double.isNaN(x) || Double.isInfinite(x) ? 0 : x), delta);
    }

    public void addTorque (Vector3 torque, StatVector3 relPos, Time delta) {
        this.addAngularAcceleration(torque.div(getMomentOfInertia(relPos)).forEachValue(x -> Double.isNaN(x) || Double.isInfinite(x) ? 0 : x), delta);
    }

    public void addTorque (StatVector3 torque, StatVector3 relPos, Time delta) {
        this.addAngularAcceleration(torque.div(getMomentOfInertia(relPos)).forEachValue(x -> Double.isNaN(x) || Double.isInfinite(x) ? 0 : x), delta);
    }

    public void addAngularForce (Vector3 newton, Vector3 relPos, Time delta) {
        this.addTorque(relPos.cross(newton), relPos, delta);
    }

    public void addAngularForce (StatVector3 newton, Vector3 relPos, Time delta) {
        this.addTorque(relPos.cross(newton), relPos, delta);
    }

    public void addAngularForce (Vector3 newton, StatVector3 relPos, Time delta) {
        this.addTorque(relPos.cross(newton), relPos, delta);
    }

    public void addAngularForce (StatVector3 newton, StatVector3 relPos, Time delta) {
        this.addTorque(relPos.cross(newton), relPos, delta);
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
                    collision.calculateCollision(delta);
                }

                collisions.add(collision);
            }
        }

        this.addAcceleration(velocity.mul(-drag), delta);
        this.addAngularAcceleration(angularVelocity.mul(-angularDrag), delta);

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
}
