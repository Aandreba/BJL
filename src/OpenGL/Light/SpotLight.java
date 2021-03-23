package OpenGL.Light;

import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.Shaders.Shader;
import Units.Angle;

import java.awt.*;

public class SpotLight {
    public Vector3 position;
    public StatVector3 direction;
    public Angle angle;
    public Color color;
    public float intensity;

    public SpotLight(Vector3 position, StatVector3 direction, Angle angle, Color color, float intensity) {
        this.position = position;
        this.direction = direction;
        this.angle = angle;
        this.color = color;
        this.intensity = intensity;
    }

    public SpotLight(StatVector3 position, StatVector3 direction, Angle angle, Color color, float intensity) {
        this.position = position.toRelative();
        this.direction = direction;
        this.angle = angle;
        this.color = color;
        this.intensity = intensity;
    }

    public void setAsUniform (String name, Shader shader) {
        shader.setUniform(name+".pos", position);
        shader.setUniform(name+".dir", direction.getNormalized());
        shader.setUniform(name+".color", color);
        shader.setUniform(name+".cutoff", angle.cosf());
        shader.setUniform(name+".intensity", intensity);
    }

    public void setAsUniform (String name, int pos, Shader shader) {
        setAsUniform(name+"["+pos+"]", shader);
    }

    public static void createUniform (String name, Shader shader) throws Exception {
        shader.createUniform(name+".pos");
        shader.createUniform(name+".dir");
        shader.createUniform(name+".color");
        shader.createUniform(name+".cutoff");
        shader.createUniform(name+".intensity");
    }

    public static void createArrayUniform (String name, int size, Shader shader) throws Exception {
        for (int i=0;i<size;i++) {
            createUniform(name + "[" + i + "]", shader);
        }
    }
}
