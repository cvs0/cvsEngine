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

import org.lwjgl.util.vector.Vector3f;

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
	
	private float elapsedTime = 0;

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
    protected boolean update() {
        velocity.y += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
        Vector3f change = new Vector3f(velocity);

        change.scale(DisplayManager.getFrameTimeSeconds());

        Vector3f.add(change, position, position);

        elapsedTime += DisplayManager.getFrameTimeSeconds();

        return elapsedTime < lifeLength;
    }
}
