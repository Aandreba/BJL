package OpenGL.Mesh;

import Matrix.RelMatrix;
import OpenGL.Extras.Vector.Vector3;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Mesh {
    private int vertexCount, triangleCount;

    protected float[] vertices;
    protected int[] triangles;
    protected float[] colors;

    private int vao, vbo, tVbo, cVbo;

    public Mesh (int vertices, int triangles) {
        this.vertexCount = vertices;
        this.triangleCount = triangles;

        this.vertices = new float[vertices * 3];
        this.triangles = new int[triangles * 3];
        this.colors = new float[vertices * 3];
    }

    public Mesh (float[] vertices, int[] triangles) {
        this.vertexCount = vertices.length / 3;
        this.triangleCount = triangles.length / 3;

        this.vertices = vertices;
        this.triangles = triangles;
        this.colors = new float[vertices.length];
    }

    public Mesh (File file) {

    }

    public int getVao() {
        return vao;
    }

    public int getVbo() {
        return vbo;
    }

    public int getTriangleVbo() {
        return tVbo;
    }

    public int getColorVbo() {
        return cVbo;
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

    public Matrix.RelMatrix getMatrix() {
        return new RelMatrix(vertexCount, 3) {
            @Override
            public double get(int row, int col) {
                return vertices[(row * 3) + col];
            }

            @Override
            public void set(int row, int col, double value) {
                vertices[(row * 3) + col] = (float) value;
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

    public Vector3 getVertex (int id) {
        return new Vector3() {
            @Override
            public double get(int pos) {
                return vertices[3 * id + pos];
            }
        };
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
     * Set vertex color
     * @param id Vertex position in matrix
     * @param color Vertex color
     */
    public void setColor (int id, Color color) {
        this.colors[3 * id] = color.getRed() / 255f;
        this.colors[3 * id + 1] = color.getGreen() / 255f;
        this.colors[3 * id + 2] = color.getBlue() / 255f;
    }

    /**
     * Set color for all vertices
     * @param color Vertices color
     */
    public void setColor (Color color) {
        for (int i=0;i<getVertexCount();i++) {
            setColor(i, color);
        }
    }

    /**
     * Draw mesh to be later rendered
     */
    public void draw () {
        // VAO
        this.vao = glGenVertexArrays();
        glBindVertexArray(this.vao);

        // VBO
        FloatBuffer vBuffer = MemoryUtil.memAllocFloat(vertices.length);
        vBuffer.put(vertices).flip();

        this.vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
        glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);;
        memFree(vBuffer);

        // Triangles VBO
        IntBuffer tBuffer = MemoryUtil.memAllocInt(triangles.length);
        tBuffer.put(triangles).flip();

        this.tVbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.tVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, tBuffer, GL_STATIC_DRAW);
        memFree(tBuffer);

        // Colour VBO
        FloatBuffer colourBuffer = memAllocFloat(colors.length);
        colourBuffer.put(colors).flip();

        this.cVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, cVbo);
        glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        memFree(colourBuffer);

        // Others
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(this.vbo);
        glDeleteBuffers(this.tVbo);
        glDeleteBuffers(this.cVbo);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(this.vao);
    }
}
