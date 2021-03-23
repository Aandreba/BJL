package OpenGL.Light;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.Shaders.Shader;

import java.awt.*;

public class PointLight {
    public StatVector3 position;
    public Color color;
    public float intensity;

    public PointLight(Vector3 position, Color color, float intensity) {
        this.position = position.toStatic();
        this.color = color;
        this.intensity = intensity;
    }

    public PointLight(StatVector3 position, Color color, float intensity) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
    }

    public void setAsUniform (String name, Shader shader) {
        shader.setUniform(name+".pos", position);
        shader.setUniform(name+".color", color);
        shader.setUniform(name+".intensity", intensity);
    }

    public void setAsUniform (String name, int pos, Shader shader) {
        setAsUniform(name+"["+pos+"]", shader);
    }

    public static void createUniform (String name, Shader shader) throws Exception {
        shader.createUniform(name+".pos");
        shader.createUniform(name+".color");
        shader.createUniform(name+".intensity");
    }

    public static void createArrayUniform (String name, int size, Shader shader) throws Exception {
        for (int i=0;i<size;i++) {
            createUniform(name + "[" + i + "]", shader);
        }
    }
}
