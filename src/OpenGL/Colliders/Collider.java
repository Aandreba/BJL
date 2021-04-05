package OpenGL.Colliders;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;

public interface Collider {
    boolean isCollidingAt (Vector3 position);
    default boolean isCollidingAt (StatVector3 position) {
        return isCollidingAt(position.toRelative());
    };

    boolean isCollidingWith (SphereCollider collider);
    boolean isCollidingWith (BoxCollider collider);
    default boolean isCollidingWith (Collider collider) {
        if (collider instanceof SphereCollider) {
            return isCollidingWith((SphereCollider) collider);
        } else if (collider instanceof BoxCollider) {
            return isCollidingWith((BoxCollider) collider);
        }

        return false;
    }

    StatVector3 pointOfCollisionWith (SphereCollider collider);
    StatVector3 pointOfCollisionWith (BoxCollider collider);
    default StatVector3 pointOfCollisionWith (Collider collider) {
        if (collider instanceof SphereCollider) {
            return pointOfCollisionWith((SphereCollider) collider);
        } else if (collider instanceof BoxCollider) {
            return pointOfCollisionWith((BoxCollider) collider);
        }

        return null;
    }
}
