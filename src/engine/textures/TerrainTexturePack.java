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

/**
 * A class representing a pack of textures for terrain rendering, including a background texture and three color textures.
 */
public class TerrainTexturePack {
    
    private TerrainTexture backgroundTexture;
    private TerrainTexture rTexture;
    private TerrainTexture gTexture;
    private TerrainTexture bTexture;
    
    /**
     * Create a new terrain texture pack with the specified textures.
     * @param backgroundTexture The background texture.
     * @param rTexture The red channel texture.
     * @param gTexture The green channel texture.
     * @param bTexture The blue channel texture.
     */
    public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture) {
        this.backgroundTexture = backgroundTexture;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
    }

    /**
     * Get the background texture of the terrain texture pack.
     * @return The background texture.
     */
    public TerrainTexture getBackgroundTexture() {
        return backgroundTexture;
    }

    /**
     * Get the red channel texture of the terrain texture pack.
     * @return The red channel texture.
     */
    public TerrainTexture getrTexture() {
        return rTexture;
    }

    /**
     * Get the green channel texture of the terrain texture pack.
     * @return The green channel texture.
     */
    public TerrainTexture getgTexture() {
        return gTexture;
    }

    /**
     * Get the blue channel texture of the terrain texture pack.
     * @return The blue channel texture.
     */
    public TerrainTexture getbTexture() {
        return bTexture;
    }
}
