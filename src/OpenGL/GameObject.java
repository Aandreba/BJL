package OpenGL;

import OpenGL.Colliders.BoxCollider;
import OpenGL.Colliders.Collider;
import OpenGL.Colliders.SphereCollider;
import OpenGL.Extras.Vector.Vector3;
import Units.Mass;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GameObject {
    final public Transform transform;
    public ArrayList<String> tags;
    public Mesh mesh;
    public Material material;
    public Collider collider;
    public Rigidbody rb;
    protected Window window;

    public GameObject (Mesh mesh, Transform transform, Material material) {
        this.mesh = mesh;
        this.transform = transform;
        this.material = material;
        this.tags = new ArrayList<>();
    }

    public GameObject (Mesh mesh, Material material, Vector3 pos, Vector3 rot, float scale) {
        this(mesh, material);

        if (pos != null) {
            this.transform.setPosition(pos);
        }

        if (rot != null) {
            this.transform.setRotation(rot);
        }

        this.transform.setScale(scale);
    }

    public GameObject (Mesh mesh, Material material) {
        this.mesh = mesh;
        this.transform = new Transform();
        this.material = material;
        this.tags = new ArrayList<>();
    }

    public GameObject (Mesh mesh, Color color) {
        this(mesh, new Material(color, 1f));
    }

    public GameObject (Mesh mesh, Texture texture) {
        this(mesh, new Material(texture, 1f));
    }

    public void createBoxCollider () {
        this.collider = new BoxCollider(new Vector3() {
            @Override
            public double get(int pos) {
                return transform.position.get(pos);
            }
        }, new Vector3() {
            @Override
            public double get(int pos) {
                return transform.scale.get(pos);
            }
        });
    }

    public void createSphereCollider () {
        this.collider = new SphereCollider(new Vector3() {
            @Override
            public double get(int pos) {
                return transform.position.get(pos);
            }
        }, (float) transform.scale.getMean());
    }

    public void createRigidbody (Mass mass) {
        this.rb = new Rigidbody(this, mass);
    }

    /**
     * Render mesh on screen
     */
    public void render () {
        window.shader.setUniform("transform", this.transform.matrix);
        material.setAsUniform(window.shader);

        if (material.texture != null) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, material.texture.id);
        }

        // Bind to the VAO
        glBindVertexArray(this.mesh.getVao());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        // Draw the vertices
        glDrawElements(GL_TRIANGLES, mesh.getTriangleCount() * 3, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup () {
        mesh.cleanup();
        material.cleanup();
    }
}
