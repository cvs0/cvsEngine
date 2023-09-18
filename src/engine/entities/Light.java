package engine.entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	private Vector3f position;
	private Vector3f colour;

	public Light(Vector3f position, Vector3f colour) {
		super();
		this.position = position;
		this.colour = colour;
	}	
	
	/**
	 * Retrieves the current position of the light source.
	 *
	 * @return A {@link Vector3f} representing the current position of the light source.
	 */
	public Vector3f getPosition() {
	    return position;
	}

	/**
	 * Sets the position of the light source to the specified coordinates.
	 *
	 * @param position A {@link Vector3f} representing the new position for the light source.
	 */
	public void setPosition(Vector3f position) {
	    this.position = position;
	}

	/**
	 * Retrieves the current color of the light source.
	 *
	 * @return A {@link Vector3f} representing the current color (RGB values) of the light source.
	 */
	public Vector3f getColour() {
	    return colour;
	}

	/**
	 * Sets the color of the light source to the specified RGB values.
	 *
	 * @param colour A {@link Vector3f} representing the new color (RGB values) for the light source.
	 */
	public void setColour(Vector3f colour) {
	    this.colour = colour;
	}

}
