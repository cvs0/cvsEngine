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

package engine.textures;

import java.nio.ByteBuffer;

/**
 * Represents texture data containing pixel information and dimensions.
 */
public class TextureData {

    private int width;          // The width of the texture data.
    private int height;         // The height of the texture data.
    private ByteBuffer buffer;  // The buffer containing pixel data.

    /**
     * Constructs a new TextureData instance.
     *
     * @param buffer The ByteBuffer containing pixel data.
     * @param width  The width of the texture data.
     * @param height The height of the texture data.
     */
    public TextureData(ByteBuffer buffer, int width, int height) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the width of the texture data.
     *
     * @return The width of the texture data.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the texture data.
     *
     * @return The height of the texture data.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the ByteBuffer containing pixel data.
     *
     * @return The ByteBuffer containing pixel data.
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }
}
