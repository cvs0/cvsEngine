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

package engine.toolbox;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.entities.Camera;
import engine.terrains.Terrain;

/**
 * A utility class for performing mouse ray casting to interact with 3D terrain.
 */
public class MousePicker {

	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;

	private Vector3f currentRay = new Vector3f();

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	
	private Terrain terrain;
	private Vector3f currentTerrainPoint;

	/**
	 * Creates a new MousePicker instance with the specified camera, projection matrix, and terrain.
	 *
	 * @param cam        The camera used for picking.
	 * @param projection The projection matrix.
	 * @param terrain    The terrain to interact with.
	 */
	public MousePicker(Camera cam, Matrix4f projection, Terrain terrain) {
	    camera = cam;
	    projectionMatrix = projection;
	    viewMatrix = MathUtils.createViewMatrix(camera);
	    this.terrain = terrain;
	}

	
	/**
     * Retrieves the current terrain point where the mouse ray intersects the terrain.
     *
     * @return The current terrain point or null if no intersection.
     */
    public Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}

	/**
     * Retrieves the current ray projected from the mouse cursor.
     *
     * @return The current ray.
     */
    public Vector3f getCurrentRay() {
		return currentRay;
	}

    /**
     * Updates the mouse picker, recalculating the current ray and terrain point.
     */
    public void update() {
		viewMatrix = MathUtils.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
		} else {
			currentTerrainPoint = null;
		}
	}

	/**
     * Calculates the mouse ray based on mouse coordinates.
     *
     * @return The calculated mouse ray.
     */
    private Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}

	/**
     * Converts eye coordinates to world coordinates.
     *
     * @param eyeCoords The eye coordinates.
     * @return The corresponding world coordinates.
     */
    private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}

	/**
     * Converts clip coordinates to eye coordinates.
     *
     * @param clipCoords The clip coordinates.
     * @return The corresponding eye coordinates.
     */
    private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	/**
     * Retrieves normalized device coordinates from mouse coordinates.
     *
     * @param mouseX The X coordinate of the mouse.
     * @param mouseY The Y coordinate of the mouse.
     * @return The normalized device coordinates.
     */
    private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / Display.getWidth() - 1f;
		float y = (2.0f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}
	
	/**
     * Gets a point on the ray at a specified distance.
     *
     * @param ray      The ray vector.
     * @param distance The distance along the ray.
     * @return The point on the ray at the given distance.
     */
    private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	
	/**
     * Performs binary search to find the intersection point of the ray and terrain.
     *
     * @param count  The current recursion count.
     * @param start  The start of the search range.
     * @param finish The end of the search range.
     * @param ray    The ray to intersect with the terrain.
     * @return The intersection point or null if not found.
     */
    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			Terrain terrain = getTerrain(endPoint.getX(), endPoint.getZ());
			if (terrain != null) {
				return endPoint;
			} else {
				return null;
			}
		}
		if (intersectionInRange(start, half, ray)) {
			return binarySearch(count + 1, start, half, ray);
		} else {
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	/**
     * Checks if the intersection point with the terrain is within a specified range.
     *
     * @param start  The start of the range.
     * @param finish The end of the range.
     * @param ray    The ray to intersect with the terrain.
     * @return True if the intersection is within the range, false otherwise.
     */
    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}

	/**
     * Checks if a point is under the ground level of the terrain.
     *
     * @param testPoint The point to check.
     * @return True if the point is under the ground, false otherwise.
     */
    private boolean isUnderGround(Vector3f testPoint) {
		Terrain terrain = getTerrain(testPoint.getX(), testPoint.getZ());
		float height = 0;
		if (terrain != null) {
			height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
		}
		if (testPoint.y < height) {
			return true;
		} else {
			return false;
		}
	}

	/**
     * Retrieves the terrain at a specified world coordinate.
     *
     * @param worldX The world X coordinate.
     * @param worldZ The world Z coordinate.
     * @return The terrain at the given coordinates or null if none found.
     */
    private Terrain getTerrain(float worldX, float worldZ) {
		return terrain;
	}

}