package engine.toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.entities.Camera;

/**
 * A utility class containing mathematical functions commonly used in game development.
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
	 */
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
	    float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);

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
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        
        if (Float.isNaN(translation.x) || Float.isNaN(translation.y) || Float.isNaN(translation.z) ||
            Float.isNaN(rx) || Float.isNaN(ry) || Float.isNaN(rz) || Float.isNaN(scale) ||
            Float.isInfinite(translation.x) || Float.isInfinite(translation.y) || Float.isInfinite(translation.z) ||
            Float.isInfinite(rx) || Float.isInfinite(ry) || Float.isInfinite(rz) || Float.isInfinite(scale)) {
            throw new IllegalArgumentException("Invalid input values");
        }

        Matrix4f.translate(translation, matrix, matrix);

        if (rx < -360f || rx > 360f || ry < -360f || ry > 360f || rz < -360f || rz > 360f) {
            throw new IllegalArgumentException("Invalid rotation angles");
        }

        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);

        if (Float.isNaN(scale) || scale <= 0f) {
            throw new IllegalArgumentException("Invalid scale value");
        }

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
	    Matrix4f matrix = new Matrix4f();
	    matrix.setIdentity();
	    
	    if (Float.isNaN(translation.x) || Float.isNaN(translation.y) ||
	        Float.isNaN(scale.x) || Float.isNaN(scale.y) ||
	        Float.isInfinite(translation.x) || Float.isInfinite(translation.y) ||
	        Float.isInfinite(scale.x) || Float.isInfinite(scale.y)) {
	        throw new IllegalArgumentException("Invalid input values");
	    }

	    Matrix4f.translate(new Vector3f(translation.x, translation.y, 0f), matrix, matrix);
	    
	    if (Float.isNaN(scale.x) || Float.isNaN(scale.y) ||
	        scale.x <= 0f || scale.y <= 0f) {
	        throw new IllegalArgumentException("Invalid scale values");
	    }

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
	    Matrix4f viewMatrix = new Matrix4f();
	    
	    if (camera == null) {
	        throw new IllegalArgumentException("Camera cannot be null");
	    }
	    
	    viewMatrix.setIdentity();
	    
	    float pitch = (float) Math.toRadians(camera.getPitch());
	    float yaw = (float) Math.toRadians(camera.getYaw());
	    
	    if (Float.isNaN(pitch) || Float.isNaN(yaw) ||
	        Float.isInfinite(pitch) || Float.isInfinite(yaw)) {
	        throw new IllegalArgumentException("Invalid camera orientation angles");
	    }
	    
	    Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
	    Matrix4f.rotate(yaw, new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
	    
	    Vector3f cameraPos = camera.getPosition();
	    
	    if (Float.isNaN(cameraPos.x) || Float.isNaN(cameraPos.y) || Float.isNaN(cameraPos.z) ||
	        Float.isInfinite(cameraPos.x) || Float.isInfinite(cameraPos.y) || Float.isInfinite(cameraPos.z)) {
	        throw new IllegalArgumentException("Invalid camera position");
	    }
	    
	    Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
	    
	    Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
	    
	    return viewMatrix;
	}

}
