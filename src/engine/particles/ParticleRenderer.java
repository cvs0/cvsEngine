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

package engine.particles;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.entities.Camera;
import engine.models.RawModel;
import engine.renderEngine.Loader;
import engine.toolbox.MathUtils;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	
	private RawModel quad;
	private ParticleShader shader;
	
	/**
     * Creates a ParticleRenderer and initializes it with a loader and a projection matrix.
     *
     * @param loader           The loader for loading particle textures.
     * @param projectionMatrix The projection matrix for rendering particles.
     */
    protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix) {
        quad = loader.loadToVAO(VERTICES, 2);
        shader = new ParticleShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    
    /**
     * Renders particles using the specified particle textures and camera.
     *
     * @param particles The particles to render, organized by texture.
     * @param camera    The camera used for rendering.
     */
    protected void render(Map<ParticleTexture, List<Particle>> particles, Camera camera) {
        Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);
        prepare();
        
        for (ParticleTexture texture : particles.keySet()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
            
            for (Particle particle : particles.get(texture)) {
                updateModelViewMatrix(particle.getPosition(), particle.getRotation(),
                    particle.getScale(), viewMatrix);
                
                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
            }
        }
        
        finishRendering();
    }
    
    /**
     * Cleans up resources used by the ParticleRenderer.
     */
    protected void cleanUp() {
        shader.cleanUp();
    }
	
    /**
     * Updates the model-view matrix for rendering particles.
     *
     * @param position    The position of the particle.
     * @param rotation    The rotation of the particle.
     * @param scale       The scale of the particle.
     * @param viewMatrix  The view matrix.
     */
    private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix) {
        Matrix4f modelMatrix = new Matrix4f();
        Matrix4f.translate(position, modelMatrix, modelMatrix);
        
        modelMatrix.m00 = viewMatrix.m00;
        modelMatrix.m01 = viewMatrix.m10;
        modelMatrix.m02 = viewMatrix.m20;
        modelMatrix.m10 = viewMatrix.m01;
        modelMatrix.m11 = viewMatrix.m11;
        modelMatrix.m12 = viewMatrix.m21;
        modelMatrix.m20 = viewMatrix.m02;
        modelMatrix.m21 = viewMatrix.m12;
        modelMatrix.m22 = viewMatrix.m22;
        
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
        Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
        shader.loadModelViewMatrix(modelViewMatrix);
    }
    
    /**
     * Prepares OpenGL for rendering particles by enabling necessary settings.
     */
    private void prepare() {
        shader.start();
        
        GL30.glBindVertexArray(quad.getVaoID());
        
        GL20.glEnableVertexAttribArray(0);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);
    }
    
    /**
     * Finishes the rendering process by disabling settings and cleaning up.
     */
    private void finishRendering() {
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        
        shader.stop();
    }
}
