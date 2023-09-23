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

package engine.models;

import engine.textures.ModelTexture;

/**
 * Represents a textured model, combining a raw model and a model texture.
 */
public class TexturedModel {
    private RawModel rawModel;
    private ModelTexture texture;

    /**
     * Constructs a TexturedModel with the provided raw model and model texture.
     *
     * @param model   The raw model representing the geometry.
     * @param texture The model texture applied to the model.
     */
    public TexturedModel(RawModel model, ModelTexture texture) {
        this.rawModel = model;
        this.texture = texture;
    }

    /**
     * Retrieves the raw model associated with this textured model.
     *
     * @return The raw model.
     */
    public RawModel getRawModel() {
        return rawModel;
    }

    /**
     * Retrieves the model texture applied to this textured model.
     *
     * @return The model texture.
     */
    public ModelTexture getTexture() {
        return texture;
    }
}
