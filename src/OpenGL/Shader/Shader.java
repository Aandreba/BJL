package OpenGL.Shader;

import Extras.Files;
import Extras.Sys;
import OpenGL.Extras.Matrix.Matrix4;
import OpenGL.Extras.Vector.Vector3;
import org.lwjgl.opengl.GL40;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    final private int id;
    private int vertex;
    private int fragment;

    private final Map<String, Integer> uniforms;

    public Shader () throws Exception {
        this.id = glCreateProgram();
        if (this.id == 0) {
            throw new Exception("Could not create Shader");
        }
        this.uniforms = new HashMap<>();

        this.createVertexShader(new File("src/OpenGL/Shader/GLCode/vertex.vert"));
        this.createFragmentShader(new File("src/OpenGL/Shader/GLCode/fragment.frag"));

        this.createUniform("project");
        this.createUniform("view");
        this.createUniform("transform");
    }

    public void createVertexShader (String code) throws Exception {
        this.vertex = createShader(code, GL_VERTEX_SHADER);
    }

    public void createVertexShader (File file) throws Exception {
        createVertexShader(Files.loadFile(file));
    }

    public void createFragmentShader (String code) throws Exception {
        this.vertex = createShader(code, GL_FRAGMENT_SHADER);
    }

    public void createFragmentShader (File file) throws Exception {
        createFragmentShader(Files.loadFile(file));
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(this.id, shaderId);
        this.link();

        return shaderId;
    }

    public void createUniform (String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(this.id,
                uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" +
                    uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniformMatrix4 (String uniformName, Matrix4 value) {
        glUniformMatrix4fv(uniforms.get(uniformName), true, value.toVector().toFloatArray());
    }

    public void link () throws Exception {
        glLinkProgram(this.id);
        if (glGetProgrami(this.id, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(this.id, 1024));
        }

        if (this.vertex != 0) {
            glDetachShader(this.id, this.vertex);
        }
        if (this.fragment != 0) {
            glDetachShader(this.id, this.fragment);
        }

        glValidateProgram(this.id);
        if (glGetProgrami(this.id, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(this.id, 1024));
        }

    }

    public void bind() {
        glUseProgram(this.id);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (this.id != 0) {
            glDeleteProgram(this.id);
        }
    }
}
