package OpenGL.Colliders;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;

public class SphereCollider implements Collider {
    /**
     * World coordinates
     */
    public Vector3 position;

    /**
     * Sphere radius
     */
    public float radius;

    public SphereCollider (Vector3 position, float radius) {
        this.position = position;
        this.radius = radius;
    }

    public SphereCollider (StatVector3 position, float radius) {
        this.position = position.toRelative();
        this.radius = radius;
    }

    @Override
    public boolean isCollidingAt (Vector3 position) {
        return this.position.dist(position) <= radius;
    }

    @Override
    public boolean isCollidingWith (SphereCollider collider) {
        return this.position.dist(collider.position) <= radius + collider.radius;
    }

    @Override
    public boolean isCollidingWith (BoxCollider collider) {
        return collider.isCollidingWith(this);
    }

    @Override
    public StatVector3 pointOfCollisionWith (SphereCollider collider) {
        Vector3 delta = collider.position.subtr(position);

        return position.sum(delta.getNormalized().mul(radius)).toStatic();
    }

    @Override
    public StatVector3 pointOfCollisionWith (BoxCollider collider) {
        return collider.pointOfCollisionWith(this);
    }
}
