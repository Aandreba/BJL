package OpenGL.Primitives.Objects;

import OpenGL.GameObject;
import OpenGL.Material;
import OpenGL.Mesh;
import OpenGL.Texture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class Text extends GameObject {
    final private static int fontSize = 128;
    final public static HashMap<String, Texture> fonts = new HashMap<>();

    private String value;

    public Text (String value, Texture font) {
        super(createMesh(value, font), new Material(font, 1f));
        this.value = value;
    }

    public Text (String value, String font) throws Exception {
        this(value, fonts.getOrDefault(font, createFontTexture(font)));
    }

    public String getValue() {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
        this.mesh.cleanup();
        this.mesh = createMesh(this.value, this.material.texture);
    }

    private static Mesh createMesh (String text, Texture bitmap) {
        int length = text.length();
        float step = 2f / length;

        int vertices = 4 * length;
        int triangles = length * 2;

        Mesh mesh = new Mesh(vertices, triangles);
        for (int i=0;i<length;i++) {
            float pos = i * step - 1;

            int up = 4 * i;
            int down = 4 * i + 1;
            int upNext = 4 * i + 2;
            int downNext = 4 * i + 3;

            mesh.setVertex(up, pos, 1, 0);
            mesh.setVertex(down, pos, -1, 0);
            mesh.setVertex(upNext, pos + step, 1, 0);
            mesh.setVertex(downNext, pos + step, -1, 0);

            mesh.setTriangle(2 * i, up, down, downNext);
            mesh.setTriangle(2 * i + 1, downNext, upNext, up);

            float character = (text.charAt(i) - 33) / 222f;
            mesh.setTexCoord(up, character, 0);
            mesh.setTexCoord(down, character, 1);
            mesh.setTexCoord(upNext, character + 1f/222, 0);
            mesh.setTexCoord(downNext, character + 1f/222, 1);
        }

        mesh.draw();
        return mesh;
    }

    public static Texture createFontTexture (String name) throws Exception {
        // ASCII: 33 --> 254 (222 characters)
        BufferedImage image = new BufferedImage(fontSize * 222, fontSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graph = image.createGraphics();
        Font font = new Font(name, Font.PLAIN, fontSize);

        graph.setFont(font);
        for (int i=33;i<255;i++) {
            graph.drawString(""+(char) i, (i-33) * fontSize, fontSize * 0.85f);
        }
        graph.dispose();

        ImageIO.write(image, "png", new File(name+".png"));
        Texture texture =  new Texture(name+".png");
        if (!new File(name+".png").delete()) {
            System.out.println("Font couldn't be deleted");
        }

        return texture;
    }

    @Override
    public String toString() {
        return value;
    }
}
