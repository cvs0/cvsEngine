package engine.toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.entities.Camera;

public class MousePicker {
	private Vector3f currentRay;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	
	private Camera camera;
	
	public MousePicker(Camera cam, Matrix4f projection) {
		this.camera = cam;
		this.projectionMatrix = projection;
		this.viewMatrix = MathUtils.createViewMatrix(camera);
	}
	
	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public void update() {
		viewMatrix = MathUtils.createViewMatrix(camera);
		currentRay = calculateMouseRay();
	}
	
	private Vector3f calculateMouseRay() {
		
	}
}
