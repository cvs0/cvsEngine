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

/**
 * Represents a free camera for navigation within a 3D environment.
 */
public class FreeCamera {
    
    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;
    
    /**
     * Constructs a new FreeCamera object.
     */
    public FreeCamera(){}
    
    /**
     * Moves the camera based on user input, allowing navigation in a 3D environment.
     * 
     * This method checks for keyboard input and adjusts the camera's position accordingly.
     * W and S keys control forward and backward movement, A and D keys control strafing,
     * Space key moves upward, and Left Shift key moves downward.
     */
    public void move(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.z -= 0.02f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x += 0.02f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x -= 0.02f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z += 0.02f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            position.y += 0.02f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.y -= 0.02f;
        }
    }

    /**
     * Retrieves the current position of the camera.
     * 
     * @return A {@link Vector3f} representing the camera's current position.
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Retrieves the pitch (vertical orientation) of the camera.
     * 
     * @return The camera's current pitch angle.
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Retrieves the yaw (horizontal orientation) of the camera.
     * 
     * @return The camera's current yaw angle.
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Retrieves the roll (tilt) of the camera.
     * 
     * @return The camera's current roll angle.
     */
    public float getRoll() {
        return roll;
    }
}
