package OpenGL.Colliders;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;

public interface Collider {
    boolean isCollidingAt (Vector3 position);
    default boolean isCollidingAt (StatVector3 position) {
        return isCollidingAt(position.toRelative());
    };
}
