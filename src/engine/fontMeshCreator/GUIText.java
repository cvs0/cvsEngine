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

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.fontRendering.TextMaster;

/**
 * Represents a piece of text in the game.
 * 
 * @author Karl
 *
 */
public class GUIText {

	private String textString;
	private float width;
	private float edge;
	private float borderWidth;
	private float borderEdge;
	private Vector2f offset;
	private Vector3f outlineColour;
	private float fontSize;

	private int textMeshVao;
	private int vertexCount;
	private Vector3f colour = new Vector3f(0f, 0f, 0f);

	private Vector2f position;
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false;

	/**
     * Get the width of the text.
     * 
     * @return The width of the text.
     */
    public float getWidth() {
        return width;
    }

    /**
     * Set the width of the text.
     * 
     * @param width - The new width of the text.
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Get the edge factor for the text.
     * 
     * @return The edge factor of the text.
     */
    public float getEdge() {
        return edge;
    }

    /**
     * Set the edge factor for the text.
     * 
     * @param edge - The new edge factor of the text.
     */
    public void setEdge(float edge) {
        this.edge = edge;
    }

    /**
     * Get the border width of the text.
     * 
     * @return The border width of the text.
     */
    public float getBorderWidth() {
        return borderWidth;
    }

    /**
     * Set the border width of the text.
     * 
     * @param borderWidth - The new border width of the text.
     */
    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    /**
     * Get the border edge factor of the text.
     * 
     * @return The border edge factor of the text.
     */
    public float getBorderEdge() {
        return borderEdge;
    }

    /**
     * Set the border edge factor of the text.
     * 
     * @param borderEdge - The new border edge factor of the text.
     */
    public void setBorderEdge(float borderEdge) {
        this.borderEdge = borderEdge;
    }

    /**
     * Get the offset of the text.
     * 
     * @return The offset of the text as a Vector2f.
     */
    public Vector2f getOffset() {
        return offset;
    }

    /**
     * Set the offset of the text.
     * 
     * @param offset - The new offset of the text as a Vector2f.
     */
    public void setOffset(Vector2f offset) {
        this.offset = offset;
    }
    
    public void setText(String text) {
    	this.textString = text;
    }

    /**
     * Get the outline color of the text.
     * 
     * @return The outline color of the text as a Vector3f.
     */
    public Vector3f getOutlineColour() {
        return outlineColour;
    }

    /**
     * Set the outline color of the text.
     * 
     * @param outlineColour - The new outline color of the text as a Vector3f.
     */
    public void setOutlineColour(Vector3f outlineColour) {
        this.outlineColour = outlineColour;
    }

    /**
     * Check if the text should be centered.
     * 
     * @return true if the text is centered, false otherwise.
     */
    public boolean isCenterText() {
        return centerText;
    }

	/**
	 * Creates a new text, loads the text's quads into a VAO, and adds the text
	 * to the screen.
	 * 
	 * @param text
	 *            - the text.
	 * @param fontSize
	 *            - the font size of the text, where a font size of 1 is the
	 *            default size.
	 * @param font
	 *            - the font that this text should use.
	 * @param position
	 *            - the position on the screen where the top left corner of the
	 *            text should be rendered. The top left corner of the screen is
	 *            (0, 0) and the bottom right is (1, 1).
	 * @param maxLineLength
	 *            - basically the width of the virtual page in terms of screen
	 *            width (1 is full screen width, 0.5 is half the width of the
	 *            screen, etc.) Text cannot go off the edge of the page, so if
	 *            the text is longer than this length it will go onto the next
	 *            line. When text is centered it is centered into the middle of
	 *            the line, based on this line length value.
	 * @param centered
	 *            - whether the text should be centered or not.
	 */
	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered, float width, float edge, float borderWidth, float borderEdge,
			Vector2f offset, Vector3f outlineColour) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		this.width = width;
		this.edge = edge;
		this.borderWidth = borderWidth;
		this.borderEdge = borderEdge;
		this.offset = offset;
		this.outlineColour = outlineColour;
		TextMaster.loadText(this);
	}

	/**
	 * Remove the text from the screen.
	 */
	public void remove() {
		TextMaster.removeText(this);
	}

	/**
	 * @return The font used by this text.
	 */
	public FontType getFont() {
		return font;
	}

	/**
	 * Set the colour of the text.
	 * 
	 * @param r
	 *            - red value, between 0 and 1.
	 * @param g
	 *            - green value, between 0 and 1.
	 * @param b
	 *            - blue value, between 0 and 1.
	 */
	public void setColour(float r, float g, float b) {
		colour.set(r, g, b);
	}

	/**
	 * @return the colour of the text.
	 */
	public Vector3f getColour() {
		return colour;
	}

	/**
	 * @return The number of lines of text. This is determined when the text is
	 *         loaded, based on the length of the text and the max line length
	 *         that is set.
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * @return The position of the top-left corner of the text in screen-space.
	 *         (0, 0) is the top left corner of the screen, (1, 1) is the bottom
	 *         right.
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**
	 * @return the ID of the text's VAO, which contains all the vertex data for
	 *         the quads on which the text will be rendered.
	 */
	public int getMesh() {
		return textMeshVao;
	}

	/**
	 * Set the VAO and vertex count for this text.
	 * 
	 * @param vao
	 *            - the VAO containing all the vertex data for the quads on
	 *            which the text will be rendered.
	 * @param verticesCount
	 *            - the total number of vertices in all of the quads.
	 */
	public void setMeshInfo(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}

	/**
	 * @return The total number of vertices of all the text's quads.
	 */
	public int getVertexCount() {
		return this.vertexCount;
	}

	/**
	 * @return the font size of the text (a font size of 1 is normal).
	 */
	protected float getFontSize() {
		return fontSize;
	}

	/**
	 * Sets the number of lines that this text covers (method used only in
	 * loading).
	 * 
	 * @param number
	 */
	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	/**
	 * @return {@code true} if the text should be centered.
	 */
	protected boolean isCentered() {
		return centerText;
	}

	/**
	 * @return The maximum length of a line of this text.
	 */
	protected float getMaxLineSize() {
		return lineMaxSize;
	}

	/**
	 * @return The string of text.
	 */
	protected String getTextString() {
		return textString;
	}

}
