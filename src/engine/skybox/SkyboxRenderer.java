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

package engine.skybox;

import java.lang.Math;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import engine.entities.Camera;
import engine.models.RawModel;
import engine.renderEngine.DisplayManager;
import engine.renderEngine.Loader;

/**
 * The SkyboxRenderer class is responsible for rendering the skybox in the game world.
 * It handles loading and rendering the skybox textures, including day and night transitions.
 */
public class SkyboxRenderer {
	private static final float SIZE = 500f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};

    private static final int NIGHT_START = 0;
    private static final int DAWN_START = 5000;
    private static final int DAY_START = 8000;
    private static final int DUSK_START = 21000;
    private static final int DAY_LENGTH = 24000;

    private static String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};
	private static String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};
	
	private RawModel cube;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
	private float time = 0;
	
	/**
     * Constructs a SkyboxRenderer instance.
     *
     * @param loader           The loader used to load textures and models.
     * @param projectionMatrix The projection matrix for rendering.
     */
    public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
        cube = loader.loadToVAO(VERTICES, 3);
        texture = loader.loadCubeMap(TEXTURE_FILES);
        nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
        shader = new SkyboxShader();
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Renders the skybox based on the camera's view and time of day.
     *
     * @param camera The camera used to render the scene.
     * @param r      The red component of the fog color.
     * @param g      The green component of the fog color.
     * @param b      The blue component of the fog color.
     */
    public void render(Camera camera, float r, float g, float b) {
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadFogColour(r, g, b);

        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        bindTextures();

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        shader.stop();
    }

    /**
     * Binds the appropriate day or night textures to the skybox based on the time of day.
     */
    private void bindTextures() {
        time += DisplayManager.getFrameTimeSeconds() * 1000;
        time %= DAY_LENGTH;

        int texture1;
        int texture2;
        float blendFactor;

        if (isNight(time)) {
            texture1 = nightTexture;
            texture2 = nightTexture;
            blendFactor = calculateBlendFactor(time, NIGHT_START, DAWN_START);
        } else if (isDawn(time)) {
            texture1 = nightTexture;
            texture2 = texture;
            blendFactor = calculateBlendFactor(time, DAWN_START, DAY_START);
        } else if (isDay(time)) {
            texture1 = texture;
            texture2 = texture;
            blendFactor = calculateBlendFactor(time, DAY_START, DUSK_START);
        } else {
            texture1 = texture;
            texture2 = nightTexture;
            blendFactor = calculateBlendFactor(time, DUSK_START, DAY_LENGTH);
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);

        shader.loadBlendFactor(clampBlendFactor(blendFactor));
    }

    private boolean isNight(float time) {
        return time >= NIGHT_START && time < DAWN_START;
    }

    private boolean isDawn(float time) {
        return time >= DAWN_START && time < DAY_START;
    }

    private boolean isDay(float time) {
        return time >= DAY_START && time < DUSK_START;
    }

    private float calculateBlendFactor(float time, int start, int end) {
        return (time - start) / (float) (end - start);
    }

    private float clampBlendFactor(float blendFactor) {
        return Math.max(0, Math.min(blendFactor, 1));
    }

}