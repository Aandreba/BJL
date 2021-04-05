package OpenGL.Extras.Gravity;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.GameObject;

public class Normal implements Gravity {
    final public static StatVector3 gravity = new StatVector3(0, -9.81, 0);

    @Override
    public StatVector3 gravityAccFor(GameObject object) {
        return gravity;
    }
}
