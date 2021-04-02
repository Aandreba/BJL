package OpenGL;

import OpenGL.Shaders.Shader;

import java.awt.*;

public class Material {
    public Texture texture;
    public Color color;
    public float reflectance;

    public Material(Texture texture, float reflectance) {
        this.texture = texture;
        this.color = Color.WHITE;
        this.reflectance = reflectance;
    }

    public Material(Color color, float reflectance) {
        this.color = color;
        this.reflectance = reflectance;
    }

    public void setAsUniform (Shader shader) {
        shader.setUniform("defColor", color);
        shader.setUniform("reflectance", reflectance);
        shader.setUniform("hasTexture", texture != null ? 1 : 0);
    }

    public static void createUniform (Shader shader) throws Exception {
        shader.createUniform("defColor");
        shader.createUniform("reflectance");
        shader.createUniform("hasTexture");
    }

    public void cleanup () {
        if (texture != null) {
            texture.cleanup();
        }
    }
}
