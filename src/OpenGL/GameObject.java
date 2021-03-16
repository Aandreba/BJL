package OpenGL;

import OpenGL.Mesh.Mesh;
import OpenGL.Shader.Shader;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryUtil.*;

public class GameObject {
    public Mesh mesh;
    public Shader shader;
    public Transform transform;

    public GameObject(Mesh mesh, Shader shader, Transform transform) {
        this.mesh = mesh;
        this.shader = shader;
        this.transform = transform;
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

        shader.setUniformMatrix4("transform", this.transform.getMatrix());
        shader.setUniformMatrix4("project", window.getProjectionMatrix().toRelative());
        shader.setUniformMatrix4("view", window.mainCamera.getViewMatrix());

        // Bind to the VAO
        glBindVertexArray(this.mesh.getVao());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the vertices
        glDrawElements(GL_TRIANGLES, mesh.getTriangles().length, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.unbind();
    }

    public void cleanup () {
        if (this.shader != null) {
            this.shader.cleanup();
        }

        mesh.cleanup();
    }
}
