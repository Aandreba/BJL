package OpenGL.Extras.Gravity;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.GameObject;
import OpenGL.Rigidbody;
import Units.Time;

public class Normal extends Gravity {
    public StatVector3 acc = new StatVector3(0, -9.81f, 0);

    public Normal(GameObject... obj) {
        super();
    }

    @Override
    public void update(Rigidbody rb, Time delta) {
        rb.addAcceleration(acc.mul(delta.getValue()));
    }
}
