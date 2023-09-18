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
