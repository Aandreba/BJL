package OpenGL.Colliders;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;

/**
 * Class {@link Collider} is the base of the collision-detection system
 * @author Alex Andreba
 * @since 0.1A
 * @see SphereCollider
 * @see BoxCollider
 * @see Collision
 */
public interface Collider {
    /**
     * Calculate if collider is in contact with specific real-world point
     * @param position Coordinate of contact
     * @return  {@link Boolean#TRUE} if colliding at point,  {@link Boolean#FALSE} otherwise
     */
    boolean isCollidingAt (Vector3 position);

    /**
     * Calculate if collider is in contact with specific real-world point
     * @see #isCollidingAt(Vector3)
     */
    default boolean isCollidingAt (StatVector3 position) {
        return isCollidingAt(position.toRelative());
    };

    /**
     * Calculate if two colliders are colliding
     * @param collider Collider with which detect collision with
     * @return {@link Boolean#TRUE} if are colliding,  {@link Boolean#FALSE} otherwise
     */
    default boolean isCollidingWith (Collider collider) {
        if (collider instanceof SphereCollider) {
            return isCollidingWith((SphereCollider) collider);
        } else if (collider instanceof BoxCollider) {
            return isCollidingWith((BoxCollider) collider);
        }

        return false;
    }

    /**
     * Calculate if two colliders are colliding
     * @see #pointOfCollisionWith(Collider)
     */
    boolean isCollidingWith (SphereCollider collider);

    /**
     * Calculate if two colliders are colliding
     * @see #pointOfCollisionWith(Collider)
     */
    boolean isCollidingWith (BoxCollider collider);

    /**
     * Calculate collision point between two colliders
     * @param collider Collider with which detect collision point with
     * @return Collision point between colliders
     */
    default StatVector3 pointOfCollisionWith (Collider collider) {
        if (collider instanceof SphereCollider) {
            return pointOfCollisionWith((SphereCollider) collider);
        } else if (collider instanceof BoxCollider) {
            return pointOfCollisionWith((BoxCollider) collider);
        }

        return null;
    }

    /**
     * Calculate collision point between two colliders
     * @see #pointOfCollisionWith(Collider)
     */
    StatVector3 pointOfCollisionWith (SphereCollider collider);

    /**
     * Calculate collision point between two colliders
     * @see #pointOfCollisionWith(Collider)
     */
    StatVector3 pointOfCollisionWith (BoxCollider collider);
}
