package engine.guis;

import org.lwjgl.util.vector.Vector2f;

/**
 * Represents a GUI texture, including its texture ID, position, and scale.
 */
public class GuiTexture {
    private int texture;

    private Vector2f position;
    private Vector2f scale;

    /**
     * Constructs a GuiTexture with the provided texture ID, position, and scale.
     *
     * @param texture  The ID of the texture to be displayed.
     * @param position The position of the GUI texture on the screen.
     * @param scale    The scale of the GUI texture.
     */
    public GuiTexture(int texture, Vector2f position, Vector2f scale) {
        super();
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    /**
     * Retrieves the texture ID of this GUI texture.
     *
     * @return The texture ID.
     */
    public int getTexture() {
        return texture;
    }

    /**
     * Retrieves the position of this GUI texture.
     *
     * @return A {@link Vector2f} representing the position.
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * Retrieves the scale of this GUI texture.
     *
     * @return A {@link Vector2f} representing the scale.
     */
    public Vector2f getScale() {
        return scale;
    }
}
