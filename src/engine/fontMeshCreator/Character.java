/**
 * CvsEngine
 *
 * @author cvs0, Karl
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

package engine.fontMeshCreator;

/**
 * Simple data structure class holding information about a certain glyph in the
 * font texture atlas. All sizes are for a font-size of 1.
 * 
 * @author Karl
 */
public class Character {

    private int id;
    private double xTextureCoord;
    private double yTextureCoord;
    private double xMaxTextureCoord;
    private double yMaxTextureCoord;
    private double xOffset;
    private double yOffset;
    private double sizeX;
    private double sizeY;
    private double xAdvance;

    /**
     * Constructs a new Character object with the specified parameters.
     * 
     * @param id            - the ASCII value of the character.
     * @param xTextureCoord - the x texture coordinate for the top left corner of
     *                      the character in the texture atlas.
     * @param yTextureCoord - the y texture coordinate for the top left corner of
     *                      the character in the texture atlas.
     * @param xTexSize      - the width of the character in the texture atlas.
     * @param yTexSize      - the height of the character in the texture atlas.
     * @param xOffset       - the x distance from the cursor to the left edge of
     *                      the character's quad.
     * @param yOffset       - the y distance from the cursor to the top edge of the
     *                      character's quad.
     * @param sizeX         - the width of the character's quad in screen space.
     * @param sizeY         - the height of the character's quad in screen space.
     * @param xAdvance      - how far in pixels the cursor should advance after
     *                      adding this character.
     */
    protected Character(int id, double xTextureCoord, double yTextureCoord, double xTexSize, double yTexSize,
            double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance) {
        this.id = id;
        this.xTextureCoord = xTextureCoord;
        this.yTextureCoord = yTextureCoord;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.xMaxTextureCoord = xTexSize + xTextureCoord;
        this.yMaxTextureCoord = yTexSize + yTextureCoord;
        this.xAdvance = xAdvance;
    }

    /**
     * Get the ASCII value of the character.
     * 
     * @return The ASCII value of the character.
     */
    protected int getId() {
        return id;
    }

    /**
     * Get the x texture coordinate for the top left corner of the character in the
     * texture atlas.
     * 
     * @return The x texture coordinate.
     */
    protected double getxTextureCoord() {
        return xTextureCoord;
    }

    /**
     * Get the y texture coordinate for the top left corner of the character in the
     * texture atlas.
     * 
     * @return The y texture coordinate.
     */
    protected double getyTextureCoord() {
        return yTextureCoord;
    }

    /**
     * Get the x texture coordinate for the bottom right corner of the character in
     * the texture atlas.
     * 
     * @return The x coordinate.
     */
    protected double getXMaxTextureCoord() {
        return xMaxTextureCoord;
    }

    /**
     * Get the y texture coordinate for the bottom right corner of the character in
     * the texture atlas.
     * 
     * @return The y coordinate.
     */
    protected double getYMaxTextureCoord() {
        return yMaxTextureCoord;
    }

    /**
     * Get the x distance from the cursor to the left edge of the character's quad.
     * 
     * @return The x offset.
     */
    protected double getxOffset() {
        return xOffset;
    }

    /**
     * Get the y distance from the cursor to the top edge of the character's quad.
     * 
     * @return The y offset.
     */
    protected double getyOffset() {
        return yOffset;
    }

    /**
     * Get the width of the character's quad in screen space.
     * 
     * @return The width.
     */
    protected double getSizeX() {
        return sizeX;
    }

    /**
     * Get the height of the character's quad in screen space.
     * 
     * @return The height.
     */
    protected double getSizeY() {
        return sizeY;
    }

    /**
     * Get how far in pixels the cursor should advance after adding this character.
     * 
     * @return The x advance value.
     */
    protected double getxAdvance() {
        return xAdvance;
    }
}
