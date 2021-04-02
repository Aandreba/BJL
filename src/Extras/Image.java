package Extras;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Image {
    private BufferedImage buffer;

    public Image (int width, int height) {
        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
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

    public boolean write (String path, String format) throws IOException {
        return ImageIO.write(buffer, format, new File(path));
    }
}
