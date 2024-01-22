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

package engine.renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.models.RawModel;
import engine.shaders.TerrainShader;
import engine.terrains.Terrain;
import engine.textures.ModelTexture;
import engine.textures.TerrainTexturePack;
import engine.toolbox.MathUtils;

/**
 * The TerrainRenderer class is responsible for rendering terrains using a terrain shader.
 */
public class TerrainRenderer {
	
	private TerrainShader shader;
	
	/**
     * Creates a new TerrainRenderer with the specified shader and projection matrix.
     *
     * @param shader           The terrain shader to use for rendering.
     * @param projectionMatrix The projection matrix for the rendering.
     */
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	/**
     * Renders a list of terrains.
     *
     * @param terrains The list of terrains to render.
     */
	public void render(List<Terrain> terrains, Matrix4f toShadowSpace) {
		shader.loadToShadowSpaceMatrix(toShadowSpace);
		for(Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
					GL11.GL_UNSIGNED_INT, 0);
			
			unbindTexturedModel();
		}
	}
	
	/**
     * Prepares a terrain for rendering by binding its model and enabling vertex attributes.
     *
     * @param terrain The terrain to prepare.
     */
	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
		
		GL30.glBindVertexArray(rawModel.getVaoID());
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		bindTextures(terrain);
		
		shader.loadShineVariables(1, 0);
	}
	
	/**
     * Binds the textures associated with the terrain to texture units.
     *
     * @param terrain The terrain containing the textures to bind.
     */
	private void bindTextures(Terrain terrain) {
		TerrainTexturePack texturePack = terrain.getTexturePack();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
	}
	
	/**
     * Unbinds the textured model by disabling vertex attributes and unbinding the VAO.
     */
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		
		GL30.glBindVertexArray(0);
	}
	
	/**
     * Loads the model matrix for the terrain into the shader.
     *
     * @param terrain The terrain for which to load the model matrix.
     */
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()),
				0, 0, 0, 1);
		
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
