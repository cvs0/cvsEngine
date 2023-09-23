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
 * Stores the vertex data for all the quads on which a text will be rendered.
 * @author Karl
 *
 */
public class TextMeshData {
	
	private float[] vertexPositions;
	private float[] textureCoords;
	
	/**
     * Constructs a new TextMeshData object with the specified vertex positions and texture coordinates.
     *
     * @param vertexPositions An array of vertex positions.
     * @param textureCoords  An array of texture coordinates.
     */
	protected TextMeshData(float[] vertexPositions, float[] textureCoords){
		this.vertexPositions = vertexPositions;
		this.textureCoords = textureCoords;
	}

	/**
     * Retrieves the vertex positions of the text mesh.
     *
     * @return An array of vertex positions.
     */
	public float[] getVertexPositions() {
		return vertexPositions;
	}

	/**
     * Retrieves the texture coordinates of the text mesh.
     *
     * @return An array of texture coordinates.
     */
	public float[] getTextureCoords() {
		return textureCoords;
	}

	/**
     * Retrieves the total number of vertices in the text mesh.
     *
     * @return The vertex count.
     */
	public int getVertexCount() {
		return vertexPositions.length / 2;
	}

}
