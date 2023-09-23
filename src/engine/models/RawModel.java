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

/**
 * Represents a raw model, consisting of a VAO (Vertex Array Object) ID and vertex count.
 */
public class RawModel {
    private int vaoID;
    private int vertexCount;

    /**
     * Constructs a RawModel with the provided VAO ID and vertex count.
     *
     * @param vaoID       The ID of the Vertex Array Object (VAO).
     * @param vertexCount The number of vertices in the model.
     */
    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    /**
     * Retrieves the vertex count of this raw model.
     *
     * @return The number of vertices.
     */
    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Retrieves the VAO ID of this raw model.
     *
     * @return The VAO ID.
     */
    public int getVaoID() {
        return vaoID;
    }
}
