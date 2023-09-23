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

package engine.water;

/**
 * Represents a WaterTile object used for rendering water in a 3D scene.
 */
public class WaterTile {

    /**
     * The size of the water tile in the game world.
     */
    public static final float TILE_SIZE = 60;

    private float height;
    private float x, z;

    /**
     * Creates a WaterTile at a specific location with a given height.
     * 
     * @param centerX The X-coordinate of the center of the water tile.
     * @param centerZ The Z-coordinate of the center of the water tile.
     * @param height  The height of the water tile.
     */
    public WaterTile(float centerX, float centerZ, float height) {
        this.x = centerX;
        this.z = centerZ;
        this.height = height;
    }

    /**
     * Get the height of the water tile.
     * 
     * @return The height of the water tile.
     */
    public float getHeight() {
        return height;
    }

    /**
     * Get the X-coordinate of the center of the water tile.
     * 
     * @return The X-coordinate.
     */
    public float getX() {
        return x;
    }

    /**
     * Get the Z-coordinate of the center of the water tile.
     * 
     * @return The Z-coordinate.
     */
    public float getZ() {
        return z;
    }
}