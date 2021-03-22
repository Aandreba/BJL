package OpenGL;

import Matrix.RelMatrix;
import OpenGL.Extras.Vector.Vector3;
import Vector.Vector;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Mesh {
    final private int vertexCount, triangleCount;

    protected float[] vertices;
    protected float[] normals;
    protected int[] triangles;
    protected float[] texCoords;

    private int vao, vbo, tVbo, nVbo, xVbo;

    public Mesh (int vertices, int triangles) {
        this.vertexCount = vertices;
        this.triangleCount = triangles;

        this.vertices = new float[vertices * 3];
        this.normals = new float[vertices * 3];
        this.triangles = new int[triangles * 3];
        this.texCoords = new float[vertices * 2];
        this.draw();
    }

    public Mesh (float[] vertices, float[] texCoords, int[] triangles, float[] normals) {
        this.vertexCount = vertices.length / 3;
        this.triangleCount = triangles.length / 3;

        this.vertices = vertices;
        this.triangles = triangles;
        this.texCoords = texCoords;
        this.normals = normals;
        this.draw();
    }

    public Mesh (float[] vertices, float[] texCoords, int[] triangles) {
        this.vertexCount = vertices.length / 3;
        this.triangleCount = triangles.length / 3;

        this.vertices = vertices;
        this.triangles = triangles;
        this.texCoords = texCoords;
        this.calculateNormals();
        this.draw();
    }

    public Mesh (float[] vertices, int[] triangles) {
        this.vertexCount = vertices.length / 3;
        this.triangleCount = triangles.length / 3;

        this.vertices = vertices;
        this.triangles = triangles;
        this.texCoords = new float[vertices.length];
        this.calculateNormals();
        this.calculateTexCoords();
        this.draw();
    }

    public void calculateNormals () {
        this.normals = new float[this.vertices.length];
        RelMatrix matrix = normalMatrix();

        for (int j=0;j<triangleCount;j++) {
            int[] triangle = getTriangle(j);

            Vector3 a = getVertex(triangle[0]);
            Vector3 b = getVertex(triangle[1]);
            Vector3 c = getVertex(triangle[2]);
            Vector3 n = b.subtr(a).cross(c.subtr(a));

            matrix.get(triangle[0]).add(n);
            matrix.get(triangle[1]).add(n);
            matrix.get(triangle[2]).add(n);
        }

        for (int i=0;i<vertexCount;i++) {
            matrix.set(i, matrix.get(i).getNormalized());
        }
    }

    public void calculateTexCoords () {
        this.texCoords = new float[this.texCoords.length];
        RelMatrix matrix = vertexMatrix();

        for (int i=0;i<matrix.getRows();i++) {
            Vector vertex = matrix.get(i).getNormalized();
            this.texCoords[i * 2] = (vertex.getFloat(0) + 1) / 2;
            this.texCoords[i * 2 + 1] = (vertex.getFloat(1) + 1) / 2;
        }
    }

    public Matrix.RelMatrix vertexMatrix() {
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

    public Matrix.RelMatrix normalMatrix() {
        return new RelMatrix(vertexCount, 3) {
            @Override
            public double get(int row, int col) {
                return normals[(row * 3) + col];
            }

            @Override
            public void set(int row, int col, double value) {
                normals[(row * 3) + col] = (float) value;
            }
        };
    }

    public Matrix.RelMatrix textureMatrix() {
        return new RelMatrix(texCoords.length / 2, 2) {
            @Override
            public double get(int row, int col) {
                return texCoords[(row * 2) + col];
            }

            @Override
            public void set(int row, int col, double value) {
                texCoords[(row * 2) + col] = (float) value;
            }
        };
    }

    public int getVao() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
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
     * Set vertex normal
     * @param id Vertex position in matrix
     * @param x X axis position (from -1 to 1)
     * @param y Y axis position (from -1 to 1)
     * @param z Z axis position (from -1 to 1)
     */
    public void setNormal (int id, float x, float y, float z) {
        this.normals[3 * id] = x;
        this.normals[3 * id + 1] = y;
        this.normals[3 * id + 2] = z;
    }

    public Vector3 getNormal (int id) {
        return new Vector3() {
            @Override
            public double get(int pos) {
                return normals[3 * id + pos];
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

    public int[] getTriangles() {
        return triangles;
    }

    public int[] getTriangle (int id) {
        return new int[]{ triangles[3 * id], triangles[3 * id + 1], triangles[3 * id + 2] };
    }

    /**
     * Draw mesh to be later rendered
     */
    public void draw () {
        // VAO
        this.vao = glGenVertexArrays();
        glBindVertexArray(this.vao);

        // VBO
        this.vbo = bindFloats(this.vertices);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Texture VBO
        this.xVbo = bindFloats(this.texCoords);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        // Vertex normals VBO
        this.nVbo = bindFloats(this.normals);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

        // Triangles VBO
        IntBuffer tBuffer = MemoryUtil.memAllocInt(triangles.length);
        tBuffer.put(triangles).flip();

        this.tVbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.tVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, tBuffer, GL_STATIC_DRAW);
        //memFree(tBuffer);

        // Others
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private int bindFloats (float[] array) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(array.length);
        buffer.put(array).flip();

        int id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        memFree(buffer);

        return id;
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(this.vbo);
        glDeleteBuffers(this.tVbo);
        glDeleteBuffers(this.nVbo);
        glDeleteBuffers(this.xVbo);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(this.vao);
    }
}
