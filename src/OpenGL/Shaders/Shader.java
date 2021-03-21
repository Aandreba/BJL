package OpenGL.Shaders;

import Extras.Files;
import OpenGL.Extras.Matrix.Matrix4;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import org.lwjgl.opengl.GL40;

import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL43.*;

public class Shader {
    final protected int id;
    protected int vertex;
    protected int compute = -1;
    protected int fragment;

    public int workGroupX, workGroupY, workGroupZ;
    protected final Map<String, Integer> uniforms;

    public Shader () throws Exception {
        this.id = glCreateProgram();
        if (this.id == 0) {
            throw new Exception("Could not create Shader");
        }
        this.uniforms = new HashMap<>();
    }

    public void createVertexShader (String code) throws Exception {
        this.vertex = createShader(code, GL_VERTEX_SHADER);
    }

    public void createVertexShader (File file) throws Exception {
        createVertexShader(Files.loadFile(file, StandardCharsets.UTF_8));
    }

    public void createComputeShader (String code) throws Exception {
        this.vertex = createShader(code, GL_COMPUTE_SHADER);
    }

    public void createComputeShader (File file) throws Exception {
        createComputeShader(Files.loadFile(file, StandardCharsets.UTF_8));
    }

    public void createFragmentShader (String code) throws Exception {
        this.vertex = createShader(code, GL_FRAGMENT_SHADER);
    }

    public void createFragmentShader (File file) throws Exception {
        createFragmentShader(Files.loadFile(file, StandardCharsets.UTF_8));
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

    public void createUniform (String uniformName) {
        int uniformLocation = glGetUniformLocation(this.id,
                uniformName);
        if (uniformLocation < 0) {
            System.out.println("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform (String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform (String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform (String uniformName, Vector3 value) {
        glUniform3f(uniforms.get(uniformName), value.xf(), value.yf(), value.zf());
    }

    public void setUniform (String uniformName, StatVector3 value) {
        glUniform3f(uniforms.get(uniformName), value.xf(), value.yf(), value.zf());
    }

    public void setUniform (String uniformName, Vector3 value, float w) {
        glUniform4f(uniforms.get(uniformName), value.xf(), value.yf(), value.zf(), w);
    }

    public void setUniform (String uniformName, StatVector3 value, float w) {
        glUniform4f(uniforms.get(uniformName), value.xf(), value.yf(), value.zf(), w);
    }

    public void setUniform (String uniformName, float x, float y, float z) {
        glUniform3f(uniforms.get(uniformName), x, y, z);
    }

    public void setUniform (String uniformName, float x, float y, float z, float w) {
        glUniform4f(uniforms.get(uniformName), x, y, z, w);
    }

    public void setUniform (String uniformName, Color color) {
        setUniform(uniformName, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
    }

    public void setUniform (String uniformName, Matrix4 value) {
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

    public static StatVector3 maxWorkGroupSize () {
        int[] v = new int[3];
        glGetIntegeri_v(GL_MAX_COMPUTE_WORK_GROUP_COUNT, 0, v);
        glGetIntegeri_v(GL_MAX_COMPUTE_WORK_GROUP_COUNT, 1, v);
        glGetIntegeri_v(GL_MAX_COMPUTE_WORK_GROUP_COUNT, 2, v);

        return new StatVector3(v[0], v[1], v[2]);
    }

    public void bind() {
        glUseProgram(this.id);
        if (compute > -1) {
            glDispatchCompute(workGroupX, workGroupY, workGroupZ);
        }
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
