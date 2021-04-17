package Extras;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Image {
    final public static Font defaultFont = new Font("Arial", Font.PLAIN, 1);

    public class Text {
        public int x, y;
        public String value;
        public Font font;
        public Color color;
        public float size;

        public Text (int x, int y, String value, Font font, Color color, float size) {
            this.x = x;
            this.y = y;
            this.value = value;
            this.font = font;
            this.color = color;
            this.size = size;
        }
    }

    private BufferedImage buffer;
    private ArrayList<Text> texts;

    public Image (int width, int height) {
        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.texts = new ArrayList<>();
    }

    public Image (BufferedImage buffer) {
        this.buffer = buffer;
    }

    public Image (URL path) throws IOException {
        this(ImageIO.read(path));
    }

    public Image (File path) throws IOException {
        this(ImageIO.read(path));
    }

    public Image (InputStream is) throws IOException {
        this(ImageIO.read(is));
    }

    public Image (ImageInputStream is) throws IOException {
        this(ImageIO.read(is));
    }

    public BufferedImage getBuffer() {
        return buffer;
    }

    public int getWidth () {
        return buffer.getWidth();
    }

    public int getHeight () {
        return buffer.getHeight();
    }

    public Color getPixel (int x, int y) {
        int p = buffer.getRGB(x, y);

        int a = (p>>24) & 0xff;
        int r = (p>>16) & 0xff;
        int g = (p>>8) & 0xff;
        int b = p & 0xff;

        return new Color(r, g, b, a);
    }

    public void setPixel (int x, int y, Color color) {
        int a = color.getAlpha();
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        int p = (a<<24) | (r<<16) | (g<<8) | b;
        buffer.setRGB(x, y, p);
    }

    public Text getText (int id) {
        return texts.get(id);
    }

    public Text getText (String value) {
        for (Text text: texts) {
            if (text.value.equals(value)) {
                return text;
            }
        }

        return null;
    }

    public Text addText (int x, int y, String value, Font font, Color color, float size) {
        Text text = new Text(x, y, value, font, color, size);

        if (this.texts.add(text)) {
            return text;
        } else {
            return null;
        }
    }

    public ArrayList<Text> getTexts (String value) {
        ArrayList<Text> texts = new ArrayList<>(this.texts);
        texts.removeIf(x -> !x.value.equals(value));

        return texts;
    }

    public boolean write (String path, String format) throws IOException {
        BufferedImage buffer = new BufferedImage(this.buffer.getWidth(), this.buffer.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graph = buffer.getGraphics();

        buffer.setData(this.buffer.getData());
        for (Text text: texts) {
            graph.setColor(text.color);
            graph.setFont(text.font.deriveFont(text.size));
            graph.drawString(text.value, text.x, text.y);
        }
        graph.dispose();

        return ImageIO.write(buffer, format, new File(path));
    }
}
