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

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.entities.Camera;
import engine.models.RawModel;
import engine.renderEngine.Loader;
import engine.toolbox.MathUtils;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	private static final int MAX_INSTANCES = 10000;
	private static final int INSTANCE_DATA_LENGTH = 21;
	
	private static final FloatBuffer buffer = BufferUtils
			.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
	
	private RawModel quad;
	private ParticleShader shader;
	
	private Loader loader;
	
	private int vbo;
	private int pointer = 0;
	
	/**
     * Creates a ParticleRenderer and initializes it with a loader and a projection matrix.
     *
     * @param loader           The loader for loading particle textures.
     * @param projectionMatrix The projection matrix for rendering particles.
     */
    protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix) {
    	this.loader = loader;
    	this.vbo = loader.createEmptyVbo(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
        quad = loader.loadToVAO(VERTICES, 2);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 4, 4, INSTANCE_DATA_LENGTH, 12);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 5, 4, INSTANCE_DATA_LENGTH, 16);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 6, 4, INSTANCE_DATA_LENGTH, 20);
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
        	
        	bindTexture(texture);
        	
        	List<Particle> particleList = particles.get(texture);
        	
            pointer = 0;
            
            float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
            
            for (Particle particle : particleList) {
            	
                updateModelViewMatrix(particle.getPosition(), particle.getRotation(),
                    particle.getScale(), viewMatrix, vboData);
                updateTexCoordInfo(particle, vboData);
            }
            
            loader.updateVbo(vbo, vboData, buffer);
            
            GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());
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
     * Updates the texture coordinate info.
     *
     * @param particle   The particle to update / store the info of.
     * @param data  The float array to store the texture coordinate data in.
     */
    private void updateTexCoordInfo(Particle particle, float[] data) {
    	data[pointer++] = particle.getTexOffset1().x;
    	data[pointer++] = particle.getTexOffset1().y;
    	data[pointer++] = particle.getTexOffset2().x;
    	data[pointer++] = particle.getTexOffset2().y;
    	data[pointer++] = particle.getBlend();
    }
    
    /**
     * Binds the textures to the particle and checks for blend type.
     * 
     * @param texture The ParticleTexture object
     */
    private void bindTexture(ParticleTexture texture) {
    	if(texture.isAdditive()) {
    		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
    	} else {
    		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	}
    	
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        
        shader.loadNumberOfRows(texture.getNumberOfRows());
    }
	
    /**
     * Updates the model-view matrix for rendering particles.
     *
     * @param position    The position of the particle.
     * @param rotation    The rotation of the particle.
     * @param scale       The scale of the particle.
     * @param viewMatrix  The view matrix.
     */
    private void updateModelViewMatrix(Vector3f position, float rotation, float scale,
    		Matrix4f viewMatrix, float[] vboData) {
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
        
        Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
        
        storeMatrixData(modelViewMatrix, vboData);
    }
    
    /**
     * Stores data inside a Matrix4f
     *
     * @param matrix   The Matrix4f to store the specified data in.
     * @param vboData  The float array for the VBO data.
     */
    private void storeMatrixData(Matrix4f matrix, float[] vboData) {
    	  vboData[pointer++] = matrix.m00;
    	  vboData[pointer++] = matrix.m01;
    	  vboData[pointer++] = matrix.m02;
    	  vboData[pointer++] = matrix.m03;
    	  vboData[pointer++] = matrix.m10;
    	  vboData[pointer++] = matrix.m11;
    	  vboData[pointer++] = matrix.m12;
    	  vboData[pointer++] = matrix.m13;
    	  vboData[pointer++] = matrix.m20;
    	  vboData[pointer++] = matrix.m21;
    	  vboData[pointer++] = matrix.m22;
    	  vboData[pointer++] = matrix.m23;
    	  vboData[pointer++] = matrix.m30;
    	  vboData[pointer++] = matrix.m31;
    	  vboData[pointer++] = matrix.m32;
    	  vboData[pointer++] = matrix.m33;
    }
    
    /**
     * Prepares OpenGL for rendering particles by enabling necessary settings.
     */
    private void prepare() {
        shader.start();
        
        GL30.glBindVertexArray(quad.getVaoID());
        
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL20.glEnableVertexAttribArray(4);
        GL20.glEnableVertexAttribArray(5);
        GL20.glEnableVertexAttribArray(6);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDepthMask(false);
    }
    
    /**
     * Finishes the rendering process by disabling settings and cleaning up.
     */
    private void finishRendering() {
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL20.glDisableVertexAttribArray(4);
        GL20.glDisableVertexAttribArray(5);
        GL20.glDisableVertexAttribArray(6);
        
        GL30.glBindVertexArray(0);
        
        shader.stop();
    }
}
