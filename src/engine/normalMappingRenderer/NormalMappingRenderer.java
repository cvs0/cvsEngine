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

package engine.normalMappingRenderer;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import engine.entities.Camera;
import engine.entities.DefaultCamera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.models.RawModel;
import engine.models.TexturedModel;
import engine.renderEngine.MasterRenderer;
import engine.textures.ModelTexture;
import engine.toolbox.MathUtils;

public class NormalMappingRenderer {

	private NormalMappingShader shader;

	/**
     * Creates a NormalMappingRenderer with the specified projection matrix.
     * 
     * @param projectionMatrix The projection matrix used for rendering.
     */
    public NormalMappingRenderer(Matrix4f projectionMatrix) {
        this.shader = new NormalMappingShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }

    /**
     * Renders entities with normal mapping.
     * 
     * @param entities   A map of textured models and their corresponding entities to render.
     * @param clipPlane  A vector representing the clipping plane for water rendering.
     * @param lights     A list of light sources in the scene.
     * @param camera     The camera used for rendering.
     */
    public void render(Map<TexturedModel, List<Entity>> entities, Vector4f clipPlane, List<Light> lights, Camera camera) {		shader.start();
		prepare(clipPlane, lights, camera);
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		shader.stop();
	}
	
    /**
     * Cleans up resources used by the renderer.
     */
    public void cleanUp() {
		shader.cleanUp();
	}

    /**
     * Prepares a textured model for rendering by binding its VAO, enabling vertex attributes, and loading shader uniforms.
     * 
     * @param model The textured model to prepare for rendering.
     */
    private void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel();
        
        GL30.glBindVertexArray(rawModel.getVaoID());
        
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        
        ModelTexture texture = model.getTexture();
        
        shader.loadNumberOfRows(texture.getNumberOfRows());
        
        if (texture.isHasTransparency()) {
            MasterRenderer.disableCulling();
        }
        
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
        
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getNormalMap());
    }

    /**
     * Unbinds the currently prepared textured model by disabling vertex attributes and binding VAO to 0.
     */
    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();
        
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        
        GL30.glBindVertexArray(0);
    }

    /**
     * Prepares an entity instance for rendering by loading its transformation matrix and texture offset.
     * 
     * @param entity The entity to prepare for rendering.
     */
    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
                entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }

    /**
     * Prepares the renderer by loading various shader uniforms including the clipping plane, sky color, lights, and view matrix.
     * 
     * @param clipPlane The vector representing the clipping plane for water rendering.
     * @param lights    A list of light sources in the scene.
     * @param camera    The camera used for rendering.
     */
    private void prepare(Vector4f clipPlane, List<Light> lights, Camera camera) {
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColour(MasterRenderer.RED, MasterRenderer.GREEN, MasterRenderer.BLUE);
        
        Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);

        shader.loadLights(lights, viewMatrix);
        shader.loadViewMatrix(viewMatrix);
    }
}
