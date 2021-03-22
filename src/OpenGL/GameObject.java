package OpenGL;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;

import java.awt.*;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GameObject {
    public Mesh mesh;
    public Transform transform;
    public Material material;

    public GameObject(Mesh mesh, Transform transform, Material material) {
        this.mesh = mesh;
        this.transform = transform;
        this.material = material;
    }

    public GameObject (Mesh mesh, Material material, Vector3 pos, Vector3 rot, float scale) {
        this(mesh, material);

        this.transform = new Transform();
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
    }

    /**
     * Render mesh on screen
     */
    public void render (Window window) {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        window.shader.setUniform("transform", this.transform.getMatrix());
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
        glDrawElements(GL_TRIANGLES, mesh.getTriangles().length, GL_UNSIGNED_INT, 0);

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
