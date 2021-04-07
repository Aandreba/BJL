package OpenGL;

;
import OpenGL.Extras.Gravity.Gravity;
import OpenGL.Extras.Gravity.Normal;
import OpenGL.Input.Input;
import OpenGL.Light.DirectionalLight;
import OpenGL.Light.PointLight;
import OpenGL.Light.SpotLight;
import OpenGL.Shaders.Shader;
import Units.Time;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.awt.*;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public abstract class Window extends ArrayList<GameObject> implements Runnable {
    final public long id;
    final public String title;
    final public Input input;
    private Camera mainCamera;
    private HUD hud;

    private int width, height;
    private boolean resized, vSync;
    private Color bkgColor;
    public Shader shader;
    public Gravity gravityType = new Normal();

    public PointLight[] points = new PointLight[5];
    public DirectionalLight[] directionals = new DirectionalLight[5];
    public SpotLight[] spots = new SpotLight[5];

    public Window (String title, int width, int height, boolean vSync) throws Exception {
        super();

        this.title = title;
        this.width = width;
        this.height = height;
        this.resized = false;
        this.vSync = vSync;
        this.mainCamera = new Camera();

        init();

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

            mainCamera.setWindow(this);
            if (hud != null) {
                hud.setWindow(this);
            }
        });

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(this.id, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        this.input = new Input(this);
        this.pushFrame();
        this.shader = new Shader();
    }

    public static void init () throws IllegalStateException {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
    }

    @Override
    public boolean add (GameObject gameObject) {
        gameObject.window = this;
        return super.add(gameObject);
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof GameObject && super.remove(o)) {
            GameObject object = (GameObject) o;
            object.window = null;
            return true;
        }

        return false;
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
        //glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public abstract void update (Time deltaTime);

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getAspectRatio () {
        return width * 1f / height;
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

    public boolean isVSync() {
        return vSync;
    }

    public Color getBkgColor() {
        return bkgColor;
    }

    public void setBackgroundColor (Color color) {
        this.bkgColor = color;
        glClearColor(this.bkgColor.getRed() / 255f,this.bkgColor.getGreen() / 255f,this.bkgColor.getBlue() / 255f,this.bkgColor.getAlpha() / 255f);
    }

    public Camera getMainCamera() {
        return mainCamera;
    }

    public void setMainCamera (Camera mainCamera) {
        this.mainCamera = mainCamera;
        this.mainCamera.setWindow(this);
    }

    public HUD getHud () {
        return hud;
    }

    public void setHud (HUD hud) {
        this.hud = hud;
        this.hud.setWindow(this);
    }

    public HUD createHUD () throws Exception {
        HUD hud = new HUD();
        setHud(hud);

        return hud;
    }

    public boolean windowShouldClose () {
        return glfwWindowShouldClose(this.id);
    }

    public void render (Time delta) {
        if (isResized()) {
            glViewport(0, 0, getWidth(), getHeight());
            setResized(false);
        }

        shader.bind();

        shader.setUniform("project", mainCamera.getProjectionMatrix());
        shader.setUniform("view", mainCamera.view);
        shader.setUniform("textureSampler", 0);

        for (int i = 0; i< points.length; i++) {
            if (points[i] != null) {
                points[i].setAsUniform("points", i, shader);
            }

            if (directionals[i] != null) {
                directionals[i].setAsUniform("directionals", i, shader);
            }

            if (spots[i] != null) {
                spots[i].setAsUniform("spots", i, shader);
            }
        }

        Rigidbody.collisions.clear();

        // Setup initial velocities
        for (GameObject object: this) {
            if (object.rb != null) {
                if (object.rb.applyGravity) {
                    object.rb.addAcceleration(gravityType.gravityAccFor(object), delta);
                }

                object.rb.setLastVelocity(delta);
                object.rb.setLastAngularVelocity(delta);
            }
        }

        for (GameObject object: this) {
            if (object.rb != null) {
                object.rb.update(this, delta);
            }
        }

        for (GameObject object: this) {
            if (object.rb != null) {
                object.rb.applyChange(object, delta);
            }

            object.render();
        }

        shader.unbind();
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
            input.update();
            update(deltaTime);
            render(deltaTime);

            if (hud != null) {
                hud.render();
            }

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
            cleanup();
        } finally {
            cleanup();
        }
    }

    public void cleanup () {
        this.shader.cleanup();

        if (this.hud != null) {
            this.hud.cleanup();
        }

        for (GameObject object: this) {
            object.cleanup();
        }
    }
}
