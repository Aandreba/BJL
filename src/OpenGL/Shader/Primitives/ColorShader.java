package OpenGL.Shader.Primitives;

import Extras.Files;
import OpenGL.Shader.Shader;

import java.awt.*;
import java.io.File;

public class ColorShader extends Shader {
    private String fragmentCode;
    private Color color;

    public ColorShader (Color color) throws Exception {
        super();
        this.fragmentCode = Files.loadFile("src/OpenGL/Shader/GLCode/fragment.frag");
        this.createVertexShader(new File("src/OpenGL/Shader/GLCode/vertex.vert"));

        this.createUniform("projection");
        this.setColor(color);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        applyColor();
    }

    private void applyColor () {
        float r = this.color.getRed() / 255f;
        float g = this.color.getGreen() / 255f;
        float b = this.color.getBlue() / 255f;
        float a = this.color.getAlpha() / 255f;

        String code = this.fragmentCode.replace("vec4(0.0, 0.0, 0.0, 1.0)", "vec4("+r+", "+g+", "+b+", "+a+")");

        try {
            this.createFragmentShader(code);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
