/**
 * CvsEngine
 *
 * @author cvs0
 * @version 1.0.0
 *
 * @license
 * MIT License
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package engine.water;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.entities.Camera;
import engine.entities.Light;
import engine.models.RawModel;
import engine.renderEngine.DisplayManager;
import engine.renderEngine.Loader;
import engine.toolbox.MathUtils;

/**
 * Represents a WaterRenderer object which is used for handling the shaders and water rendering.
 */
public class WaterRenderer {
    
    private static final String DUDV_MAP = "waterDUDV";
    private static final String NORMAL_MAP = "normal";
    private static final float WAVE_SPEED = 0.03f;

    private RawModel quad;
    private WaterShader shader;
    private WaterFrameBuffers fbos;
    
    private float moveFactor = 0;
    
    private int dudvTexture;
    private int normalMap;

    /**
     * Creates a new WaterRenderer.
     * 
     * @param loader           The loader used to load textures and models.
     * @param shader           The shader program for rendering water.
     * @param projectionMatrix The projection matrix for the scene.
     * @param fbos             The framebuffers for reflection and refraction.
     */
    public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;
        dudvTexture = loader.loadTexture(DUDV_MAP);
        normalMap = loader.loadTexture(NORMAL_MAP);
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);
    }

    /**
     * Renders the water tiles in the scene.
     * 
     * @param water  The list of water tiles to render.
     * @param camera The camera used for rendering.
     * @param sun    The light source in the scene.
     */
    public void render(List<WaterTile> water, Camera camera, Light sun) {
        prepareRender(camera, sun);    
        for (WaterTile tile : water) {
            Matrix4f modelMatrix = MathUtils.createTransformationMatrix(
                    new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
                    WaterTile.TILE_SIZE);
            shader.loadModelMatrix(modelMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
        }
        unbind();
    }
    
    /**
     * Prepares the renderer for rendering water.
     * 
     * @param camera The camera used for rendering.
     * @param sun    The light source in the scene.
     */
    private void prepareRender(Camera camera, Light sun){
        shader.start();
        shader.loadViewMatrix(camera);
        moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeSeconds();
        moveFactor %= 1;
        shader.loadMoveFactor(moveFactor);
        shader.loadLight(sun);
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    /**
     * Unbinds VAOs and disables vertex attributes after rendering water.
     */
    private void unbind(){
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        GL11.glDisable(GL11.GL_BLEND);
        shader.stop();
    }

    /**
     * Sets up the VAO (Vertex Array Object) for rendering water.
     * 
     * @param loader The loader used for loading vertex data.
     */
    private void setUpVAO(Loader loader) {
        // Just x and z vertex positions here, y is set to 0 in the vertex shader
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVAO(vertices, 2);
    }
}