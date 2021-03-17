package OpenGL.Extras.Gravity;

import OpenGL.Rigidbody;
import Units.Time;
import java.util.ArrayList;

public abstract class Gravity extends ArrayList<Rigidbody> {
    public abstract void update(Rigidbody rb, Time delta);

    public void update(Time delta) {
        for (Rigidbody rb: this) {
            update(rb, delta);
        }

        for (Rigidbody rb: this) {
            rb.update(delta);
        }
    }
}
