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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TextMeshCreator {

	protected static final double LINE_HEIGHT = 0.03f;
	protected static final int SPACE_ASCII = 32;

	private MetaFile metaData;

	/**
     * Constructs a new TextMeshCreator with the specified MetaFile containing character data.
     *
     * @param metaFile The MetaFile containing character data.
     */
	protected TextMeshCreator(File metaFile) {
		metaData = new MetaFile(metaFile);
	}

	/**
     * Creates the text mesh data for the specified GUIText object.
     *
     * @param text The GUIText object for which to create the text mesh data.
     * @return The TextMeshData containing the vertex positions and texture coordinates.
     */
	protected TextMeshData createTextMesh(GUIText text) {
		List<Line> lines = createStructure(text);
		TextMeshData data = createQuadVertices(text, lines);
		return data;
	}

	/**
	 * Creates the structure of lines and words from the specified GUIText object's text content.
	 * Each character is analyzed, and words and lines are formed accordingly.
	 *
	 * @param text The GUIText object containing the text content.
	 * @return A list of Line objects representing the structured text.
	 */
	private List<Line> createStructure(GUIText text) {
		char[] chars = text.getTextString().toCharArray();
		List<Line> lines = new ArrayList<Line>();
		Line currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
		Word currentWord = new Word(text.getFontSize());
		for (char c : chars) {
			int ascii = (int) c;
			if (ascii == SPACE_ASCII) {
				boolean added = currentLine.attemptToAddWord(currentWord);
				if (!added) {
					lines.add(currentLine);
					currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
					currentLine.attemptToAddWord(currentWord);
				}
				currentWord = new Word(text.getFontSize());
				continue;
			}
			Character character = metaData.getCharacter(ascii);
			currentWord.addCharacter(character);
		}
		completeStructure(lines, currentLine, currentWord, text);
		return lines;
	}


	/**
	 * Completes the structure of lines and words by adding the current word and line to the list.
	 * This is used to finalize the text structure.
	 *
	 * @param lines       The list of Line objects.
	 * @param currentLine The current Line being processed.
	 * @param currentWord The current Word being processed.
	 * @param text        The GUIText object containing the text content.
	 */
	private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, GUIText text) {
		boolean added = currentLine.attemptToAddWord(currentWord);
		if (!added) {
			lines.add(currentLine);
			currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
			currentLine.attemptToAddWord(currentWord);
		}
		lines.add(currentLine);
	}

	/**
	 * Creates the vertex and texture coordinate data for rendering text quads.
	 *
	 * @param text  The GUIText object for which to create the text mesh data.
	 * @param lines The list of Line objects representing the structured text.
	 * @return The TextMeshData containing the vertex positions and texture coordinates.
	 */
	private TextMeshData createQuadVertices(GUIText text, List<Line> lines) {
		text.setNumberOfLines(lines.size());
		double curserX = 0f;
		double curserY = 0f;
		List<Float> vertices = new ArrayList<Float>();
		List<Float> textureCoords = new ArrayList<Float>();
		for (Line line : lines) {
			if (text.isCentered()) {
				curserX = (line.getMaxLength() - line.getLineLength()) / 2;
			}
			for (Word word : line.getWords()) {
				for (Character letter : word.getCharacters()) {
					addVerticesForCharacter(curserX, curserY, letter, text.getFontSize(), vertices);
					addTexCoords(textureCoords, letter.getxTextureCoord(), letter.getyTextureCoord(),
							letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
					curserX += letter.getxAdvance() * text.getFontSize();
				}
				curserX += metaData.getSpaceWidth() * text.getFontSize();
			}
			curserX = 0;
			curserY += LINE_HEIGHT * text.getFontSize();
		}		
		return new TextMeshData(listToArray(vertices), listToArray(textureCoords));
	}

	/**
	 * Adds vertices for a character to the list of vertices.
	 *
	 * @param curserX   The current X position of the cursor.
	 * @param curserY   The current Y position of the cursor.
	 * @param character The Character object representing the character to add.
	 * @param fontSize  The font size of the text.
	 * @param vertices  The list of vertices to add to.
	 */
	private void addVerticesForCharacter(double curserX, double curserY, Character character, double fontSize,
			List<Float> vertices) {
		double x = curserX + (character.getxOffset() * fontSize);
		double y = curserY + (character.getyOffset() * fontSize);
		double maxX = x + (character.getSizeX() * fontSize);
		double maxY = y + (character.getSizeY() * fontSize);
		double properX = (2 * x) - 1;
		double properY = (-2 * y) + 1;
		double properMaxX = (2 * maxX) - 1;
		double properMaxY = (-2 * maxY) + 1;
		addVertices(vertices, properX, properY, properMaxX, properMaxY);
	}


	/**
	 * Adds vertices for a quad to the list of vertices.
	 *
	 * @param vertices The list of vertices to add to.
	 * @param x        The X coordinate of the quad.
	 * @param y        The Y coordinate of the quad.
	 * @param maxX     The maximum X coordinate of the quad.
	 * @param maxY     The maximum Y coordinate of the quad.
	 */
	private static void addVertices(List<Float> vertices, double x, double y, double maxX, double maxY) {
		vertices.add((float) x);
		vertices.add((float) y);
		vertices.add((float) x);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) y);
		vertices.add((float) x);
		vertices.add((float) y);
	}

	/**
	 * Adds texture coordinates for a quad to the list of texture coordinates.
	 *
	 * @param texCoords The list of texture coordinates to add to.
	 * @param x          The X texture coordinate of the quad.
	 * @param y          The Y texture coordinate of the quad.
	 * @param maxX       The maximum X texture coordinate of the quad.
	 * @param maxY       The maximum Y texture coordinate of the quad.
	 */
	private static void addTexCoords(List<Float> texCoords, double x, double y, double maxX, double maxY) {
		texCoords.add((float) x);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) y);
	}

	/**
	 * Converts a List of Float objects to a float array.
	 *
	 * @param listOfFloats The List of Float objects to convert.
	 * @return A float array containing the elements from the List.
	 */
	private static float[] listToArray(List<Float> listOfFloats) {
		float[] array = new float[listOfFloats.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = listOfFloats.get(i);
		}
		return array;
	}
}
