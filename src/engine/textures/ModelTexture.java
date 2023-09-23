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
 * A class representing a texture applied to a 3D model.
 */
public class ModelTexture {
    
    private int textureID;
    private int normalMap;
    
    private float shineDamper = 1;
    
    private boolean hasTransparency = false;
    private boolean useFakeLighting = false;
    
    private int numberOfRows = 1;
    private float reflectivity = 0;
    
    public int getNormalMap() {
		return normalMap;
	}
    
    /**
     * Get the number of rows in the texture atlas (used for animated textures).
     * @return The number of rows.
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }
    
    public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
	}

    /**
     * Set the number of rows in the texture atlas (used for animated textures).
     * @param numberOfRows The number of rows.
     */
    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    /**
     * Check if fake lighting should be applied to the texture.
     * @return True if fake lighting is enabled, false otherwise.
     */
    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    /**
     * Set whether fake lighting should be applied to the texture.
     * @param useFakeLighting True to enable fake lighting, false to disable it.
     */
    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    /**
     * Check if the texture has transparency.
     * @return True if the texture has transparency, false otherwise.
     */
    public boolean isHasTransparency() {
        return hasTransparency;
    }

    /**
     * Set whether the texture has transparency.
     * @param hasTransparency True if the texture has transparency, false otherwise.
     */
    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    /**
     * Get the shine damper value for the texture (affects specular lighting).
     * @return The shine damper value.
     */
    public float getShineDamper() {
        return shineDamper;
    }

    /**
     * Set the shine damper value for the texture (affects specular lighting).
     * @param shineDamper The shine damper value.
     */
    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    /**
     * Get the reflectivity value for the texture (affects specular lighting).
     * @return The reflectivity value.
     */
    public float getReflectivity() {
        return reflectivity;
    }

    /**
     * Set the reflectivity value for the texture (affects specular lighting).
     * @param reflectivity The reflectivity value.
     */
    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
    
    /**
     * Create a new model texture with the specified texture ID.
     * @param id The ID of the texture.
     */
    public ModelTexture(int id) {
        this.textureID = id;
    }
    
    /**
     * Get the ID of the texture.
     * @return The texture ID.
     */
    public int getID() {
        return this.textureID;
    }
}
