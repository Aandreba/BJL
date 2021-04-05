package OpenGL.Extras.Gravity;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.GameObject;

public interface Gravity {
    StatVector3 gravityAccFor (GameObject object);
}
