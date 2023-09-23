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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.fontMeshCreator.FontType;
import engine.fontMeshCreator.GUIText;
import engine.fontMeshCreator.TextMeshData;
import engine.renderEngine.Loader;

/**
 * Manages rendering and manipulation of GUI text elements.
 */
public class TextMaster {
	
	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	
	/**
     * Initializes the TextMaster with a loader.
     *
     * @param theLoader The loader to use for text rendering.
     */
    public static void init(Loader theLoader) {
		renderer = new FontRenderer();
		loader = theLoader;
	}
	
    /**
     * Renders all the GUI text elements.
     */
	public static void render(){
		renderer.render(texts);
	}
	
	/**
     * Loads a GUI text element for rendering.
     *
     * @param text The GUI text element to load.
     */
	public static void loadText(GUIText text){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		
		text.setMeshInfo(vao, data.getVertexCount());
		
		List<GUIText> textBatch = texts.get(font);
		
		if(textBatch == null){
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		
		textBatch.add(text);
	}
	
	/**
     * Removes a GUI text element from rendering.
     *
     * @param text The GUI text element to remove.
     */
	public static void removeText(GUIText text){
		List<GUIText> textBatch = texts.get(text.getFont());
		
		textBatch.remove(text);
		
		if(textBatch.isEmpty()){
			texts.remove(texts.get(text.getFont()));
		}
	}
	
	/**
     * Cleans up resources used by the TextMaster.
     */
	public static void cleanUp(){
		renderer.cleanUp();
	}

}