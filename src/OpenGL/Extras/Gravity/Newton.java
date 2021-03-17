package OpenGL.Extras.Gravity;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.GameObject;
import Units.Mass;
import Units.Time;
import Vector.StatVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Newton extends ArrayList<GameObject> {
    final public float G = 1e-2f;
    final public HashMap<GameObject, Mass> mass;
    final public HashMap<GameObject, StatVector3> velocity;

    public Newton (GameObject... obj) {
        super();
        this.mass = new HashMap<>();
        this.velocity = new HashMap<>();
        Collections.addAll(this, obj);
    }

    @Override
    public boolean add(GameObject gameObject) {
        this.mass.put(gameObject, new Mass(1));
        this.velocity.put(gameObject, new StatVector3());
        return super.add(gameObject);
    }

    public void setMass (GameObject obj, Mass mass) {
        this.mass.put(obj, mass);
    }

    public void setVelocity (GameObject obj, Vector3 vel) {
        this.velocity.put(obj, vel.toStatic());
    }

    public void updateVelocity (Time delta) {
        for (GameObject obj: this) {
            StatVector3 vel = this.velocity.get(obj);

            for (GameObject target: this) {
                if (obj.equals(target)) {
                    continue;
                }

                Vector3 dist = obj.transform.position.subtr(target.transform.position);
                Vector3 acc = dist.getNormalized().mul(-G * this.mass.get(target).getValue()).div(dist.getMagnitude());
                vel.add(acc.mul(delta.getValue()));
            }
        }
    }

    public void update (Time delta) {
        updateVelocity(delta);
        for (GameObject obj: this) {
            obj.transform.position.add(this.velocity.get(obj).mul(delta.getValue()));
        }
    }
}
