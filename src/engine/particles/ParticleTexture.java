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

package engine.particles;

public class ParticleTexture {
    
    private int textureID;
    private int numberOfRows;
    
    /**
     * Creates a new ParticleTexture with the specified texture ID and number of rows.
     * 
     * @param textureID   The ID of the texture.
     * @param numberOfRows The number of rows in the texture atlas.
     */
    public ParticleTexture(int textureID, int numberOfRows) {
        this.textureID = textureID;
        this.numberOfRows = numberOfRows;
    }

    /**
     * Get the ID of the particle texture.
     * 
     * @return The texture ID.
     */
    public int getTextureID() {
        return textureID;
    }

    /**
     * Get the number of rows in the texture atlas.
     * 
     * @return The number of rows.
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }
}