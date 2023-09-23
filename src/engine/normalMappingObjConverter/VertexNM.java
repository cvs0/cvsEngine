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

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a vertex with position, texture, and normal data for normal mapping.
 */
public class VertexNM {
	
	private static final int NO_INDEX = -1;
	
	private Vector3f position;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private VertexNM duplicateVertex = null;
	private int index;
	private float length;
	private List<Vector3f> tangents = new ArrayList<Vector3f>();
	private Vector3f averagedTangent = new Vector3f(0, 0, 0);
	
	/**
     * Creates a new vertex with the given index and position.
     *
     * @param index    The index of the vertex.
     * @param position The position vector of the vertex.
     */
    protected VertexNM(int index, Vector3f position) {
		this.index = index;
		this.position = position;
		this.length = position.length();
	}
	
    /**
     * Adds a tangent vector to this vertex for normal mapping.
     *
     * @param tangent The tangent vector to add.
     */
    protected void addTangent(Vector3f tangent) {
		tangents.add(tangent);
	}
	
    /**
     * Duplicates this vertex with a new index.
     *
     * @param newIndex The index for the duplicated vertex.
     * @return A new duplicated vertex.
     */
    protected VertexNM duplicate(int newIndex) {
		VertexNM vertex = new VertexNM(newIndex, position);
		vertex.tangents = this.tangents;
		return vertex;
	}
	
    /**
     * Averages the stored tangent vectors to compute an averaged tangent.
     */
    protected void averageTangents() {
		if(tangents.isEmpty()){
			return;
		}
		for(Vector3f tangent : tangents){
			Vector3f.add(averagedTangent, tangent, averagedTangent);
		}
		averagedTangent.normalise();
	}
	
    /**
     * Retrieves the averaged tangent vector for this vertex.
     *
     * @return The averaged tangent vector.
     */
    protected Vector3f getAverageTangent() {
		return averagedTangent;
	}
	
    /**
     * Gets the index of this vertex.
     *
     * @return The index of this vertex.
     */
    protected int getIndex() {
		return index;
	}
	
    /**
     * Gets the length of the position vector of this vertex.
     *
     * @return The length of the position vector.
     */
    protected float getLength() {
		return length;
	}
	
    /**
     * Checks if the texture and normal indices for this vertex are set.
     *
     * @return True if both texture and normal indices are set, false otherwise.
     */
    protected boolean isSet() {
		return textureIndex!=NO_INDEX && normalIndex!=NO_INDEX;
	}
	
    /**
     * Checks if this vertex has the same texture and normal indices as another vertex.
     *
     * @param textureIndexOther The texture index of the other vertex.
     * @param normalIndexOther  The normal index of the other vertex.
     * @return True if both texture and normal indices match, false otherwise.
     */
    protected boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther) {
		return textureIndexOther==textureIndex && normalIndexOther==normalIndex;
	}
	
    /**
     * Sets the texture index for this vertex.
     *
     * @param textureIndex The texture index to set.
     */
    protected void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}
	
    /**
     * Sets the normal index for this vertex.
     *
     * @param normalIndex The normal index to set.
     */
    protected void setNormalIndex(int normalIndex) {
		this.normalIndex = normalIndex;
	}

    /**
     * Retrieves the position vector of this vertex.
     *
     * @return The position vector.
     */
    protected Vector3f getPosition() {
		return position;
	}

    /**
     * Gets the texture index of this vertex.
     *
     * @return The texture index.
     */
    protected int getTextureIndex() {
		return textureIndex;
	}

    /**
     * Gets the normal index of this vertex.
     *
     * @return The normal index.
     */
    protected int getNormalIndex() {
		return normalIndex;
	}

    /**
     * Gets the duplicated vertex associated with this vertex.
     *
     * @return The duplicated vertex, or null if none exists.
     */
    protected VertexNM getDuplicateVertex() {
		return duplicateVertex;
	}

    /**
     * Sets the duplicated vertex associated with this vertex.
     *
     * @param duplicateVertex The duplicated vertex to set.
     */
    protected void setDuplicateVertex(VertexNM duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

}
