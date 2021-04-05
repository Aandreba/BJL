package OpenGL.Colliders;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;

public class SphereCollider implements Collider {
    public Vector3 position;
    public float radius;

    public SphereCollider(Vector3 position, float radius) {
        this.position = position;
        this.radius = radius;
    }

    public SphereCollider(StatVector3 position, float radius) {
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
        if (radius > collider.radius) {
            Vector3 dir = position.subtr(collider.position).getNormalized();
            return dir.mul(radius).toStatic();
        }

        Vector3 dir = collider.position.subtr(position).getNormalized();
        return dir.mul(collider.radius).toStatic();
    }

    @Override
    public StatVector3 pointOfCollisionWith (BoxCollider collider) {
        return collider.pointOfCollisionWith(this);
    }
}
