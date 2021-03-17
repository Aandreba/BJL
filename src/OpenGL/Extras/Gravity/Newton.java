package OpenGL.Extras.Gravity;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.GameObject;
import OpenGL.Rigidbody;
import Units.Mass;
import Units.Time;
import Vector.StatVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Newton extends Gravity {
    final public float G = 1e-2f;

    public Newton (Rigidbody... obj) {
        super();
        Collections.addAll(this, obj);
    }

    @Override
    public void update(Rigidbody rb, Time delta) {
        for (Rigidbody target: this) {
            if (rb.equals(target)) {
                continue;
            }

            Vector3 dist = rb.gameObject.transform.position.subtr(target.gameObject.transform.position);
            Vector3 acc = dist.getNormalized().mul(-G * target.mass.getValue()).div(dist.getMagnitude());
            rb.addAcceleration(acc.mul(delta.getValue()));
        }
    }
}
