package OpenGL.Extras.Gravity;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.GameObject;
import OpenGL.Window;

public class Newton implements Gravity {
    final public static float G = 6.674e-11f;
    //final public static float G = 1f;
    final public Window window;

    public Newton (Window window) {
        this.window = window;
    }

    @Override
    public StatVector3 gravityAccFor (GameObject object) {
        Vector3 force = new Vector3() {
            @Override
            public double get(int pos) {
                return 0;
            }
        };

        for (GameObject obj2: window) {
            if (obj2.equals(object)) {
                continue;
            }

            Vector3 dist = obj2.transform.position.subtr(object.transform.position);
            Vector3 dir = dist.getNormalized();

            double m2 = obj2.rb.mass.getValue();
            double r2 = dist.getMagnitude();

            force = force.sum(dir.mul(-G * m2 / r2));
        }

        return force.toStatic();
    }
}
