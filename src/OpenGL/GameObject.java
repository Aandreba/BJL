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
    public Shader shader;
    public Transform transform;
    public Texture texture;
    public Color color = Color.GRAY;

    public GameObject(Mesh mesh, Shader shader, Transform transform) {
        this.mesh = mesh;
        this.shader = shader;
        this.transform = transform;
    }

    public GameObject (Mesh mesh, Shader shader, Vector3 pos, Vector3 rot, float scale) {
        this(mesh, shader);

        this.transform = new Transform();
        if (pos != null) {
            this.transform.setPosition(pos);
        }

        if (rot != null) {
            this.transform.setRotation(rot);
        }

        this.transform.setScale(scale);
    }

    public GameObject (Mesh mesh, Shader shader) {
        this.mesh = mesh;
        this.shader = shader;
        this.transform = new Transform();
    }

    /**
     * Render mesh on screen
     */
    public void render (Window window) {
        shader.bind();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shader.setUniform("transform", this.transform.getMatrix());
        shader.setUniform("project", window.getProjectionMatrix().toRelative());
        shader.setUniform("view", window.mainCamera.view);
        shader.setUniform("textureSampler", 0);
        shader.setUniform("defColor", color);
        shader.setUniform("useColor", (texture == null) ? 1 : 0);

        if (texture != null) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.id);
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

        shader.unbind();
    }

    public void cleanup () {
        if (this.shader != null) {
            this.shader.cleanup();
        }

        if (this.texture != null) {
            this.texture.cleanup();
        }

        mesh.cleanup();
    }
}
