package engine.guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import engine.models.RawModel;
import engine.renderEngine.Loader;
import engine.toolbox.MathUtils;

/**
 * This class is responsible for rendering GUI elements on the screen.
 */
public class GuiRenderer {

    private final RawModel quad;
    private GuiShader shader;

    /**
     * Constructs a GuiRenderer using a loader.
     *
     * @param loader The loader used to load VAOs (Vertex Array Objects).
     */
    public GuiRenderer(Loader loader) {
        float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };

        quad = loader.loadToVAO(positions, 2);
        shader = new GuiShader();
    }

    /**
     * Renders a list of GUI textures on the screen.
     *
     * @param guis A list of GuiTexture objects to be rendered.
     */
    public void render(List<GuiTexture> guis) {
        shader.start();

        GL30.glBindVertexArray(quad.getVaoID());

        GL20.glEnableVertexAttribArray(0);

        for (GuiTexture gui : guis) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            Matrix4f matrix = MathUtils.createTransformationMatrix(gui.getPosition(), gui.getScale());

            shader.loadTransformation(matrix);

            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);

        GL30.glBindVertexArray(0);

        GL20.glEnableVertexAttribArray(0);

        shader.stop();
    }

    /**
     * Cleans up resources used by the GuiRenderer.
     */
    public void cleanUp() {
        shader.cleanUp();
    }
}
