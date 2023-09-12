package engine.entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	private Vector3f positions;
	private Vector3f colour;

	public Light(Vector3f positions, Vector3f colour) {
		super();
		this.positions = positions;
		this.colour = colour;
	}	
	
	public Vector3f getPositions() {
		return positions;
	}

	public void setPositions(Vector3f positions) {
		this.positions = positions;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
}
