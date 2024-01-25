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

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.entities.Camera;
import engine.entities.DefaultCamera;
import engine.entities.Player;
import engine.renderEngine.DisplayManager;

public class Particle {
	private Vector3f position;
	private Vector3f velocity;
	
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;
	
	private ParticleTexture texture;
	
	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend;

	private float elapsedTime = 0;
	private float distance;
	
	private Vector3f reusableChange = new Vector3f();

	/**
     * Creates a new particle with the specified properties and adds it to the particle system.
     *
     * @param texture       The texture of the particle.
     * @param position      The initial position of the particle.
     * @param velocity      The initial velocity of the particle.
     * @param gravityEffect The effect of gravity on the particle's movement.
     * @param lifeLength    The total life span of the particle.
     * @param rotation      The initial rotation of the particle.
     * @param scale         The initial scale of the particle.
     */
    public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
                    float scale) {
        this.texture = texture;
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        
        ParticleMaster.addParticle(this);
    }
    
    /**
     * Gets the particle's distance from the camera.
     *
     * @return The particle's distance from the camera.
     */
    public float getDistance() {
		return distance;
	}

    /**
     * Gets the first texture offset for the particle.
     *
     * @return The particle's first texture offset.
     */
    public Vector2f getTexOffset1() {
		return texOffset1;
	}

    /**
     * Gets the 2nd texture offset for the particle.
     *
     * @return The particle's 2nd texture offset.
     */
	public Vector2f getTexOffset2() {
		return texOffset2;
	}

	/**
     * Gets the blend factor for the particle texture.
     *
     * @return The particle's blend factor.
     */
	public float getBlend() {
		return blend;
	}
	
    /**
     * Gets the texture of the particle.
     *
     * @return The particle's texture.
     */
    public ParticleTexture getTexture() {
        return texture;
    }

    /**
     * Gets the current position of the particle.
     *
     * @return The current position vector of the particle.
     */
    protected Vector3f getPosition() {
        return position;
    }

    /**
     * Gets the current rotation of the particle.
     *
     * @return The current rotation angle of the particle.
     */
    protected float getRotation() {
        return rotation;
    }

    /**
     * Gets the current scale of the particle.
     *
     * @return The current scale factor of the particle.
     */
    protected float getScale() {
        return scale;
    }

    /**
     * Updates the particle's position and other properties.
     *
     * @return True if the particle is still alive (within its life span), false otherwise.
     */
    protected boolean update(Camera camera) {
        velocity.y += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
        
        reusableChange.set(velocity);
        reusableChange.scale(DisplayManager.getFrameTimeSeconds());
        
        Vector3f.add(reusableChange, position, position);
        updateTextureCoordInfo();
        
        distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
        elapsedTime += DisplayManager.getFrameTimeSeconds();
        
        return elapsedTime < lifeLength;
    }
    
    /**
     * Updates the particle's texture coordinate information for the animations.
     */
    private void updateTextureCoordInfo() {
    	float lifeFactor = elapsedTime / lifeLength;
    	int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
    	float atlasProgression = lifeFactor * stageCount;
    	int index1 = (int) Math.floor(atlasProgression);
    	int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
    	
    	this.blend = atlasProgression % 1;
    	
    	setTextureOffset(texOffset1, index1);
    	setTextureOffset(texOffset2, index2);
    }
    
    /**
     * Sets the texture offset for the animation atlas.
     * @param offset The vector2f offset for the texture.
     * @param index The integer index for the columns / row calculation for the texture atlas.
     */
    private void setTextureOffset(Vector2f offset, int index) {
    	int column = index % texture.getNumberOfRows();
    	int row = index / texture.getNumberOfRows();
    	
    	offset.x = (float) column / texture.getNumberOfRows();
    	offset.y = (float) row / texture.getNumberOfRows();
    }
}
