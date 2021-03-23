package OpenGL.Light;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Shaders.Shader;
import Units.Angle;

import java.awt.*;

public class DirectionalLight {
    public Color color;
    public Angle angle;
    public float intensity;

    public DirectionalLight(Color color, Angle angle, float intensity) {
        this.color = color;
        this.angle = angle;
        this.intensity = intensity;
    }

    public void setAsUniform (String name, Shader shader) {
        StatVector3 dir = new StatVector3(angle.sinf(), angle.cosf(), 0);

        shader.setUniform(name+".color", color);
        shader.setUniform(name+".dir", dir.getNormalized());
        shader.setUniform(name+".intensity", intensity);
    }

    public void setAsUniform (String name, int pos, Shader shader) {
        setAsUniform(name+"["+pos+"]", shader);
    }

    public static void createUniform (String name, Shader shader) throws Exception {
        shader.createUniform(name+".color");
        shader.createUniform(name+".dir");
        shader.createUniform(name+".intensity");
    }

    public static void createArrayUniform (String name, int size, Shader shader) throws Exception {
        for (int i=0;i<size;i++) {
            createUniform(name + "[" + i + "]", shader);
        }
    }
}
