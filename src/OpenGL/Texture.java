package OpenGL;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    final public int id;
    final public int width, height, channels;

    public Texture (String file) throws Exception {
        ByteBuffer buffer;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = stbi_load(file, w, h, c, 4);
            if (buffer == null) {
                throw new Exception("Image file [" + file  + "] not loaded: " + stbi_failure_reason());
            }

            this.width = w.get();
            this.height = h.get();
            this.channels = c.get();
        }

        this.id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    public Texture (Color color) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(16);
        for (int i=0;i<4;i++) {
            buffer.put(i * 4, color.getRed() / 255f);
            buffer.put(i * 4 + 1, color.getGreen() / 255f);
            buffer.put(i * 4 + 2, color.getBlue() / 255f);
            buffer.put(i * 4 + 3, color.getAlpha() / 255f);
        }

        this.id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 2, 2, 0, GL_RGBA, GL_FLOAT, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        this.width = 2;
        this.height = 2;
        this.channels = 4;
    }

    public void bind () {
        glBindTexture(GL_TEXTURE_2D, this.id);
    }

    public void cleanup () {
        glDeleteTextures(this.id);
    }
}
