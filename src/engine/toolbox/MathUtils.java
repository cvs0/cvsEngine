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

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.entities.Camera;

/**
 * A utility class for math related functions.
 */
public class MathUtils {
	
	/**
	 * Calculates the barrycentric interpolation of a point within a triangle.
	 *
	 * @param p1 The first vertex of the triangle.
	 * @param p2 The second vertex of the triangle.
	 * @param p3 The third vertex of the triangle.
	 * @param pos The position within the triangle as a 2D vector.
	 * @return The interpolated value at the given position within the triangle.
	 * @throws IllegalArgumentException If input values are invalid.
	 */
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
	    float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
	    
	    if (Float.isNaN(p1.x) || Float.isNaN(p1.y) || Float.isNaN(p1.z) // p1
	    		|| Float.isInfinite(p1.x) || Float.isInfinite(p1.y) || Float.isInfinite(p1.z) // p1
	    		
	    		|| Float.isNaN(p2.x) || Float.isNaN(p2.y) || Float.isNaN(p2.z) // p2
	    		|| Float.isInfinite(p2.x) || Float.isInfinite(p2.y) || Float.isInfinite(p2.z) // p2
	    		
	    		|| Float.isNaN(p3.x) || Float.isNaN(p3.y) || Float.isNaN(p3.z) // p3
	    		|| Float.isInfinite(p3.x) || Float.isInfinite(p3.y) || Float.isInfinite(p3.z) // p3
	    		
	    		|| Float.isNaN(pos.x) || Float.isNaN(pos.y) // pos
	    		|| Float.isInfinite(pos.x) || Float.isInfinite(pos.y) // pos
	    		) {
	            throw new IllegalArgumentException("Invalid input values.");
	        }
	    
	    if (Math.abs(det) < 1e-6f) {
	        return Float.NaN;
	    }

	    float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
	    float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
	    float l3 = 1.0f - l1 - l2;

	    if (l1 < 0.0f || l2 < 0.0f || l3 < 0.0f || l1 > 1.0f || l2 > 1.0f || l3 > 1.0f) {
	        return Float.NaN;
	    }

	    return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	/**
	 * Creates a transformation matrix based on translation, rotation, and scale values.
	 *
	 * @param translation The translation vector (x, y, z).
	 * @param rx The rotation around the x-axis (in degrees).
	 * @param ry The rotation around the y-axis (in degrees).
	 * @param rz The rotation around the z-axis (in degrees).
	 * @param scale The uniform scale factor.
	 * @return The transformation matrix.
	 * @throws IllegalArgumentException If input values are invalid.
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
        float rz, float scale) {
		// Check if translation vector is null
		if (translation == null) {
			throw new IllegalArgumentException("Translation vector cannot be null.");
		}

		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();

		// Check if translation values are valid (not NaN or infinite)
		if (Float.isNaN(translation.x) || Float.isNaN(translation.y) || Float.isNaN(translation.z) ||
			Float.isInfinite(translation.x) || Float.isInfinite(translation.y) || Float.isInfinite(translation.z)) {
			throw new IllegalArgumentException("Invalid translation values.");
		}

		// Translate the matrix based on the translation vector
		Matrix4f.translate(translation, matrix, matrix);

		// Check if rotation angles are valid (within the range -360 to 360 degrees)
		if (rx < -360f || rx > 360f || ry < -360f || ry > 360f || rz < -360f || rz > 360f) {
			throw new IllegalArgumentException("Invalid rotation angles.");
		}

		// Rotate the matrix based on the rotation angles
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);

		// Check if scale value is valid (not NaN, infinite, or non-positive)
		if (Float.isNaN(scale) || Float.isInfinite(scale) || scale <= 0f) {
			throw new IllegalArgumentException("Invalid scale value.");
		}

		// Scale the matrix based on the scale value
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

		return matrix;
	}

	/**
	 * Creates a transformation matrix based on translation and scale vectors (2D).
	 *
	 * @param translation The translation vector (x, y).
	 * @param scale The scale vector (x, y).
	 * @return The transformation matrix.
	 * @throws IllegalArgumentException If input values are invalid.
	 */
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		// Check if translation or scale vectors are null
		if (translation == null || scale == null) {
			throw new IllegalArgumentException("Translation and scale vectors cannot be null.");
		}
	
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
	
		// Check if translation values are valid (not NaN or infinite)
		if (Float.isNaN(translation.x) || Float.isNaN(translation.y) ||
			Float.isInfinite(translation.x) || Float.isInfinite(translation.y)) {
			throw new IllegalArgumentException("Invalid translation values.");
		}
	
		// Translate the matrix based on the translation vector
		Matrix4f.translate(new Vector3f(translation.x, translation.y, 0f), matrix, matrix);
	
		// Check if scale values are valid (not NaN, infinite, or non-positive)
		if (Float.isNaN(scale.x) || Float.isNaN(scale.y) ||
			Float.isInfinite(scale.x) || Float.isInfinite(scale.y) ||
			scale.x <= 0f || scale.y <= 0f) {
			throw new IllegalArgumentException("Invalid scale values.");
		}
	
		// Scale the matrix based on the scale vector
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
	
		return matrix;
	}

	/**
	 * Creates a view matrix based on the camera's position and orientation.
	 *
	 * @param camera The camera for which to create the view matrix.
	 * @return The view matrix.
	 * @throws IllegalArgumentException If the camera is null or has invalid properties.
	 */
	public static Matrix4f createViewMatrix(Camera camera) {
		// Check if the camera is null
		if (camera == null) {
			throw new IllegalArgumentException("Camera cannot be null.");
		}
	
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
	
		// Convert pitch and yaw to radians
		float pitch = (float) Math.toRadians(camera.getPitch());
		float yaw = (float) Math.toRadians(camera.getYaw());
	
		// Check if pitch and yaw are valid (not NaN or infinite)
		if (Float.isNaN(pitch) || Float.isNaN(yaw) ||
			Float.isInfinite(pitch) || Float.isInfinite(yaw)) {
			throw new IllegalArgumentException("Invalid camera orientation angles.");
		}
	
		// Rotate the view matrix based on pitch and yaw
		Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate(yaw, new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
	
		Vector3f cameraPos = camera.getPosition();
	
		// Check if camera position is valid (not NaN or infinite)
		if (Float.isNaN(cameraPos.x) || Float.isNaN(cameraPos.y) || Float.isNaN(cameraPos.z) ||
			Float.isInfinite(cameraPos.x) || Float.isInfinite(cameraPos.y) || Float.isInfinite(cameraPos.z)) {
			throw new IllegalArgumentException("Invalid camera position.");
		}
	
		// Translate the view matrix based on the negative camera position
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
	
		return viewMatrix;
	}
}
