package OpenGL;

import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
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

    public void bind () {
        glBindTexture(GL_TEXTURE_2D, this.id);
    }

    public void cleanup () {
        glDeleteTextures(this.id);
    }
}
