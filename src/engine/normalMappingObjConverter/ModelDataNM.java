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

package engine.normalMappingObjConverter;

/**
 * Represents model data for a model with normal mapping.
 */
public class ModelDataNM {

    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private float[] tangents;
    private int[] indices;
    private float furthestPoint;

    /**
     * Creates a new instance of ModelDataNM.
     *
     * @param vertices      The array of vertex coordinates.
     * @param textureCoords The array of texture coordinates.
     * @param normals       The array of normal vectors.
     * @param tangents      The array of tangent vectors.
     * @param indices       The array of vertex indices.
     * @param furthestPoint The furthest point in the model.
     */
    public ModelDataNM(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices, float furthestPoint) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
        this.tangents = tangents;
    }

    /**
     * Gets the array of vertex coordinates.
     *
     * @return The vertex coordinates.
     */
    public float[] getVertices() {
        return vertices;
    }

    /**
     * Gets the array of texture coordinates.
     *
     * @return The texture coordinates.
     */
    public float[] getTextureCoords() {
        return textureCoords;
    }

    /**
     * Gets the array of tangent vectors.
     *
     * @return The tangent vectors.
     */
    public float[] getTangents() {
        return tangents;
    }

    /**
     * Gets the array of normal vectors.
     *
     * @return The normal vectors.
     */
    public float[] getNormals() {
        return normals;
    }

    /**
     * Gets the array of vertex indices.
     *
     * @return The vertex indices.
     */
    public int[] getIndices() {
        return indices;
    }

    /**
     * Gets the furthest point in the model.
     *
     * @return The furthest point.
     */
    public float getFurthestPoint() {
        return furthestPoint;
    }
}