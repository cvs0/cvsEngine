package engine.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram {
	
	public static final String VERTEX_FILE = "src/engine/shaders/vertexShader.txt";
	public static final String FRAGMENT_FILE = "src/engine/shaders/fragmentShader.txt";
	
	private int location_tranformationMatrix;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_tranformationMatrix = super.getUniformLocation("tranformationMatrix;");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_tranformationMatrix, matrix);
	}
}
