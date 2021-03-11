package OpenGL;

import OpenGL.Mesh.Mesh;
import Units.Angle;
import Units.Time;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.awt.*;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public abstract class Window extends ArrayList<Mesh> implements Runnable {
    final private long id;

    final public String title;
    private int width, height;
    private boolean resized, vSync;
    private Color bkgColor;

    public Camera mainCamera;

    public Window (String title, int width, int height, boolean vSync) {
        super();

        this.title = title;
        this.width = width;
        this.height = height;
        this.resized = false;
        this.vSync = vSync;
        this.mainCamera = new Camera(this, new Angle(90, Angle.Type.Degrees), 0.01f, 1000f);

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        this.id = glfwCreateWindow(width, height, title, NULL, NULL);

        if (this.id == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup resize callback
        glfwSetFramebufferSizeCallback(this.id, (window, w, h) -> {
            this.width = w;
            this.height = h;
            this.resized = true;
        });

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(this.id, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        this.pushFrame();
    }

    public void pushFrame () {
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(this.id, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    this.id,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(this.id);

        // Enable v-sync
        if (this.vSync) {
            glfwSwapInterval(1);
        }

        // Make the window visible
        glfwShowWindow(this.id);

        GL.createCapabilities();
    }

    public abstract void update (Time deltaTime);

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public void setVSync(boolean vSync) {
        this.vSync = vSync;
    }

    public boolean isvSync() {
        return vSync;
    }

    public Color getBkgColor() {
        return bkgColor;
    }

    public void setBackgroundColor (int red, int green, int blue, int alpha) {
        this.bkgColor = new Color(red, green, blue, alpha);
        glClearColor(this.bkgColor.getRed() / 255f,this.bkgColor.getGreen() / 255f,this.bkgColor.getBlue() / 255f,this.bkgColor.getAlpha() / 255f);
    }

    public boolean windowShouldClose () {
        return glfwWindowShouldClose(this.id);
    }

    public void render () {
        for (Mesh mesh: this) {
            mesh.render(this);
        }
    }

    public void updateFrame () {
        glfwSwapBuffers(this.id);
        glfwPollEvents();
    }

    public void clear () {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void loop () {
        long lastTime = System.nanoTime();
        Time deltaTime;

        boolean running = true;
        while (running && !windowShouldClose()) {
            long thisTime = System.nanoTime();
            deltaTime = new Time(thisTime - lastTime, Time.Type.Nanoseconds);

            clear();
            update(deltaTime);
            render();
            updateFrame();

            lastTime = thisTime;
        }
    }

    @Override
    public void run() {
        try {
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    public void cleanup () {
        for (Mesh mesh: this) {
            mesh.cleanup();
        }
    }
}
