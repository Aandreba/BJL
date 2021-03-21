package OpenGL.Shaders;

import OpenGL.Light.LightPoint;

import java.io.File;

public class DefShader extends Shader {
    public DefShader() throws Exception {
        super();
        this.createVertexShader(new File("src/OpenGL/GL/vertex.vert"));
        this.createFragmentShader(new File("src/OpenGL/GL/fragment.frag"));

        this.createUniform("project");
        this.createUniform("view");
        this.createUniform("transform");
        this.createUniform("textureSampler");
        this.createUniform("defColor");
        this.createUniform("hasTexture");

        LightPoint.createArrayUniform("points", 5, this);
    }
}
