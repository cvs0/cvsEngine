package engine.shaders;

public class StaticShader extends ShaderProgram {
	
	public static final String VERTEX_FILE = "src/engine/shaders/vertexShader.txt";
	public static final String FRAGMENT_FILE = "src/engine/shaders/fragmentShader.txt";

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
}
