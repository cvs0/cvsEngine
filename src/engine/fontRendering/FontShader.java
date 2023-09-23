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

package engine.fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.shaders.ShaderProgram;

/**
 * Represents a shader program used for rendering text with fonts.
 */
public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/engine/fontRendering/fontVertex.txt";
	private static final String FRAGMENT_FILE = "src/engine/fontRendering/fontFragment.txt";
	
	private int location_colour;
	private int location_translation;
	private int location_width;
	private int location_edge;
	private int location_borderWidth;
	private int location_borderEdge;
	private int location_offset;
	private int location_outlineColour;
	
	/**
     * Creates a new FontShader program.
     */
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/**
     * Retrieves the locations of uniform variables in the shader.
     */
	@Override
	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");
		location_width = super.getUniformLocation("width");
		location_edge = super.getUniformLocation("edge");
		location_borderWidth = super.getUniformLocation("borderWidth");
		location_borderEdge = super.getUniformLocation("borderEdge");
		location_offset = super.getUniformLocation("offset");
		location_outlineColour = super.getUniformLocation("outlineColour");
	}

	/**
     * Binds attribute locations for the shader program.
     */
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	/**
     * Loads the outline color for rendering text.
     *
     * @param outlineColour The color for the text outline.
     */
	protected void loadOutlineColour(Vector3f outlineColour) {
		super.loadVector(location_outlineColour, outlineColour);
	}
	
	/**
     * Loads the width for rendering text.
     *
     * @param width The width of the text.
     */
	protected void loadWidth(float width) {
		super.loadFloat(location_width, width);
	}
	
	/**
     * Loads the edge value for rendering text.
     *
     * @param edge The edge value for text rendering.
     */
	protected void loadEdge(float edge) {
		super.loadFloat(location_edge, edge);
	}
	
	/**
     * Loads the border width for rendering text.
     *
     * @param borderWidth The border width of the text.
     */
	protected void loadBorderWidth(float borderWidth) {
		super.loadFloat(location_borderWidth, borderWidth);
	}
	
	/**
     * Loads the border edge for rendering text.
     *
     * @param borderEdge The border edge value for text rendering.
     */
	protected void loadBorderEdge(float borderEdge) {
		super.loadFloat(location_borderEdge, borderEdge);
	}
	
	/**
     * Loads the offset for rendering text.
     *
     * @param offset The offset vector for text rendering.
     */
	protected void loadOffset(Vector2f offset) {
		super.load2DVector(location_offset, offset);
	}
	
	/**
     * Loads the color for rendering text.
     *
     * @param colour The color of the text.
     */
	protected void loadColour(Vector3f colour){
		super.loadVector(location_colour, colour);
	}
	
	/**
     * Loads the translation for rendering text.
     *
     * @param translation The translation vector for text rendering.
     */
	protected void loadTranslation(Vector2f translation){
		super.load2DVector(location_translation, translation);
	}


}