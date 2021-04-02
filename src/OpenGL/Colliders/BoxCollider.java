package OpenGL.Colliders;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.Transform;

public class BoxCollider implements Collider {
    Vector3 position;
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
        Vector3 closest = new Vector3() {
            @Override
            public double get(int pos) {
                return Math.max(position.get(pos) - scale.get(pos), Math.min(position.get(pos) + scale.get(pos), collider.position.get(pos)));
            }
        };

        double dist = closest.subtr(collider.position).getSqrtMagnitude();
        return dist < collider.radius;
    }

    public boolean isCollidingWith (BoxCollider collider) {
        Vector3 dist = this.position.subtr(collider.position).abs();
        Vector3 size = this.scale.sum(collider.scale);
        return dist.x() <= size.x() && dist.y() <= size.y() && dist.z() <= size.z();
    }
}
