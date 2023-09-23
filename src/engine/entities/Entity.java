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

import org.lwjgl.util.vector.Vector3f;

import engine.models.TexturedModel;

/**
 * Represents an entity in a 3D environment.
 */
public class Entity {

    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;

    private int textureIndex = 0;

    /**
     * Constructs a new Entity with the specified parameters.
     *
     * @param model    The textured model representing this entity.
     * @param position The initial position of the entity.
     * @param rotX     The initial rotation around the X-axis.
     * @param rotY     The initial rotation around the Y-axis.
     * @param rotZ     The initial rotation around the Z-axis.
     * @param scale    The initial scale of the entity.
     */
    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    /**
     * Constructs a new Entity with the specified parameters, including a texture index.
     *
     * @param model       The textured model representing this entity.
     * @param index       The index of the texture.
     * @param position    The initial position of the entity.
     * @param rotX        The initial rotation around the X-axis.
     * @param rotY        The initial rotation around the Y-axis.
     * @param rotZ        The initial rotation around the Z-axis.
     * @param scale       The initial scale of the entity.
     */
    public Entity(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.textureIndex = index;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    /**
     * Calculates the X offset for the entity's texture coordinate.
     *
     * @return The X offset for the entity's texture coordinate.
     */
    public float getTextureXOffset() {
        if (model != null && model.getTexture() != null) {
            int numberOfRows = model.getTexture().getNumberOfRows();
            if (numberOfRows > 0) {
                int column = textureIndex % numberOfRows;
                return (float) column / (float) numberOfRows;
            } else {
                return 0.0f;
            }
        } else {
            return 0.0f;
        }
    }

    /**
     * Calculates the Y offset for the entity's texture coordinate.
     *
     * @return The Y offset for the entity's texture coordinate.
     */
    public float getTextureYOffset() {
        if (model != null && model.getTexture() != null) {
            int numberOfRows = model.getTexture().getNumberOfRows();
            if (numberOfRows > 0) {
                int row = textureIndex / numberOfRows;
                return (float) row / (float) numberOfRows;
            } else {
                return 0.0f;
            }
        } else {
            return 0.0f;
        }
    }

    /**
     * Increases the entity's position by the specified amounts in each dimension.
     *
     * @param dx The change in the X-coordinate.
     * @param dy The change in the Y-coordinate.
     * @param dz The change in the Z-coordinate.
     */
    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    /**
     * Increases the entity's rotation by the specified amounts around each axis.
     *
     * @param dx The change in rotation around the X-axis.
     * @param dy The change in rotation around the Y-axis.
     * @param dz The change in rotation around the Z-axis.
     */
    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;

        while (this.rotX < -360) {
            this.rotX += 360;
        }
        while (this.rotX >= 360) {
            this.rotX -= 360;
        }

        while (this.rotY < -360) {
            this.rotY += 360;
        }
        while (this.rotY >= 360) {
            this.rotY -= 360;
        }

        while (this.rotZ < -360) {
            this.rotZ += 360;
        }
        while (this.rotZ >= 360) {
            this.rotZ -= 360;
        }
    }

    /**
     * Retrieves the textured model of the entity.
     *
     * @return The textured model representing the entity.
     */
    public TexturedModel getModel() {
        return model;
    }

    /**
     * Sets the textured model for the entity.
     *
     * @param model The textured model to set.
     */
    public void setModel(TexturedModel model) {
        this.model = model;
    }

    /**
     * Retrieves the current position of the entity.
     *
     * @return A {@link Vector3f} representing the entity's current position.
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Sets the position of the entity to the specified coordinates.
     *
     * @param position A {@link Vector3f} representing the new position to set for the entity.
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     * Retrieves the rotation around the X-axis of the entity.
     *
     * @return The current rotation around the X-axis.
     */
    public float getRotX() {
        return rotX;
    }

    /**
     * Sets the rotation around the X-axis of the entity.
     *
     * @param rotX The new rotation around the X-axis to set.
     */
    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    /**
     * Retrieves the rotation around the Y-axis of the entity.
     *
     * @return The current rotation around the Y-axis.
     */
    public float getRotY() {
        return rotY;
    }

    /**
     * Sets the rotation around the Y-axis of the entity.
     *
     * @param rotY The new rotation around the Y-axis to set.
     */
    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    /**
     * Retrieves the rotation around the Z-axis of the entity.
     *
     * @return The current rotation around the Z-axis.
     */
    public float getRotZ() {
        return rotZ;
    }

    /**
     * Sets the rotation around the Z-axis of the entity.
     *
     * @param rotZ The new rotation around the Z-axis to set.
     */
    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    /**
     * Retrieves the scale factor of the entity.
     *
     * @return The current scale factor of the entity.
     */
    public float getScale() {
        return scale;
    }

    /**
     * Sets the scale factor of the entity.
     *
     * @param scale The new scale factor to set for the entity.
     */
    public void setScale(float scale) {
        this.scale = scale;
    }
}
