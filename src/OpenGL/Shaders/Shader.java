package OpenGL.Shaders;

import Extras.Files;
import OpenGL.Extras.Matrix.Matrix4;
import OpenGL.Extras.Matrix.StatMatrix4;
import OpenGL.Extras.Vector.StatVector2;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector2;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.Light.DirectionalLight;
import OpenGL.Light.PointLight;
import OpenGL.Light.SpotLight;
import OpenGL.Material;

import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL43.*;

public class Shader {
    final protected int id;
    final protected Map<String, Integer> uniforms;
    protected int vertex;
    protected int fragment;

    protected Shader (boolean init) throws Exception {
        this.id = glCreateProgram();
        if (this.id == 0) {
            throw new Exception("Could not create Shader");
        }
        this.uniforms = new HashMap<>();
    }

    public Shader () throws Exception {
        this(true);

        this.createVertexShader(Files.loadResource("/OpenGL/GL/vertex.vert"));
        this.createFragmentShader(Files.loadResource("/OpenGL/GL/fragment.frag"));

        this.createUniform("project");
        this.createUniform("view");
        this.createUniform("transform");
        this.createUniform("textureSampler");

        Material.createUniform(this);
        PointLight.createArrayUniform("points", 5, this);
        DirectionalLight.createArrayUniform("directionals", 5, this);
        SpotLight.createArrayUniform("spots", 5, this);
    }

    public void createVertexShader (String code) throws Exception {
        this.vertex = createShader(code, GL_VERTEX_SHADER);
    }

    public void createVertexShader (File file) throws Exception {
        createVertexShader(Files.loadFile(file, StandardCharsets.UTF_8));
    }

    public void createFragmentShader (String code) throws Exception {
        this.fragment = createShader(code, GL_FRAGMENT_SHADER);
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

    public void setUniform (String uniformName, Vector2 value) {
        glUniform2f(uniforms.get(uniformName), value.xf(), value.yf());
    }

    public void setUniform (String uniformName, StatVector2 value) {
        glUniform2f(uniforms.get(uniformName), value.xf(), value.yf());
    }

    public void setUniform (String uniformName, float x, float y) {
        glUniform2f(uniforms.get(uniformName), x, y);
    }

    public void setUniform (String uniformName, Vector3 value) {
        glUniform3f(uniforms.get(uniformName), value.xf(), value.yf(), value.zf());
    }

    public void setUniform (String uniformName, StatVector3 value) {
        glUniform3f(uniforms.get(uniformName), value.xf(), value.yf(), value.zf());
    }

    public void setUniform (String uniformName, float x, float y, float z) {
        glUniform3f(uniforms.get(uniformName), x, y, z);
    }

    public void setUniform (String uniformName, Vector3 value, float w) {
        glUniform4f(uniforms.get(uniformName), value.xf(), value.yf(), value.zf(), w);
    }

    public void setUniform (String uniformName, StatVector3 value, float w) {
        glUniform4f(uniforms.get(uniformName), value.xf(), value.yf(), value.zf(), w);
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

    public void setUniform (String uniformName, StatMatrix4 value) {
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
