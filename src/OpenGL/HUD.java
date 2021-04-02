package OpenGL;

import Extras.Files;
import OpenGL.Extras.Matrix.StatMatrix4;
import OpenGL.Shaders.Shader;

import java.util.ArrayList;
import java.util.Collections;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class HUD extends ArrayList<GameObject> {
    class HUDShader extends Shader {
        public HUDShader () throws Exception {
            super(true);

            this.createVertexShader(Files.loadResource("/OpenGL/GL/hud_vertex.vert"));
            this.createFragmentShader(Files.loadResource("/OpenGL/GL/hud_fragment.frag"));

            this.createUniform("project");
            this.createUniform("transform");
            this.createUniform("textureSampler");
            Material.createUniform(this);
        }
    }

    final public HUDShader shader;
    private Window window;

    public HUD (GameObject... obj) throws Exception {
        super();
        Collections.addAll(this, obj);

        this.shader = new HUDShader();
    }

    protected void setWindow (Window window) {
        this.window = window;
    }

    public void render () {
        shader.bind();
        shader.setUniform("project", 1 / window.getAspectRatio());
        shader.setUniform("textureSampler", 0);

        for (GameObject obj: this) {
            render(obj);
        }

        shader.unbind();
    }

    private void render (GameObject object) {
        shader.setUniform("transform", object.transform.matrix);
        object.material.setAsUniform(shader);

        if (object.material.texture != null) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, object.material.texture.id);
        }

        // Bind to the VAO
        glBindVertexArray(object.mesh.getVao());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        // Draw the vertices
        glDrawElements(GL_TRIANGLES, object.mesh.getTriangleCount() * 3, GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup () {
        this.shader.cleanup();
        for (GameObject obj: this) {
            obj.cleanup();
        }
    }
}
