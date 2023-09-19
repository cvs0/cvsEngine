package engine.fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.shaders.ShaderProgram;

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
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

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

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	protected void loadOutlineColour(Vector3f outlineColour) {
		super.loadVector(location_outlineColour, outlineColour);
	}
	
	protected void loadWidth(float width) {
		super.loadFloat(location_width, width);
	}
	
	protected void loadEdge(float edge) {
		super.loadFloat(location_edge, edge);
	}
	
	protected void loadBorderWidth(float borderWidth) {
		super.loadFloat(location_borderWidth, borderWidth);
	}
	
	protected void loadBorderEdge(float borderEdge) {
		super.loadFloat(location_borderEdge, borderEdge);
	}
	
	protected void loadOffset(Vector2f offset) {
		super.load2DVector(location_offset, offset);
	}
	
	protected void loadColour(Vector3f colour){
		super.loadVector(location_colour, colour);
	}
	
	protected void loadTranslation(Vector2f translation){
		super.load2DVector(location_translation, translation);
	}


}