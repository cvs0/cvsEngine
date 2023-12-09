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
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import engine.shaders.StaticShader;

/**
 * Represents a camera used for viewing the 3D scene.
 */
public class Camera {

    private float distanceFromPlayer = 35;
    private float angleAroundPlayer = 0;

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 20;
    private float yaw = 0;
    private float roll;

    private Player player;

    /**
     * Constructs a camera associated with a player.
     *
     * @param player The player entity to which this camera is attached.
     */
    public Camera(Player player) {
        this.player = player;
    }

    /**
     * Moves the camera based on user input and player movement.
     */
    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();

        calculateCameraPosition(horizontalDistance, verticalDistance);

        yaw = 180 - (player.getRotY() + angleAroundPlayer);
        yaw %= 360;
    }

    /**
     * Inverts the camera's pitch, effectively flipping its view.
     */
    public void invertPitch() {
        this.pitch = -pitch;
    }

    /**
     * Retrieves the position of the camera.
     *
     * @return A {@link Vector3f} representing the camera's position.
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Retrieves the pitch angle of the camera.
     *
     * @return The pitch angle of the camera.
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Retrieves the yaw angle of the camera.
     *
     * @return The yaw angle of the camera.
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Retrieves the roll angle of the camera.
     *
     * @return The roll angle of the camera.
     */
    public float getRoll() {
        return roll;
    }

    /**
     * Calculates the camera's position based on the player's orientation and zoom level.
     *
     * @param horizDistance The horizontal distance from the player.
     * @param verticDistance The vertical distance from the player.
     */
    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));

        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance + 4;
    }

    /**
     * Calculates the horizontal distance between the camera and the player.
     *
     * @return The horizontal distance.
     */
    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch + 4)));
    }

    /**
     * Calculates the vertical distance between the camera and the player.
     *
     * @return The vertical distance.
     */
    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch + 4)));
    }

    /**
     * Calculates the zoom level of the camera based on mouse input.
     */
    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.03f;

        distanceFromPlayer -= zoomLevel;

        if (distanceFromPlayer < 5) {
            distanceFromPlayer = 5;
        }
    }

    /**
     * Calculates the pitch angle of the camera based on mouse input.
     */
    private void calculatePitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.2f;
            pitch = Math.max(0, Math.min(pitch - pitchChange, 90));
        }
    }

    /**
     * Calculates the angle around the player based on mouse input.
     */
    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }
}
