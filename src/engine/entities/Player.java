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

package engine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import engine.models.TexturedModel;
import engine.renderEngine.DisplayManager;
import engine.terrains.Terrain;

public class Player extends Entity {
	
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	public static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	private static final float TERRAIN_HEIGHT = 0;
	
	private float upwardsSpeed = 0;
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	
	private boolean isInAir = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	/**
	 * Moves the entity based on its current speed and orientation, while considering the terrain.
	 *
	 * This method calculates the entity's movement based on its current speed and orientation.
	 * It adjusts the entity's position to move along the terrain, applies gravity, and ensures
	 * that the entity stays above the terrain surface.
	 *
	 * @param terrain The terrain that the entity is interacting with.
	 */
	public void move(Terrain terrain) {
		checkInputs();
		
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		
		super.increasePosition(dx, 0, dz);
		
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		
		if(super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			
			super.getPosition().y = terrainHeight;
		}
	}
	
	/**
	 * Initiates a jump if the entity is currently on the ground.
	 * 
	 * This method allows the entity to jump by setting its upward speed to a predefined
	 * jump power if it is currently on the ground. The entity's state is updated to indicate
	 * that it is in the air after jumping.
	 */
	private void jump() {
	    if (!isInAir) {
	        this.upwardsSpeed = JUMP_POWER;
	        isInAir = true;
	    }
	}

	/**
	 * Check and process user input for entity movement.
	 * 
	 * This method checks for keyboard input to determine the entity's movement behavior.
	 * It sets the current speed and turn speed based on the keys pressed, allowing the
	 * entity to move forward, backward, or turn left and right. Additionally, it triggers
	 * a jump if the space key is pressed.
	 */
	public void checkInputs() {
	    if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
	        this.currentSpeed = RUN_SPEED;
	    } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
	        this.currentSpeed = -RUN_SPEED;
	    } else {
	        this.currentSpeed = 0;
	    }
	    
	    if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
	        this.currentTurnSpeed = -TURN_SPEED;
	    } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
	        this.currentTurnSpeed = TURN_SPEED;
	    } else {
	        this.currentTurnSpeed = 0;
	    }
	    
	    if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
	        jump();
	    }
	}

}
