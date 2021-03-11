package OpenGL.Mesh;

import Matrix.Matrix;
import Matrix.StatMatrix;
import OpenCL.Context;
import OpenGL.Shader.Primitives.ColorShader;
import OpenGL.Shader.Shader;
import OpenGL.Window;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Mesh {
    private int vertexCount, triangleCount;

    private float[] vertices;
    private int[] triangles;

    public Shader shader;
    private int vao, vbo, tVbo;

    public Mesh (int vertices, int triangles, Shader shader) {
        this.vertexCount = vertices;
        this.triangleCount = triangles;

        this.vertices = new float[vertices * 3];
        this.triangles = new int[triangles * 3];
        this.shader = shader;
    }

    public Mesh (float[] vertices, int[] triangles, Shader shader) {
        this.vertexCount = vertices.length / 3;
        this.triangleCount = triangles.length / 3;

        this.vertices = vertices;
        this.triangles = triangles;
        this.shader = shader;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getTriangleCount() {
        return triangleCount;
    }

    public float[] getVertices() {
        return vertices;
    }

    public Matrix getVertexMatrix () {
        return new Matrix (vertexCount, 3) {
            @Override
            public double get(int row, int col) {
                return vertices[(row * 3) + col];
            }
        };
    }

    public int[] getTriangles() {
        return triangles;
    }

    /**
     * Set vertex position
     * @param id Vertex position in matrix
     * @param x X axis position (from -1 to 1)
     * @param y Y axis position (from -1 to 1)
     * @param z Z axis position (from -1 to 1)
     */
    public void setVertex (int id, float x, float y, float z) {
        this.vertices[3 * id] = x;
        this.vertices[3 * id + 1] = y;
        this.vertices[3 * id + 2] = z;
    }

    /**
     * Set triangle values
     * @param id Triangle position in matrix
     * @param from Initial vertex in vertices matrix
     * @param middle Middle vertex in vertices matrix
     * @param to Final vertex in vertices matrix
     */
    public void setTriangle (int id, int from, int middle, int to) {
        this.triangles[3 * id] = from;
        this.triangles[3 * id + 1] = middle;
        this.triangles[3 * id + 2] = to;
    }

    /**
     * Draw mesh to be later rendered
     */
    public void draw () {
        FloatBuffer vBuffer = MemoryUtil.memAllocFloat(vertices.length);
        vBuffer.put(vertices).flip();

        this.vao = glGenVertexArrays();
        glBindVertexArray(this.vao);

        this.vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
        glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
        memFree(vBuffer);

        IntBuffer tBuffer = MemoryUtil.memAllocInt(triangles.length);
        tBuffer.put(triangles).flip();

        this.tVbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.tVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, tBuffer, GL_STATIC_DRAW);
        memFree(tBuffer);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        //MemoryUtil.memFree(vBuffer);
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

        // Bind to the VAO
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the vertices
        glDrawElements(GL_TRIANGLES, triangles.length, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shader.unbind();
    }

    public void cleanup() {
        if (this.shader != null) {
            this.shader.cleanup();
        }

        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(this.vbo);
        glDeleteBuffers(this.tVbo);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(this.vao);
    }
}
