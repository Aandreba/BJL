package OpenGL.Colliders;

import Matrix.Matrix;
import OpenGL.Extras.Matrix.StatMatrix4;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.Mesh;
import OpenGL.Transform;
import Vector.Vector;

public class MeshCollider implements Collider {
    public Mesh mesh;
    public Transform transform;

    public MeshCollider (Mesh mesh, Transform transform) {
        this.mesh = mesh;
        this.transform = transform;
    }

    @Override
    public boolean isCollidingAt (Vector3 position) {
        StatMatrix4 transform = this.transform.getMatrix().toStatic();
        Matrix worldVertices = transform.smartMul(mesh.vertexMatrix4().transposed()).transposed();

        for (int i=0;i<worldVertices.getRows();i++) {
            Vector v = worldVertices.get(i);
            Vector3 vertex = new Vector3() {
                @Override
                public double get(int pos) {
                    return v.get(pos);
                }
            };

            Vector3 normal = mesh.getNormal(i);
            Vector dir = vertex.subtr(position).getNormalized();
            //System.out.println(dir+" ; "+normal);
            break;
        }

        System.out.println();
        return false;
    }
}
