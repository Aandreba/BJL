package OpenGL.Colliders;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.Transform;

public class BoxCollider implements Collider {
    /**
     * World coordinates
     */
    Vector3 position;

    /**
     * Box scale in 3 dimensions
     */
    Vector3 scale;

    public BoxCollider (Vector3 position, Vector3 scale) {
        this.position = position;
        this.scale = scale;
    }

    public BoxCollider (StatVector3 position, Vector3 scale) {
        this.position = position.toRelative();
        this.scale = scale;
    }

    public BoxCollider (Vector3 position, StatVector3 scale) {
        this.position = position;
        this.scale = scale.toRelative();
    }

    public BoxCollider (StatVector3 position, StatVector3 scale) {
        this.position = position.toRelative();
        this.scale = scale.toRelative();
    }

    public BoxCollider (Vector3 position, float scale) {
        this.position = position;
        this.scale = new Vector3() {
            @Override
            public double get(int pos) {
                return scale;
            }
        };
    }

    public BoxCollider (StatVector3 position, float scale) {
        this.position = position.toRelative();
        this.scale = new Vector3() {
            @Override
            public double get(int pos) {
                return scale;
            }
        };
    }

    @Override
    public boolean isCollidingAt (Vector3 position) {
        Vector3 dist = this.position.subtr(position).abs();
        return dist.x() <= this.scale.x() && dist.y() <= this.scale.y() && dist.z() <= this.scale.z();
    }

    @Override
    public boolean isCollidingWith (SphereCollider collider) {
        StatVector3 closest = pointOfCollisionWith(collider);

        double dist = closest.subtr(collider.position).getSqrtMagnitude();
        return dist < collider.radius;
    }

    @Override
    public boolean isCollidingWith (BoxCollider collider) {
        Vector3 dist = this.position.subtr(collider.position).abs();
        Vector3 size = this.scale.sum(collider.scale);
        return dist.x() <= size.x() && dist.y() <= size.y() && dist.z() <= size.z();
    }

    @Override
    public StatVector3 pointOfCollisionWith (BoxCollider collider) {
        if (scale.getMean() > collider.scale.getMean()) {
            Vector3 dir = position.subtr(collider.position).getNormalized().round();
            return dir.mul(scale).toStatic();
        }

        Vector3 dir = collider.position.subtr(position).getNormalized().round();
        return dir.mul(collider.scale).toStatic();
    }

    @Override
    public StatVector3 pointOfCollisionWith (SphereCollider collider) {
        Vector3 closest = new Vector3() {
            @Override
            public double get(int pos) {
                return Math.max(position.get(pos) - scale.get(pos), Math.min(position.get(pos) + scale.get(pos), collider.position.get(pos)));
            }
        };

        return closest.toStatic();
        /*return new Vector3() {
            @Override
            public double get(int pos) {
                return Math.max(position.get(pos) - scale.get(pos), Math.min(position.get(pos) + scale.get(pos), collider.position.get(pos)));
            }
        }.toStatic();*/
    }
}
